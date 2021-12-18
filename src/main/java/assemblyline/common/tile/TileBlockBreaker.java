package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.settings.Constants;
import electrodynamics.SoundRegister;
import electrodynamics.api.capability.electrodynamic.CapabilityElectrodynamic;
import electrodynamics.api.particle.ParticleAPI;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileBlockBreaker extends GenericTile {
	public float clientRunningTicks;
	public boolean works = false;
	public float progress = 0;

	public TileBlockBreaker(BlockPos worldPosition, BlockState blockState) {
		super(DeferredRegisters.TILE_BLOCKBREAKER.get(), worldPosition, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient).tickCommon(this::tickCommon));
		addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.BLOCKBREAKER_USAGE * 20).relativeInput(Direction.SOUTH));
	}

	public void tickCommon(ComponentTickable component) {
		ComponentDirection direction = getComponent(ComponentType.Direction);
		BlockPos off = worldPosition.offset(direction.getDirection().getOpposite().getNormal());
		BlockState state = level.getBlockState(off);
		works = !state.isAir() && state.getDestroySpeed(level, off) > 0
				&& this.<ComponentElectrodynamic>getComponent(ComponentType.Electrodynamic).getJoulesStored() > Constants.BLOCKBREAKER_USAGE;
		if (works) {
			ComponentElectrodynamic electrodynamic = getComponent(ComponentType.Electrodynamic);
			double k1 = 1 / state.getDestroySpeed(level, off) / 30;
			if (progress < 1) {
				progress += k1 * 5;
				electrodynamic.extractPower(TransferPack.joulesVoltage(Constants.BLOCKBREAKER_USAGE, CapabilityElectrodynamic.DEFAULT_VOLTAGE),
						false);
			} else {
				if (!level.isClientSide) {
//		    Block block = state.getBlock();
					level.destroyBlock(off, true);
					progress = 0;
					// output block here somewhere
				}
				works = false;
			}
		} else {
			progress = 0;
		}
	}

	public void tickServer(ComponentTickable component) {
		if (component.getTicks() % 20 == 0) {
			ComponentPacketHandler handler = getComponent(ComponentType.PacketHandler);
			handler.sendGuiPacketToTracking();
		}
	}

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
}
