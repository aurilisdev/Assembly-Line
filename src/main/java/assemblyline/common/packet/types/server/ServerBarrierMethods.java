package assemblyline.common.packet.types.server;

import assemblyline.common.tile.TileFarmer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ServerBarrierMethods {
    public static void handleFarmer(Level level, BlockPos pos, int num) {

        ServerLevel world = (ServerLevel) level;

        if (world.getBlockEntity(pos) instanceof TileFarmer farmer) {
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
