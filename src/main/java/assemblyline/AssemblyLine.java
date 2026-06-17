package assemblyline;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.packet.NetworkHandler;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.registers.UnifiedAssemblyLineRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import voltaic.prefab.configuration.ConfigurationHandler;

@Mod(AssemblyLine.ID)
@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public class AssemblyLine {

    public static final String ID = "assemblyline";
    public static final String NAME = "Assembly Line";

    public AssemblyLine() {
	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
	ConfigurationHandler.registerConfig(AssemblyLineConstants.class);
	AssemblyLineVoxelShapes.init();
	UnifiedAssemblyLineRegister.register(bus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
	NetworkHandler.init();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
	event.enqueueWork(() -> {
	    AssemblyLineClientRegister.setup();
	});
    }

    public static final ResourceLocation rl(String path) {
	return new ResourceLocation(AssemblyLine.ID, path);
    }

}
