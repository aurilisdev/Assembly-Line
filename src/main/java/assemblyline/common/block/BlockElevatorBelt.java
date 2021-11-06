package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileElevatorBelt;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraftforge.common.ToolType;

public class BlockElevatorBelt extends Block {

    public BlockElevatorBelt() {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(BlockGenericMachine.FACING, Direction.NORTH));
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockConveyorBelt));
    }

    @Override
    @Deprecated
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
	BlockEntity tile = world.getBlockEntity(pos);
	if (!world.isClientSide && tile instanceof TileConveyorBelt && entityIn instanceof ItemEntity && entityIn.tickCount > 5) {
	    TileConveyorBelt belt = (TileConveyorBelt) tile;
	    ItemEntity item = (ItemEntity) entityIn;
	    item.setItem(belt.addItemOnBelt(item.getItem()));
	}
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.setValue(BlockGenericMachine.FACING, rot.rotate(state.getValue(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.getRotation(state.getValue(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockElevatorBelt)) {
	    BlockEntity tile = worldIn.getBlockEntity(pos);
	    if (!(state.getBlock() == newState.getBlock()
		    && state.getValue(BlockGenericMachine.FACING) != newState.getValue(BlockGenericMachine.FACING)) && tile instanceof GenericTile) {
		GenericTile generic = (GenericTile) tile;
		if (generic.hasComponent(ComponentType.Inventory)) {
		    Containers.dropContents(worldIn, pos, generic.getComponent(ComponentType.Inventory));
		}
	    }
	    super.onRemove(state, worldIn, pos, newState, isMoving);
	}
    }

    @Override
    @Deprecated
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
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
	return new TileElevatorBelt();
    }
}
