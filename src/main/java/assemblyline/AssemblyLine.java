package assemblyline;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.registers.UnifiedAssemblyLineRegister;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(AssemblyLine.ID)
@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public final class AssemblyLine {

	public static final String ID = "assemblyline";
	public static final String NAME = "Assembly Line";

	public AssemblyLine(IEventBus bus, ModContainer container) {
	    	AssemblyLineConfig.INSTANCE = new AssemblyLineConfig();
		container.registerConfig(ModConfig.Type.COMMON, AssemblyLineConfig.INSTANCE.SPEC);
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

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
