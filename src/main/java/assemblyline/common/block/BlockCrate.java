package assemblyline.common.block;

import assemblyline.common.tile.TileCrate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
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

public class BlockCrate extends Block {

    public BlockCrate() {
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL)
		.harvestTool(ToolType.PICKAXE));
    }

    @Deprecated
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	TileEntity tile = worldIn.getTileEntity(pos);
	if (tile instanceof IInventory) {
	    if (!(state.getBlock() == newState.getBlock()
		    && state.get(BlockConveyorBelt.FACING) != newState.get(BlockConveyorBelt.FACING))) {
		InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tile);
	    }
	}
	super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
	    Hand handIn, BlockRayTraceResult hit) {
	if (!worldIn.isRemote) {
	    TileCrate tile = (TileCrate) worldIn.getTileEntity(pos);
	    if (tile != null) {
		player.setItemStackToSlot(
			handIn == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND,
			HopperTileEntity.putStackInInventoryAllSlots(player.inventory, tile, player.getHeldItem(handIn),
				Direction.DOWN));
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
