package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileConveyorBelt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.DirectionProperty;
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
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public final boolean running;

	public BlockConveyorBelt(boolean running) {
		super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE).notSolid());
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
		this.running = running;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(DeferredRegisters.blockConveyorbelt));
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote) {
			if (tile instanceof TileConveyorBelt) {
				TileConveyorBelt belt = (TileConveyorBelt) tile;
				belt.onEntityCollision(entityIn, running);
			}
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Deprecated
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Deprecated
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof BlockConveyorBelt)) {
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos().offset(context.getPlacementHorizontalFacing());
		Block blockAtNext = context.getWorld().getBlockState(pos).getBlock();
		BlockState returner = getDefaultState();
		if (!(blockAtNext instanceof BlockConveyorBelt || blockAtNext instanceof BlockManipulator
				|| blockAtNext instanceof BlockSorterBelt)) {
			pos = pos.offset(Direction.UP);
			blockAtNext = context.getWorld().getBlockState(pos).getBlock();
			if (blockAtNext instanceof BlockConveyorBelt || blockAtNext instanceof BlockManipulator
					|| blockAtNext instanceof BlockSorterBelt) {
				if (running) {
					returner = DeferredRegisters.blockSlantedConveyorbeltRunning.getDefaultState();
				} else {
					returner = DeferredRegisters.blockSlantedConveyorbelt.getDefaultState();
				}
			}
		}
		return returner.with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
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
