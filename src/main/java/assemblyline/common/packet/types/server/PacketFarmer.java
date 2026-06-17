package assemblyline.common.packet.types.server;

import assemblyline.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketFarmer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_FARMER_PACKETID = NetworkHandler.id("packetfarmer");
    public static final Type<PacketFarmer> TYPE = new Type<>(PACKET_FARMER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketFarmer> CODEC = StreamCodec.composite(ByteBufCodecs.INT,
	    instance -> instance.num, BlockPos.STREAM_CODEC, instance -> instance.pos, PacketFarmer::new

    );

    private final int num;
    private final BlockPos pos;

    public PacketFarmer(int num, BlockPos pos) {
	this.num = num;
	this.pos = pos;
    }

    public static void handle(PacketFarmer message, IPayloadContext context) {
	ServerBarrierMethods.handleFarmer(context.player().level(), message.pos, message.num);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
	return TYPE;
    }
}
