package assemblyline.common.tile;

import java.util.List;
import java.util.function.Predicate;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.ITickableTileBase;
import electrodynamics.common.tile.generic.GenericTileBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileDetector extends GenericTileBase implements ITickableTileBase {

    private int ticks;
    public boolean isPowered = false;

    public TileDetector() {
	super(DeferredRegisters.TILE_DETECTOR.get());
    }

    @Override
    public void tickServer() {
	ticks++;
	if (ticks % 4 == 0) {
	    List<ItemEntity> entities = world.getEntitiesWithinAABB(EntityType.ITEM,
		    new AxisAlignedBB(pos.offset(getFacing())),
		    (Predicate<ItemEntity>) t -> t != null && !t.getItem().isEmpty());
	    if (!entities.isEmpty()) {
		if (!isPowered) {
		    isPowered = true;
		    world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
		}
	    } else if (isPowered) {
		isPowered = false;
		world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
	    }

	}
    }
}
