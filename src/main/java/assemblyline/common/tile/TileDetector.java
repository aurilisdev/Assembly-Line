package assemblyline.common.tile;

import java.util.List;
import java.util.function.Predicate;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentTickable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileDetector extends GenericTileTicking {
    public boolean isPowered = false;

    public TileDetector() {
	super(DeferredRegisters.TILE_DETECTOR.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentTickable().tickServer(this::tickServer));
    }

    public void tickServer(ComponentTickable component) {
	if (component.getTicks() % 4 == 0) {
	    List<ItemEntity> entities = world.getEntitiesWithinAABB(EntityType.ITEM,
		    new AxisAlignedBB(pos.offset(this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection())),
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
