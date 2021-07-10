package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileConveyorBelt;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockConveyorBelt extends Block {
    private static final VoxelShape shape = VoxelShapes.create(0, 0, 0, 1, 5.0 / 16.0, 1);

    public BlockConveyorBelt() {
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).notSolid());
	setDefaultState(stateContainer.getBaseState().with(BlockGenericMachine.FACING, Direction.NORTH));
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	return shape;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockConveyorBelt));
    }

    @Override
    @Deprecated
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
	TileEntity tile = world.getTileEntity(pos);
	if (!world.isRemote && tile instanceof TileConveyorBelt && entityIn instanceof ItemEntity && entityIn.ticksExisted > 5) {
	    TileConveyorBelt belt = (TileConveyorBelt) tile;
	    ItemEntity item = (ItemEntity) entityIn;
	    item.setItem(belt.addItemOnBelt(item.getItem()));
	}
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.with(BlockGenericMachine.FACING, rot.rotate(state.get(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.toRotation(state.get(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockConveyorBelt)) {
	    TileEntity tile = worldIn.getTileEntity(pos);
	    if (!(state.getBlock() == newState.getBlock() && state.get(BlockGenericMachine.FACING) != newState.get(BlockGenericMachine.FACING))
		    && tile instanceof GenericTile) {
		GenericTile generic = (GenericTile) tile;
		if (generic.hasComponent(ComponentType.Inventory)) {
		    InventoryHelper.dropInventoryItems(worldIn, pos, generic.getComponent(ComponentType.Inventory));
		}
	    }
	    super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
    }

    @Override
    @Deprecated
    public BlockRenderType getRenderType(BlockState state) {
	return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
	return getDefaultState().with(BlockGenericMachine.FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	builder.add(BlockGenericMachine.FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
	return new TileConveyorBelt();
    }
}
