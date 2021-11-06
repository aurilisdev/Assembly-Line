package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.api.IWrenchItem;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.IWrenchable;
import electrodynamics.prefab.tile.components.ComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolType;

public class BlockSorterBelt extends Block implements IWrenchable {
    private static final VoxelShape shape = Shapes.or(Shapes.box(0, 14.0 / 16.0, 0, 1, 1, 1), Shapes.box(0, 0, 0, 1, 5.0 / 16.0, 1));
    public final boolean running;

    public BlockSorterBelt(boolean running) {
	super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).noOcclusion());
	registerDefaultState(stateDefinition.any().setValue(BlockGenericMachine.FACING, Direction.NORTH));
	this.running = running;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
	return worldIn instanceof Level && ((Level) worldIn).isClientSide ? Shapes.block() : shape;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockSorterBelt));
    }

    @Override
    @Deprecated
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
	BlockEntity tile = world.getBlockEntity(pos);
	if (!world.isClientSide && tile instanceof TileSorterBelt) {
	    TileSorterBelt belt = (TileSorterBelt) tile;
	    belt.onEntityCollision(entityIn, running);
	}
    }

    @Override
    @Deprecated
    public void onRotate(ItemStack stack, BlockPos pos, Player player) {
	player.level.setBlockAndUpdate(pos, rotate(player.level.getBlockState(pos), Rotation.CLOCKWISE_90));
    }

    @Override
    public void onPickup(ItemStack stack, BlockPos pos, Player player) {
	Level world = player.level;
	BlockEntity te = world.getBlockEntity(pos);
	if (te instanceof GenericTile) {
	    Containers.dropContents(player.level, pos, ((GenericTile) te).getComponent(ComponentType.Inventory));
	}
	world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
	world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(asBlock())));
    }

    @Override
    @Deprecated
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
	if (worldIn.isClientSide) {
	    return InteractionResult.SUCCESS;
	} else if (!(player.getItemInHand(handIn).getItem() instanceof IWrenchItem)) {
	    BlockEntity tileentity = worldIn.getBlockEntity(pos);
	    if (tileentity instanceof GenericTile) {
		player.openMenu((MenuProvider) ((GenericTile) tileentity).getComponent(ComponentType.ContainerProvider));
	    }
	    player.awardStat(Stats.INTERACT_WITH_FURNACE);
	    return InteractionResult.CONSUME;
	}
	return InteractionResult.FAIL;
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
	if (!(newState.getBlock() instanceof BlockSorterBelt)) {
	    BlockEntity tile = worldIn.getBlockEntity(pos);
	    if (tile instanceof GenericTile && !(state.getBlock() == newState.getBlock()
		    && state.getValue(BlockGenericMachine.FACING) != newState.getValue(BlockGenericMachine.FACING))) {
		Containers.dropContents(worldIn, pos, ((GenericTile) tile).getComponent(ComponentType.Inventory));
	    }
	    super.onRemove(state, worldIn, pos, newState, isMoving);
	}
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
	return new TileSorterBelt();
    }
}
