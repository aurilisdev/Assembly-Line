package assemblyline.common.block;

import assemblyline.common.tile.TileBlockPlacer;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBlockPlacer extends GenericMachineBlock {

    public BlockBlockPlacer() {
	super(TileBlockPlacer::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
	return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
