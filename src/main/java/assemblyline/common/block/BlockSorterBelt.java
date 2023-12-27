package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockSorterBelt extends GenericEntityBlockWaterloggable {

	private static final VoxelShape shape = VoxelShapes.or(VoxelShapes.box(0, 14.0 / 16.0, 0, 1, 1, 1), VoxelShapes.box(0, 0, 0, 1, 5.0 / 16.0, 1));

	public BlockSorterBelt() {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().harvestLevel(1).harvestTool(ToolType.PICKAXE));
		registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH).setValue(BlockMachine.ON, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(BlockMachine.ON, false);
	}

	@Override
	protected void createBlockStateDefinition(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(BlockMachine.ON);
	}

	@Override
	public TileEntity createTileEntity(BlockState arg0, IBlockReader arg1) {
		return new TileSorterBelt();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(this));
	}

}
