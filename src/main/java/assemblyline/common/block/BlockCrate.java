package assemblyline.common.block;

import assemblyline.common.tile.TileCrate;
import electrodynamics.api.tile.GenericTile;
import electrodynamics.api.tile.components.ComponentType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE));
    }

    @Deprecated
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	TileEntity tile = worldIn.getTileEntity(pos);
	if (tile instanceof GenericTile
		&& !(state.getBlock() == newState.getBlock() && state.get(BlockConveyorBelt.FACING) != newState.get(BlockConveyorBelt.FACING))) {
	    GenericTile generic = (GenericTile) tile;
	    if (generic.hasComponent(ComponentType.Inventory)) {
		InventoryHelper.dropInventoryItems(worldIn, pos, generic.getComponent(ComponentType.Inventory));
	    }
	}
	super.onReplaced(state, worldIn, pos, newState, isMoving);

    }

    @Override
    @Deprecated
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
	    BlockRayTraceResult hit) {
	if (!worldIn.isRemote) {
	    TileCrate tile = (TileCrate) worldIn.getTileEntity(pos);
	    if (tile != null) {
		player.setItemStackToSlot(handIn == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND,
			HopperTileEntity.putStackInInventoryAllSlots(player.inventory, tile.getComponent(ComponentType.Inventory),
				player.getHeldItem(handIn), Direction.EAST));
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
