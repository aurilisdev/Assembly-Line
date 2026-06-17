package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineSounds;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import voltaic.api.particle.ParticleAPI;
import voltaic.api.sound.SoundAPI;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentContainerProvider;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentPacketHandler;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.registers.VoltaicCapabilities;

public class TileBlockBreaker extends TileOutlineArea {

    public SingleProperty<Integer> ticksSinceCheck = property(
	    new SingleProperty<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
    public SingleProperty<Integer> currentWaitTime = property(
	    new SingleProperty<>(PropertyTypes.INTEGER, "currentWaitTime", 0));
    public final SingleProperty<Boolean> works = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "works", false));
    public final SingleProperty<Double> progress = property(
	    new SingleProperty<>(PropertyTypes.DOUBLE, "progress", 0.0));

    public TileBlockBreaker(BlockPos pos, BlockState state) {
	super(AssemblyLineTiles.TILE_BLOCKBREAKER.get(), pos, state);
	addComponent(new ComponentPacketHandler(this));
	addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
	addComponent(new ComponentElectrodynamic(this, false, true)
		.setInputDirections(BlockEntityUtils.MachineDirection.FRONT)
		.voltage(VoltaicCapabilities.DEFAULT_VOLTAGE)
		.maxJoules(AssemblyLineConfig.INSTANCE.BLOCKBREAKER_USAGE.getAsDouble() * 20));
	addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().upgrades(3))
		.validUpgrades(ContainerBlockBreaker.VALID_UPGRADES).valid(machineValidator()));
	addComponent(new ComponentContainerProvider("blockbreaker", this)
		.createMenu((id, player) -> new ContainerBlockBreaker(id, player,
			getComponent(IComponentType.Inventory), getCoordsArray())));
	addComponent(new ComponentForgeEnergy(this));
	height.setValue(1);
    }

    public void tickServer(ComponentTickable component) {

	ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

	if (electro.getJoulesStored() < AssemblyLineConfig.INSTANCE.BLOCKBREAKER_USAGE.getAsDouble()) {
	    progress.setValue(0.0);
	    return;
	}

	ticksSinceCheck.setValue((int) (progress.getValue() * 100));
	currentWaitTime.setValue(100);

	Direction facing = getFacing();
	BlockPos block = worldPosition.offset(facing.getOpposite().getNormal());
	BlockState blockState = level.getBlockState(block);

	works.setValue(!blockState.isAir() && blockState.getDestroySpeed(level, block) > 0);

	if (!works.getValue()) {
	    progress.setValue(0.0);
	    return;
	}

	double k1 = 1 / blockState.getDestroySpeed(level, block) / 30;

	if (progress.getValue() < 1) {
	    progress.setValue(progress.getValue() + k1 * 5);

	    electro.joules(electro.getJoulesStored() - AssemblyLineConfig.INSTANCE.BLOCKBREAKER_USAGE.getAsDouble());

	    return;

	}

	level.destroyBlock(block, true);
	progress.setValue(0.0);

	works.setValue(false);

    }

    public void tickClient(ComponentTickable component) {
	if (!works.getValue()) {
	    return;
	}
	if (component.getTicks() % 200 == 0) {
	    SoundAPI.playSound(AssemblyLineSounds.SOUND_BLOCKBREAKER.get(), SoundSource.BLOCKS, 0.5f, 1, worldPosition);
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
	return works.getValue() ? 15 : 0;
    }

}
