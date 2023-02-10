package assemblyline.common.block;

import assemblyline.common.tile.TileBlockBreaker;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockBlockBreaker extends GenericMachineBlock {

	public BlockBlockBreaker() {
		super(TileBlockBreaker::new);

	}
}
