package assemblyline.common.packet.types;

import java.util.function.Supplier;

import assemblyline.common.tile.TileFarmer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketFarmer {

	private final int num;
	private final BlockPos pos;

	public PacketFarmer(int num, BlockPos pos) {
		this.num = num;
		this.pos = pos;
	}

	public static void handle(PacketFarmer message, Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			ServerLevel world = (ServerLevel) context.get().getSender().level();
			if (world != null) {
				TileFarmer farmer = (TileFarmer) world.getBlockEntity(message.pos);
				if (farmer != null) {
					switch (message.num) {
					case 0:
						farmer.fullGrowBonemeal.set(!farmer.fullGrowBonemeal.get());
						break;
					case 1:
						farmer.refillEmpty.set(!farmer.refillEmpty.get());
						break;
					default:
						break;
					}
				}
			}
		});
	}

	public static void encode(PacketFarmer pkt, FriendlyByteBuf buf) {
		buf.writeInt(pkt.num);
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
	}

	public static PacketFarmer decode(FriendlyByteBuf buf) {
		return new PacketFarmer(buf.readInt(), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
	}

}
