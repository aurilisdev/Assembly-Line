package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileCrate;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.CapabilityItemHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCrate extends Block {

    public final int size;

    public BlockCrate(int size) {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE));
	this.size = size;
    }

    @Deprecated
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	BlockEntity tile = worldIn.getBlockEntity(pos);
	if (tile instanceof GenericTile
		&& !(state.getBlock() == newState.getBlock() && state.getValue(BlockGenericMachine.FACING) != newState.getValue(BlockGenericMachine.FACING))) {
	    GenericTile generic = (GenericTile) tile;
	    if (generic.hasComponent(ComponentType.Inventory)) {
		Containers.dropContents(worldIn, pos, generic.getComponent(ComponentType.Inventory));
	    }
	}
	super.onRemove(state, worldIn, pos, newState, isMoving);

    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(this));
    }

    @Override
    @Deprecated
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
	    BlockHitResult hit) {
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
			    HopperBlockEntity.addItem(player.inventory, tile.getComponent(ComponentType.Inventory),
				    player.getItemInHand(handIn), Direction.EAST));
		}
	    }
	}
	return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
	return new TileCrate(size);
    }
}
