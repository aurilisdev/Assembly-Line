package assemblyline.common.block;

import assemblyline.common.tile.TileBlockPlacer;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockBlockPlacer extends GenericMachineBlock {

	public BlockBlockPlacer() {
		super(TileBlockPlacer::new);
	}
}
