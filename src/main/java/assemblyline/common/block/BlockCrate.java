package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileCrate;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.GenericTileTicking;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockCrate extends BaseEntityBlock {

    public final int size;

    public BlockCrate(int size) {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops());
	this.size = size;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	BlockEntity tile = worldIn.getBlockEntity(pos);
	if (tile instanceof GenericTile generic && !(state.getBlock() == newState.getBlock()
		&& state.getValue(BlockGenericMachine.FACING) != newState.getValue(BlockGenericMachine.FACING))) {
	    if (generic.hasComponent(ComponentType.Inventory)) {
		Containers.dropContents(worldIn, pos, generic.<ComponentInventory>getComponent(ComponentType.Inventory));
	    }
	}
	super.onRemove(state, worldIn, pos, newState, isMoving);

    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
	return RenderShape.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(this));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
	if (!worldIn.isClientSide) {
	    TileCrate tile = (TileCrate) worldIn.getBlockEntity(pos);
	    if (tile != null) {
		if (player.isShiftKeyDown()) {
		    ComponentInventory inv = tile.getComponent(ComponentType.Inventory);
		    for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).resolve().get()
				.extractItem(i, inv.getMaxStackSize(), false);
			if (!stack.isEmpty()) {
			    ItemEntity item = new ItemEntity(worldIn, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5, stack);
			    worldIn.addFreshEntity(item);
			    break;
			}
		    }

		} else {
		    player.setItemSlot(handIn == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND,
			    HopperBlockEntity.addItem(player.getInventory(), tile.getComponent(ComponentType.Inventory), player.getItemInHand(handIn),
				    Direction.EAST));
		}
	    }
	}
	return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	return new TileCrate(size, pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type) {
	return this::tick;
    }

    public <T extends BlockEntity> void tick(Level lvl, BlockPos pos, BlockState state, T t) {
	if (t instanceof GenericTileTicking tick) {
	    tick.tick();
	}
    }
}
