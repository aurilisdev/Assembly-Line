package assemblyline.common.packet.types.server;

import assemblyline.common.tile.TileFarmer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ServerBarrierMethods {
    public static void handleFarmer(World level, BlockPos pos, int num) {

        ServerWorld world = (ServerWorld) level;

        TileEntity tileentity = world.getBlockEntity(pos);
        
        if (tileentity instanceof TileFarmer) {
        	TileFarmer farmer = (TileFarmer) tileentity;
            switch (num) {
                case 0:
                    farmer.fullGrowBonemeal.setValue(!farmer.fullGrowBonemeal.getValue());
                    break;
                case 1:
                    farmer.refillEmpty.setValue(!farmer.refillEmpty.getValue());
                    break;
                default:
                    break;
            }
        }
    }
}
