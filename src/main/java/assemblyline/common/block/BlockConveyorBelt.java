package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileConveyorBelt.ConveyorType;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockConveyorBelt extends GenericEntityBlockWaterloggable {
	private static final VoxelShape shape = VoxelShapes.box(0, 0, 0, 1, 5.0 / 16.0, 1);

	public BlockConveyorBelt() {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().harvestTool(ToolType.PICKAXE).harvestLevel(1));
		registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, PlayerEntity player) {
		TileEntity tile = player.level.getBlockEntity(pos);
		if (tile instanceof TileConveyorBelt) {
			TileConveyorBelt belt = (TileConveyorBelt) tile;
			if (belt.conveyorType.get() + 1 <= ConveyorType.values().length - 1) {
				belt.conveyorType.set(ConveyorType.values()[belt.conveyorType.get() + 1].ordinal());
			} else {
				belt.conveyorType.set(ConveyorType.values()[0].ordinal());
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState arg0, Builder arg1) {
		return Arrays.asList(new ItemStack(this));
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(GenericEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	protected void createBlockStateDefinition(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(GenericEntityBlock.FACING);
	}

	@Override
	public TileEntity createTileEntity(BlockState arg0, IBlockReader arg1) {
		return new TileConveyorBelt();
	}
}