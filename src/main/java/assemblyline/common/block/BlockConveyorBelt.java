package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileConveyorBelt;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.GenericTileTicking;
import electrodynamics.prefab.tile.IWrenchable;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockConveyorBelt extends BaseEntityBlock implements IWrenchable {
    private static final VoxelShape shape = Shapes.box(0, 0, 0, 1, 5.0 / 16.0, 1);

    public BlockConveyorBelt() {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(BlockGenericMachine.FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
	return shape;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockConveyorBelt));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
	BlockEntity tile = world.getBlockEntity(pos);
	if (!world.isClientSide && tile instanceof TileConveyorBelt belt && entityIn instanceof ItemEntity item && entityIn.tickCount > 5) {
	    item.setItem(belt.addItemOnBelt(item.getItem()));
	}
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.setValue(BlockGenericMachine.FACING, rot.rotate(state.getValue(BlockGenericMachine.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.getRotation(state.getValue(BlockGenericMachine.FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockConveyorBelt)) {
	    BlockEntity tile = worldIn.getBlockEntity(pos);
	    if (!(state.getBlock() == newState.getBlock()
		    && state.getValue(BlockGenericMachine.FACING) != newState.getValue(BlockGenericMachine.FACING))
		    && tile instanceof GenericTile generic) {
		if (generic.hasComponent(ComponentType.Inventory)) {
		    Containers.dropContents(worldIn, pos, generic.<ComponentInventory>getComponent(ComponentType.Inventory));
		}
	    }
	    super.onRemove(state, worldIn, pos, newState, isMoving);
	}
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
	return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
	return defaultBlockState().setValue(BlockGenericMachine.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	builder.add(BlockGenericMachine.FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	return new TileConveyorBelt(pos, state);
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

    @Override
    public void onRotate(ItemStack stack, BlockPos pos, Player player) {
	player.level.setBlockAndUpdate(pos, rotate(player.level.getBlockState(pos), Rotation.CLOCKWISE_90));
    }

    @Override
    public void onPickup(ItemStack stack, BlockPos pos, Player player) {
	Level world = player.level;
	BlockEntity tile = world.getBlockEntity(pos);
	if (tile instanceof GenericTile generic && generic.hasComponent(ComponentType.Inventory)) {
	    Containers.dropContents(world, pos, generic.<ComponentInventory>getComponent(ComponentType.Inventory));
	}
	world.destroyBlock(pos, true, player);
    }
}
