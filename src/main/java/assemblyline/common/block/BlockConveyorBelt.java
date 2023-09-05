package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileConveyorBelt.ConveyorType;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockConveyorBelt extends GenericEntityBlockWaterloggable {
	private static final VoxelShape shape = Shapes.box(0, 0, 0, 1, 5.0 / 16.0, 1);

	public BlockConveyorBelt() {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return shape;
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		if (player.level().getBlockEntity(pos) instanceof TileConveyorBelt belt) {
			if (belt.conveyorType.get() + 1 <= ConveyorType.values().length - 1) {
				belt.conveyorType.set(ConveyorType.values()[belt.conveyorType.get() + 1].ordinal());
			} else {
				belt.conveyorType.set(ConveyorType.values()[0].ordinal());
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(this));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(GenericEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(GenericEntityBlock.FACING);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileConveyorBelt(pos, state);
	}
}
