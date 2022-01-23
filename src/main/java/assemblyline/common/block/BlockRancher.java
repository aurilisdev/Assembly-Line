package assemblyline.common.block;

import assemblyline.common.tile.TileRancher;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockRancher extends GenericMachineBlock {

	public BlockRancher() {
		super(TileRancher::new);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
