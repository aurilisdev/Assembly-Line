package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.api.IWrenchItem;
import electrodynamics.api.tile.GenericTile;
import electrodynamics.api.tile.IWrenchable;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.common.block.BlockGenericMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockSorterBelt extends Block implements IWrenchable {
    private static final VoxelShape shape = VoxelShapes.or(VoxelShapes.create(0, 14.0 / 16.0, 0, 1, 1, 1),
	    VoxelShapes.create(0, 0, 0, 1, 5.0 / 16.0, 1));
    public final boolean running;

    public BlockSorterBelt(boolean running) {
	super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).notSolid());
	setDefaultState(stateContainer.getBaseState().with(BlockGenericMachine.FACING, Direction.NORTH));
	this.running = running;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	return worldIn instanceof World && ((World) worldIn).isRemote ? VoxelShapes.fullCube() : shape;
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
	return Arrays.asList(new ItemStack(DeferredRegisters.blockSorterBelt));
    }

    @Override
    @Deprecated
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
	TileEntity tile = world.getTileEntity(pos);
	if (!world.isRemote && tile instanceof TileSorterBelt) {
	    TileSorterBelt belt = (TileSorterBelt) tile;
	    belt.onEntityCollision(entityIn, running);
	}
    }

    @Override
    @Deprecated
    public void onRotate(ItemStack stack, BlockPos pos, PlayerEntity player) {
	player.world.setBlockState(pos, rotate(player.world.getBlockState(pos), Rotation.CLOCKWISE_90));
    }

    @Override
    public void onPickup(ItemStack stack, BlockPos pos, PlayerEntity player) {
	World world = player.world;
	TileEntity te = world.getTileEntity(pos);
	if (te instanceof GenericTile) {
	    InventoryHelper.dropInventoryItems(player.world, pos, ((GenericTile) te).getComponent(ComponentType.Inventory));
	}
	world.setBlockState(pos, Blocks.AIR.getDefaultState());
	world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(getSelf())));
    }

    @Override
    @Deprecated
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
	    BlockRayTraceResult hit) {
	if (worldIn.isRemote) {
	    return ActionResultType.SUCCESS;
	} else if (!(player.getHeldItem(handIn).getItem() instanceof IWrenchItem)) {
	    TileEntity tileentity = worldIn.getTileEntity(pos);
	    if (tileentity instanceof GenericTile) {
		player.openContainer((INamedContainerProvider) ((GenericTile) tileentity).getComponent(ComponentType.ContainerProvider));
	    }
	    player.addStat(Stats.INTERACT_WITH_FURNACE);
	    return ActionResultType.CONSUME;
	}
	return ActionResultType.FAIL;
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
	return state.with(BlockGenericMachine.FACING, rot.rotate(state.get(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
	return state.rotate(mirrorIn.toRotation(state.get(BlockGenericMachine.FACING)));
    }

    @Deprecated
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	if (!(newState.getBlock() instanceof BlockSorterBelt)) {
	    TileEntity tile = worldIn.getTileEntity(pos);
	    if (tile instanceof GenericTile && !(state.getBlock() == newState.getBlock()
		    && state.get(BlockGenericMachine.FACING) != newState.get(BlockGenericMachine.FACING))) {
		InventoryHelper.dropInventoryItems(worldIn, pos, ((GenericTile) tile).getComponent(ComponentType.Inventory));
	    }
	    super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
	return getDefaultState().with(BlockGenericMachine.FACING, context.getPlacementHorizontalFacing().getOpposite());
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
	return new TileSorterBelt();
    }
}
