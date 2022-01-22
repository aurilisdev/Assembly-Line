package assemblyline.common.block;

import assemblyline.common.tile.TileMobGrinder;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMobGrinder extends GenericMachineBlock {

	public BlockMobGrinder() {
		super(TileMobGrinder::new);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

}
