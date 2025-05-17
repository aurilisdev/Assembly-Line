package assemblyline.common.tile.belt;

import assemblyline.common.tile.belt.utils.ConveyorBeltProperties;
import assemblyline.common.tile.belt.utils.ConveyorClass;
import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.util.math.AxisAlignedBB;

public class TileConveyorBelt extends GenericTileConveyorBelt {

    public TileConveyorBelt() {
        super(AssemblyLineTiles.TILE_BELT.get(), ConveyorBeltProperties.builder(ConveyorClass.REGULAR));
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
       	return super.getRenderBoundingBox().inflate(3);
    }

}
