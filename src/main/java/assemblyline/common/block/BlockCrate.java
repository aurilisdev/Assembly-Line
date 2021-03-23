package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileCrate;
import electrodynamics.api.tile.GenericTile;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.common.block.BlockGenericMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockCrate extends Block {

    public BlockCrate() {
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE));
    }

    @Deprecated
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	TileEntity tile = worldIn.getTileEntity(pos);
	if (tile instanceof GenericTile
		&& !(state.getBlock() == newState.getBlock() && state.get(BlockGenericMachine.FACING) != newState.get(BlockGenericMachine.FACING))) {
	    GenericTile generic = (GenericTile) tile;
	    if (generic.hasComponent(ComponentType.Inventory)) {
		InventoryHelper.dropInventoryItems(worldIn, pos, generic.getComponent(ComponentType.Inventory));
	    }
	}
	super.onReplaced(state, worldIn, pos, newState, isMoving);

    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(this));
    }

    @Override
    @Deprecated
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
	    BlockRayTraceResult hit) {
	if (!worldIn.isRemote) {
	    TileCrate tile = (TileCrate) worldIn.getTileEntity(pos);
	    if (tile != null) {
		if (player.isSneaking()) {
		    ComponentInventory inv = tile.getComponent(ComponentType.Inventory);
		    for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).resolve().get()
				.extractItem(i, inv.getInventoryStackLimit(), false);
			if (!stack.isEmpty()) {
			    ItemEntity item = new ItemEntity(worldIn, player.getPosX() + 0.5, player.getPosY() + 0.5, player.getPosZ() + 0.5, stack);
			    worldIn.addEntity(item);
			    break;
			}
		    }

		} else {
		    player.setItemStackToSlot(handIn == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND,
			    HopperTileEntity.putStackInInventoryAllSlots(player.inventory, tile.getComponent(ComponentType.Inventory),
				    player.getHeldItem(handIn), Direction.EAST));
		}
	    }
	}
	return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
	return new TileCrate();
    }
}
