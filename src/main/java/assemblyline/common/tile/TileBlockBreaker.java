package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineTiles;
import electrodynamics.api.particle.ParticleAPI;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileBlockBreaker extends TileOutlineArea {

	public Property<Integer> ticksSinceCheck = property(new Property<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
	public Property<Integer> currentWaitTime = property(new Property<>(PropertyTypes.INTEGER, "currentWaitTime", 0));
	public final Property<Boolean> works = property(new Property<>(PropertyTypes.BOOLEAN, "works", false));
	public final Property<Double> progress = property(new Property<>(PropertyTypes.DOUBLE, "progress", 0.0));

	public TileBlockBreaker(BlockPos pos, BlockState state) {
		super(AssemblyLineTiles.TILE_BLOCKBREAKER.get(), pos, state);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.BLOCKBREAKER_USAGE * 20));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().upgrades(3)).validUpgrades(ContainerBlockBreaker.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider("container.blockbreaker", this).createMenu((id, player) -> new ContainerBlockBreaker(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
		height.set(1);
	}

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

		level.destroyBlock(block, true);
		progress.set(0.0);

		works.set(false);
		
	}

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
	public int getComparatorSignal() {
		return works.get() ? 15 : 0;
	}

}
