package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.Constants;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TileBlockPlacer extends GenericTile {

	public TileBlockPlacer(BlockPos worldPosition, BlockState blockState) {
		super(DeferredRegisters.TILE_BLOCKPLACER.get(), worldPosition, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient).tickCommon(this::tickCommon));
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.BLOCKPLACER_USAGE * 20).relativeInput(Direction.SOUTH));
		addComponent(new ComponentInventory(this).size(1));
		addComponent(new ComponentContainerProvider("container.blockplacer")
				.createMenu((id, player) -> new ContainerBlockPlacer(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		ComponentDirection direction = getComponent(ComponentType.Direction);
		ComponentInventory inventory = getComponent(ComponentType.Inventory);
		BlockPos off = worldPosition.offset(direction.getDirection().getOpposite().getNormal());
		BlockState state = level.getBlockState(off);
		if (tick.getTicks() % 20 == 0) {
			ComponentElectrodynamic electrodynamic = getComponent(ComponentType.Electrodynamic);
			if (electrodynamic.getJoulesStored() > Constants.BLOCKPLACER_USAGE) {
				electrodynamic.extractPower(TransferPack.joulesVoltage(Constants.BLOCKBREAKER_USAGE, ElectrodynamicsCapabilities.DEFAULT_VOLTAGE),
						false);
				if (state.isAir()) {
					ItemStack stack = inventory.getItem(0);
					if (!stack.isEmpty() && stack.getItem() instanceof BlockItem bi) {
						Block b = bi.getBlock();
						BlockState newState = b.getStateForPlacement(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, stack,
								new BlockHitResult(Vec3.ZERO, direction.getDirection(), off, false)));
						if (newState.canSurvive(level, off)) {
							level.setBlockAndUpdate(off, newState);
							stack.shrink(1);
						}
					}
				}
			}
		}
	}

	private void tickCommon(ComponentTickable tick) {
	}

	private void tickClient(ComponentTickable tick) {
	}

}
