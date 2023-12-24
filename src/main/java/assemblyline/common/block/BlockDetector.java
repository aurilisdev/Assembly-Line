package assemblyline.common.block;

import assemblyline.common.tile.TileDetector;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockDetector extends GenericEntityBlockWaterloggable {

	public static final DirectionProperty FACING = GenericEntityBlock.FACING;

	public BlockDetector() {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().harvestTool(ToolType.PICKAXE).harvestLevel(1));
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	protected void createBlockStateDefinition(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState arg0, IBlockReader arg1) {
		return new TileDetector();
	}

}
