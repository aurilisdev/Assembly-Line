package assemblyline.common.tile.belt;

import java.util.List;

import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.object.CachedTileOutput;

public class TileDetector extends GenericTile {

	public boolean isPowered = false;

	private CachedTileOutput beltCache;

	public TileDetector(BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineTiles.TILE_DETECTOR.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	public void tickServer(ComponentTickable component) {

		if(beltCache == null) {
			beltCache = new CachedTileOutput(getLevel(), getBlockPos().relative(getFacing()));
		}

		if(component.getTicks() % 10 == 0 && !(beltCache.getSafe() instanceof GenericTileConveyorBelt)) {
			beltCache.update(getBlockPos().relative(getFacing()));
		}

		if(beltCache.getSafe() instanceof GenericTileConveyorBelt belt) {

			if(belt.getItemOnBelt().isEmpty() && isPowered) {
				isPowered = false;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			} if(!belt.getItemOnBelt().isEmpty() && !isPowered) {
				isPowered = true;
				level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
			}

		} else if (component.getTicks() % 4 == 0) {

			BlockPos relative = worldPosition.relative(getFacing());

			List<ItemEntity> entities = level.getEntities(EntityType.ITEM, new AABB(relative), entity -> entity != null && !entity.getItem().isEmpty());
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
	public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	@Override
	public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putBoolean("powered", isPowered);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		isPowered = compound.getBoolean("powered");
	}
}
