package assemblyline.common.block;

import assemblyline.common.tile.TileMobGrinder;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockMobGrinder extends GenericMachineBlock {

	public BlockMobGrinder() {
		super(TileMobGrinder::new);
	}

}
