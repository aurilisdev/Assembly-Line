package assemblyline.common.tile.belt;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.type.ComponentTickable;

public class TileDetector extends GenericTile {

    public boolean isPowered = false;

    public TileDetector(BlockPos worldPosition, BlockState blockState) {
	super(AssemblyLineTiles.TILE_DETECTOR.get(), worldPosition, blockState);
	addComponent(new ComponentTickable(this).tickServer(this::tickServer));
    }

    public void tickServer(Level level, ComponentTickable component) {
	BlockPos relative = worldPosition.relative(getFacing());
	if (level.getBlockEntity(relative) instanceof GenericTileConveyorBelt belt) {

	    boolean powered = !belt.getItemOnBelt().isEmpty();
	    if (isPowered == powered)
		return;

	    isPowered = powered;
	    level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
	    return;
	}

	if (component.getTicks() % 4 != 0)
	    return;

	boolean powered = !level.getEntities(EntityType.ITEM, new AABB(relative), entity -> !entity.getItem().isEmpty())
		.isEmpty();

	if (isPowered == powered)
	    return;

	isPowered = powered;
	level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
    }

    @Override
    public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hit) {
	return InteractionResult.PASS;
    }

    @Override
    public ItemInteractionResult useWithItem(Level level, ItemStack used, Player player, InteractionHand hand,
	    BlockHitResult hit) {
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
