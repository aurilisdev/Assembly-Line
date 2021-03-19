package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileManipulator;
import electrodynamics.common.block.BlockGenericMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

public class BlockManipulator extends Block {
    private static final VoxelShape shape = VoxelShapes.create(0, 0, 0, 1, 11.0 / 16.0, 1);

    public final boolean input;
    public final boolean running;

    public BlockManipulator(boolean input, boolean running) {
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).notSolid());
	setDefaultState(stateContainer.getBaseState().with(BlockGenericMachine.FACING, Direction.NORTH));
	this.input = input;
	this.running = running;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	return shape;
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.with(BlockGenericMachine.FACING, rot.rotate(state.get(BlockGenericMachine.FACING)));
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockManipulatorInput));
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.toRotation(state.get(BlockGenericMachine.FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
	return getDefaultState().with(BlockGenericMachine.FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    @Deprecated
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockManipulator)) {
	    super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
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
	return new TileManipulator();
    }
}
