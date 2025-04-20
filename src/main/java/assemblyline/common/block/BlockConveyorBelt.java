package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;

import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import voltaic.common.block.states.VoltaicBlockStates;
import voltaic.common.block.voxelshapes.VoxelShapeProvider;
import voltaic.prefab.block.GenericEntityBlockWaterloggable;

public class BlockConveyorBelt extends GenericEntityBlockWaterloggable {

	public static final double MAX_Y = 5.0 / 16.0;

	private final VoxelShapeProvider shapeProvider;
	private final BlockEntityType.BlockEntitySupplier<?> supplier;

	public BlockConveyorBelt(VoxelShapeProvider shapeProvider, BlockEntityType.BlockEntitySupplier<?> supplier) {
		super(Blocks.IRON_BLOCK.properties().strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(VoltaicBlockStates.FACING, Direction.NORTH));
		this.shapeProvider = shapeProvider;
		this.supplier = supplier;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

		Direction dir = null;
		if (state.hasProperty(VoltaicBlockStates.FACING)) {
			dir = state.getValue(VoltaicBlockStates.FACING);
		}

		return this.shapeProvider.getShape(dir);

	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		if (player.level().getBlockEntity(pos) instanceof GenericTileConveyorBelt belt) {
			belt.cycleConveyorType();
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(this));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(VoltaicBlockStates.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(VoltaicBlockStates.FACING);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return supplier.create(pos, state);
	}
}
