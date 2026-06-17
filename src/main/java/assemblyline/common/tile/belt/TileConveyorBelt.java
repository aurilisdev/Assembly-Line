package assemblyline.common.tile.belt;

import assemblyline.common.tile.belt.utils.ConveyorBeltProperties;
import assemblyline.common.tile.belt.utils.ConveyorClass;
import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileConveyorBelt extends GenericTileConveyorBelt {

    public TileConveyorBelt(BlockPos worldPosition, BlockState blockState) {
	super(AssemblyLineTiles.TILE_BELT.get(), worldPosition, blockState,
		ConveyorBeltProperties.builder(ConveyorClass.REGULAR));
    }

}
