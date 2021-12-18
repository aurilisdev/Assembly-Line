package assemblyline.common.tile;

import java.util.List;
import java.util.function.Predicate;

import assemblyline.DeferredRegisters;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileDetector extends GenericTile {
	public boolean isPowered = false;

	public TileDetector(BlockPos worldPosition, BlockState blockState) {
		super(DeferredRegisters.TILE_DETECTOR.get(), worldPosition, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentTickable().tickServer(this::tickServer));
	}

	public void tickServer(ComponentTickable component) {
		if (component.getTicks() % 4 == 0) {
			List<ItemEntity> entities = level.getEntities(EntityType.ITEM,
					new AABB(worldPosition.relative(this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection())),
					(Predicate<ItemEntity>) t -> t != null && !t.getItem().isEmpty());
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
}
