package assemblyline.client;

import assemblyline.DeferredRegisters;
import assemblyline.References;
import assemblyline.client.render.tile.RenderConveyorBelt;
import assemblyline.client.render.tile.RenderCrate;
import assemblyline.client.screen.ScreenSorterBelt;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = Bus.MOD)
public class ClientRegister {
    @SubscribeEvent
    public static void onModelEvent(ModelRegistryEvent event) {
	ModelLoader.addSpecialModel(MODEL_CONVEYOR);
	ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATED);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYOR);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORANIMATED);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWN);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWNANIMATED);
	ModelLoader.addSpecialModel(MODEL_MANIPULATORINPUT);
	ModelLoader.addSpecialModel(MODEL_MANIPULATORINPUTRUNNING);
	ModelLoader.addSpecialModel(MODEL_MANIPULATOROUTPUT);
	ModelLoader.addSpecialModel(MODEL_MANIPULATOROUTPUTRUNNING);
    }

    public static final ResourceLocation MODEL_CONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbelt");
    public static final ResourceLocation MODEL_CONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltrunning");
    public static final ResourceLocation MODEL_SLOPEDCONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbeltsloped");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopedrunning");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWN = new ResourceLocation(References.ID + ":block/conveyorbeltslopeddown");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = new ResourceLocation(
	    References.ID + ":block/conveyorbeltslopeddownrunning");
    public static final ResourceLocation MODEL_MANIPULATORINPUT = new ResourceLocation(References.ID + ":block/manipulatorinput");
    public static final ResourceLocation MODEL_MANIPULATORINPUTRUNNING = new ResourceLocation(References.ID + ":block/manipulatorinputrunning");
    public static final ResourceLocation MODEL_MANIPULATOROUTPUT = new ResourceLocation(References.ID + ":block/manipulatoroutput");
    public static final ResourceLocation MODEL_MANIPULATOROUTPUTRUNNING = new ResourceLocation(References.ID + ":block/manipulatoroutputrunning");

    public static void setup() {
	ScreenManager.registerFactory(DeferredRegisters.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
	ClientRegistry.bindTileEntityRenderer(DeferredRegisters.TILE_CRATE.get(), RenderCrate::new);
	ClientRegistry.bindTileEntityRenderer(DeferredRegisters.TILE_BELT.get(), RenderConveyorBelt::new);
    }
}
