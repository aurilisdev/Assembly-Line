package assemblyline.common.tile;

import java.util.List;
import java.util.function.Predicate;

import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;

public class TileDetector extends GenericTile {

	public boolean isPowered = false;

	public TileDetector() {
		super(AssemblyLineBlockTypes.TILE_DETECTOR.get());
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	public void tickServer(ComponentTickable component) {
		if (component.getTicks() % 4 == 0) {
			List<ItemEntity> entities = level.getEntities(EntityType.ITEM, new AxisAlignedBB(worldPosition.relative(getFacing())), (Predicate<ItemEntity>) t -> t != null && !t.getItem().isEmpty());
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
	public ActionResultType use(PlayerEntity arg0, Hand arg1, BlockRayTraceResult arg2) {
		return ActionResultType.FAIL;
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
