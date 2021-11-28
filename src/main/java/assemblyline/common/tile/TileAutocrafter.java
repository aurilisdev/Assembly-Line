package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileAutocrafter extends GenericTile {
    public boolean isPowered = false;

    public TileAutocrafter(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_AUTOCRAFTER.get(), worldPosition, blockState);
	addComponent(new ComponentDirection());
    }
}
