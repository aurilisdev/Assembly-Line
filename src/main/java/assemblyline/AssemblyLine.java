package assemblyline;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.registers.UnifiedAssemblyLineRegister;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import voltaic.prefab.configuration.ConfigurationHandler;

@Mod(AssemblyLine.ID)
@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public class AssemblyLine {

	public static final String ID = "assemblyline";
	public static final String NAME = "Assembly Line";

	public AssemblyLine(IEventBus bus) {
		ConfigurationHandler.registerConfig(AssemblyLineConstants.class);
		AssemblyLineVoxelShapes.init();
		UnifiedAssemblyLineRegister.register(bus);
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			AssemblyLineClientRegister.setup();
		});
	}

	public static final ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(AssemblyLine.ID, path);
	}

}
