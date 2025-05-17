package assemblyline.common.packet;

import java.util.HashMap;
import java.util.Optional;

import assemblyline.AssemblyLine;
import assemblyline.common.packet.types.server.PacketFarmer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

	public static HashMap<String, String> playerInformation = new HashMap<>();
	private static final String PROTOCOL_VERSION = "1";
	private static int disc = 0;
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(AssemblyLine.rl("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void init() {
		CHANNEL.registerMessage(disc++, PacketFarmer.class, PacketFarmer::encode, PacketFarmer::decode, PacketFarmer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
}
