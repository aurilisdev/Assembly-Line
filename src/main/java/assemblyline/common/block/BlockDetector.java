package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileDetector;
import electrodynamics.prefab.tile.GenericTileTicking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDetector extends BaseEntityBlock {
    private static final VoxelShape shape = Shapes.box(0, 0, 0, 1, 11.0 / 16.0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BlockDetector() {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
	return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
	return shape;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(this));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
	return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
	return blockState.getSignal(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
	BlockEntity tile = blockAccess.getBlockEntity(pos);
	if (tile instanceof TileDetector det) {
	    return det.isPowered ? 15 : 0;
	}
	return 0;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
	return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	return new TileDetector(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type) {
	return this::tick;
    }

    public <T extends BlockEntity> void tick(Level lvl, BlockPos pos, BlockState state, T t) {
	if (t instanceof GenericTileTicking tick) {
	    tick.tick();
	}
    }
}
