package assemblyline.common.tile;

import java.util.List;
import java.util.function.Predicate;

import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class TileDetector extends GenericTile {
	public boolean isPowered = false;

	public TileDetector(BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineBlockTypes.TILE_DETECTOR.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	public void tickServer(ComponentTickable component) {
		if (component.getTicks() % 4 == 0) {
			List<ItemEntity> entities = level.getEntities(EntityType.ITEM, new AABB(worldPosition.relative(getFacing())), (Predicate<ItemEntity>) t -> t != null && !t.getItem().isEmpty());
			if (!entities.isEmpty()) {
				if (!isPowered) {
					isPowered = true;
					level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
				}
			} else if (isPowered) {
				isPowered = false;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			}
		}
	}

	@Override
	public InteractionResult use(Player arg0, InteractionHand arg1, BlockHitResult arg2) {
		return InteractionResult.FAIL;
	}

	@Override
	public int getSignal(Direction dir) {
		return isPowered ? 15 : 0;
	}

	@Override
	public int getDirectSignal(Direction dir) {
		return getSignal(dir);
	}
}
