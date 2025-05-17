package assemblyline.common.tile.belt;

import java.util.List;

import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.object.CachedTileOutput;

public class TileDetector extends GenericTile {

	public boolean isPowered = false;

	private CachedTileOutput beltCache;

	public TileDetector() {
		super(AssemblyLineTiles.TILE_DETECTOR.get());
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	public void tickServer(ComponentTickable component) {

		if(beltCache == null) {
			beltCache = new CachedTileOutput(getLevel(), getBlockPos().relative(getFacing()));
		}

		if(component.getTicks() % 10 == 0 && !(beltCache.getSafe() instanceof GenericTileConveyorBelt)) {
			beltCache.update(getBlockPos().relative(getFacing()));
		}

		if(beltCache.getSafe() instanceof GenericTileConveyorBelt) {
			
			GenericTileConveyorBelt belt = beltCache.getSafe();

			if(belt.getItemOnBelt().isEmpty() && isPowered) {
				isPowered = false;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			} if(!belt.getItemOnBelt().isEmpty() && !isPowered) {
				isPowered = true;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			}

		} else if (component.getTicks() % 4 == 0) {

			BlockPos relative = worldPosition.relative(getFacing());

			List<ItemEntity> entities = level.getEntities(EntityType.ITEM, new AxisAlignedBB(relative), entity -> entity != null && !entity.getItem().isEmpty());
			if (!entities.isEmpty()) {
				if (!isPowered) {
					isPowered = true;
					level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
				}
			}  else if (isPowered) {
				isPowered = false;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			}
		}
	}

	@Override
	public ActionResultType use(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return ActionResultType.PASS;
	}

	@Override
	public int getSignal(Direction dir) {
		return isPowered ? 15 : 0;
	}

	@Override
	public int getDirectSignal(Direction dir) {
		return getSignal(dir);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putBoolean("powered", isPowered);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		isPowered = compound.getBoolean("powered");
	}
}
