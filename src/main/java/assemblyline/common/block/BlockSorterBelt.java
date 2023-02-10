package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.api.IWrenchItem;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class BlockSorterBelt extends GenericEntityBlockWaterloggable {
	private static final VoxelShape shape = Shapes.or(Shapes.box(0, 14.0 / 16.0, 0, 1, 1, 1), Shapes.box(0, 0, 0, 1, 5.0 / 16.0, 1));

	public BlockSorterBelt() {
		super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH).setValue(BlockMachine.ON, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return worldIn instanceof Level lvl && lvl.isClientSide ? Shapes.block() : shape;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!world.isClientSide && tile instanceof TileSorterBelt belt) {
			belt.onEntityCollision(entityIn, BlockEntityUtils.isLit(belt));
		}
	}

	@Override
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(BlockMachine.ON, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(BlockMachine.ON);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileSorterBelt(pos, state);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(this));
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof BlockSorterBelt)) {
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof GenericTile gen && !(state.getBlock() == newState.getBlock() && state.getValue(FACING) != newState.getValue(FACING))) {
				Containers.dropContents(worldIn, pos, gen.<ComponentInventory>getComponent(ComponentType.Inventory));
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

}
