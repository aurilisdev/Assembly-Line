package assemblyline.common.packet;

import java.util.HashMap;
import java.util.Optional;

import assemblyline.References;
import assemblyline.common.packet.types.PacketFarmer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {

	public static HashMap<String, String> playerInformation = new HashMap<>();
	private static final String PROTOCOL_VERSION = "1";
	private static int disc = 0;
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(References.ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void init() {
		CHANNEL.registerMessage(disc++, PacketFarmer.class, PacketFarmer::encode, PacketFarmer::decode, PacketFarmer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
}
