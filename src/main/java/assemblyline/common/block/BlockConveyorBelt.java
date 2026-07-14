package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import voltaic.api.tile.TileEntitySupplier;
import voltaic.common.block.states.VoltaicBlockStates;
import voltaic.common.block.voxelshapes.VoxelShapeProvider;
import voltaic.prefab.block.GenericEntityBlockWaterloggable;

public class BlockConveyorBelt extends GenericEntityBlockWaterloggable {

	public static final double MAX_Y = 5.0 / 16.0;

	private final VoxelShapeProvider shapeProvider;
	private final TileEntitySupplier<? extends TileEntity> supplier;

	public BlockConveyorBelt(VoxelShapeProvider shapeProvider, TileEntitySupplier<? extends TileEntity> supplier) {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(VoltaicBlockStates.FACING, Direction.NORTH));
		this.shapeProvider = shapeProvider;
		this.supplier = supplier;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

		Direction dir = null;
		if (state.hasProperty(VoltaicBlockStates.FACING)) {
			dir = state.getValue(VoltaicBlockStates.FACING);
		}

		return this.shapeProvider.getShape(dir);

	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, PlayerEntity player) {
		TileEntity tileentity = player.level.getBlockEntity(pos);
		if (tileentity instanceof GenericTileConveyorBelt) {
			((GenericTileConveyorBelt) tileentity).cycleConveyorType();
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Arrays.asList(new ItemStack(this));
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(VoltaicBlockStates.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(VoltaicBlockStates.FACING);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return supplier.create();
	}

}
