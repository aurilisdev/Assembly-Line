package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.particle.ParticleAPI;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileBlockBreaker extends TileFrontHarvester {

	public final Property<Boolean> works = property(new Property<>(PropertyType.Boolean, "works", false));
	public final Property<Double> progress = property(new Property<>(PropertyType.Double, "progress", 0.0));

	public TileBlockBreaker(BlockPos pos, BlockState state) {
		super(AssemblyLineBlockTypes.TILE_BLOCKBREAKER.get(), pos, state, Constants.BLOCKBREAKER_USAGE * 20, (int) ElectrodynamicsCapabilities.DEFAULT_VOLTAGE, "blockbreaker");
		height.set(2);
	}

	@Override
	public void tickCommon(ComponentTickable tickable) {

	}

	@Override
	public void tickServer(ComponentTickable component) {

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		
		if(electro.getJoulesStored() < Constants.BLOCKBREAKER_USAGE) {
			progress.set(0.0);
			return;
		}
		
		ticksSinceCheck.set((int) (progress.get() * 100));
		currentWaitTime.set(100);

		Direction facing = getFacing();
		BlockPos block = worldPosition.offset(facing.getOpposite().getNormal());
		BlockState blockState = level.getBlockState(block);
		
		works.set(!blockState.isAir() && blockState.getDestroySpeed(level, block) > 0);
		
		if(!works.get()) {
			progress.set(0.0);
			return;
		}
		
		double k1 = 1 / blockState.getDestroySpeed(level, block) / 30;
		
		if (progress.get() < 1) {
			progress.set(progress.get() + k1 * 5);
			
			electro.joules(electro.getJoulesStored() - Constants.BLOCKBREAKER_USAGE);
			
			return;
			
		} 
		
		if (!level.isClientSide) {
			
			level.destroyBlock(block, true); 
			progress.set(0.0);

		}
		works.set(false);
		
	}

	@Override
	public void tickClient(ComponentTickable component) {
		if (!works.get()) {
			return;
		}
		if (component.getTicks() % 200 == 0) {
			SoundAPI.playSound(ElectrodynamicsSounds.SOUND_MINERALGRINDER.get(), SoundSource.BLOCKS, 0.5f, 1, worldPosition);
		}
		BlockPos offset = worldPosition.offset(getFacing().getOpposite().getNormal());
		Block block = level.getBlockState(offset).getBlock();
		double d4 = level.random.nextDouble() * 1.2 + offset.getX() - 0.1;
		double d5 = level.random.nextDouble() * 1.2 + offset.getY() - 0.1;
		double d6 = level.random.nextDouble() * 1.2 + offset.getZ() - 0.1;
		ParticleAPI.addGrindedParticle(level, d4, d5, d6, 0.0D, 5D, 0.0D, block.defaultBlockState(), worldPosition);
	}

	@Override
	public double getUsage() {
		return Constants.BLOCKBREAKER_USAGE;
	}

	@Override
	public ComponentInventory getInv(TileFrontHarvester harvester) {
		return new ComponentInventory(harvester, InventoryBuilder.newInv().upgrades(3)).validUpgrades(ContainerFrontHarvester.VALID_UPGRADES).valid(machineValidator());
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerBlockBreaker(id, player, getComponent(IComponentType.Inventory), getCoordsArray());
	}

	@Override
	public int getComparatorSignal() {
		return works.get() ? 15 : 0;
	}

}
