package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileElevatorBelt;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;

public class BlockElevatorBelt extends GenericEntityBlockWaterloggable {

    public BlockElevatorBelt() {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockElevatorBelt));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
	BlockEntity tile = world.getBlockEntity(pos);
	if (!world.isClientSide && tile instanceof TileConveyorBelt belt && entityIn instanceof ItemEntity item && entityIn.tickCount > 5) {
	    item.setItem(belt.addItemOnBelt(item.getItem()));
	}
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockElevatorBelt)) {
	    BlockEntity tile = worldIn.getBlockEntity(pos);
	    if (!(state.getBlock() == newState.getBlock()
		    && state.getValue(GenericEntityBlock.FACING) != newState.getValue(GenericEntityBlock.FACING))
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
	return defaultBlockState().setValue(GenericEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	super.createBlockStateDefinition(builder);
	builder.add(GenericEntityBlock.FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	return new TileElevatorBelt(pos, state);
    }
}