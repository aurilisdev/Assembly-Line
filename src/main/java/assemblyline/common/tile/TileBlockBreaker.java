package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import electrodynamics.SoundRegister;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.particle.ParticleAPI;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileBlockBreaker extends TileFrontHarvester {
	public float clientRunningTicks;
	public boolean works = false;
	public float progress = 0;

	public TileBlockBreaker(BlockPos pos, BlockState state) {
		super(DeferredRegisters.TILE_BLOCKBREAKER.get(), pos, state, Constants.BLOCKBREAKER_USAGE * 20, (int)ElectrodynamicsCapabilities.DEFAULT_VOLTAGE, "blockbreaker");
	}

	@Override
	public void tickCommon(ComponentTickable tickable) {
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		//ignore dims; for rendering purposes
		currentLength = DEFAULT_CHECK_LENGTH;
		currentWidth = DEFAULT_CHECK_WIDTH;
		currentHeight = 2;
		ticksSinceCheck = (int) (progress * 100);
		currentWaitTime = 100;
		powerUsageMultiplier = 1;
		
		if (tickable.getTicks() % 20 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		ComponentDirection direction = getComponent(ComponentType.Direction);
		BlockPos block = worldPosition.offset(direction.getDirection().getOpposite().getNormal());
		BlockState blockState = level.getBlockState(block);
		works = !blockState.isAir() && blockState.getDestroySpeed(level, block) > 0 && electro.getJoulesStored() >= Constants.BLOCKBREAKER_USAGE;
		if (works) {
			double k1 = 1 / blockState.getDestroySpeed(level, block) / 30;
			if (progress < 1) {
				progress += k1 * 5;
				electro.extractPower(TransferPack.joulesVoltage(Constants.BLOCKBREAKER_USAGE, ElectrodynamicsCapabilities.DEFAULT_VOLTAGE), false);
			} else {
				if (!level.isClientSide) {
//		    Block block = state.getBlock();
					level.destroyBlock(block, true);
					progress = 0;
					// output block here somewhere
				}
				works = false;
			}
		} else {
			progress = 0;
		}
	}

	@Override
	public void tickServer(ComponentTickable component) {
		if (component.getTicks() % 20 == 0) {
			ComponentPacketHandler handler = getComponent(ComponentType.PacketHandler);
			handler.sendGuiPacketToTracking();
		}
	}

	@Override
	public void tickClient(ComponentTickable component) {
		if (works) {
			ComponentDirection direction = getComponent(ComponentType.Direction);
			clientRunningTicks++;
			if (component.getTicks() % 200 == 0) {
				SoundAPI.playSound(SoundRegister.SOUND_MINERALGRINDER.get(), SoundSource.BLOCKS, 0.5f, 1, worldPosition);
			}
			BlockPos offset = worldPosition.offset(direction.getDirection().getOpposite().getNormal());
			Block block = level.getBlockState(offset).getBlock();
			double d4 = level.random.nextDouble() * 1.2 + offset.getX() - 0.1;
			double d5 = level.random.nextDouble() * 1.2 + offset.getY() - 0.1;
			double d6 = level.random.nextDouble() * 1.2 + offset.getZ() - 0.1;
			ParticleAPI.addGrindedParticle(level, d4, d5, d6, 0.0D, 5D, 0.0D, block.defaultBlockState(), worldPosition);
		}
	}

	@Override
	public double getUsage() {
		return Constants.BLOCKBREAKER_USAGE;
	}

	@Override
	public ComponentInventory getInv(TileFrontHarvester harvester) {
		return new ComponentInventory(harvester).size(3).upgrades(3).valid(machineValidator());
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerBlockBreaker(id, player, getComponent(ComponentType.Inventory), getCoordsArray());
	}
}
