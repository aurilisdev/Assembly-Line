package assemblyline.common.block;

import assemblyline.common.tile.TileRancher;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockRancher extends GenericMachineBlock {

	public BlockRancher() {
		super(TileRancher::new);
	}

}
