package assemblyline.common.packet.types.server;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import voltaic.api.codec.StreamCodec;

public class PacketFarmer {
	
	public static final StreamCodec<PacketBuffer, PacketFarmer> CODEC = new StreamCodec<PacketBuffer, PacketFarmer>() {
		
		@Override
		public void encode(PacketBuffer buf, PacketFarmer data) {
			buf.writeInt(data.num);
			StreamCodec.BLOCK_POS.encode(buf, data.pos);
		}
		
		@Override
		public PacketFarmer decode(PacketBuffer buf) {
			return new PacketFarmer(buf.readInt(), StreamCodec.BLOCK_POS.decode(buf));
		}
	}; 

	private final int num;
	private final BlockPos pos;

	public PacketFarmer(int num, BlockPos pos) {
		this.num = num;
		this.pos = pos;
	}

	public static void handle(PacketFarmer message, Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			ServerBarrierMethods.handleFarmer(ctx.getSender().level, message.pos, message.num);
		});
		ctx.setPacketHandled(true);
	}

	public static void encode(PacketFarmer pkt, PacketBuffer buf) {
		buf.writeInt(pkt.num);
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
	}

	public static PacketFarmer decode(PacketBuffer buf) {
		return new PacketFarmer(buf.readInt(), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
	}

}
