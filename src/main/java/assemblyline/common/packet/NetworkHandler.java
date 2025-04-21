package assemblyline.common.packet;

import assemblyline.AssemblyLine;
import assemblyline.common.packet.types.server.PacketFarmer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

	private static final String PROTOCOL_VERSION = "1";

	@SubscribeEvent
	public static void registerPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registry = event.registrar(AssemblyLine.ID).versioned(PROTOCOL_VERSION).optional();

		// SERVER
		registry.playToServer(PacketFarmer.TYPE, PacketFarmer.CODEC, PacketFarmer::handle);

	}

	public static ResourceLocation id(String name) {
		return AssemblyLine.rl(name);
	}
}
