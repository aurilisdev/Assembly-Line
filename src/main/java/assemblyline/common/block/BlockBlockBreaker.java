package assemblyline.common.block;

import assemblyline.common.tile.TileBlockBreaker;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBlockBreaker extends GenericMachineBlock {

	public BlockBlockBreaker() {
		super(TileBlockBreaker::new);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
