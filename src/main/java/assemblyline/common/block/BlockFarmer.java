package assemblyline.common.block;

import assemblyline.common.tile.TileFarmer;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockFarmer extends GenericMachineBlock {

	public BlockFarmer() {
		super(TileFarmer::new);
	}

}
