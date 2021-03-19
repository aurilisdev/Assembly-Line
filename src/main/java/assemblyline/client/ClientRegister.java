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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientRegister {
    public static final ResourceLocation MODEL_CONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbelt");
    public static final ResourceLocation MODEL_CONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltrunning");
    public static final ResourceLocation MODEL_SLOPEDCONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbeltsloped");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopedrunning");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWN = new ResourceLocation(References.ID + ":block/conveyorbeltslopeddown");
    public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = new ResourceLocation(
	    References.ID + ":block/conveyorbeltslopeddownrunning");

    public static void setup() {
	ModelLoader.addSpecialModel(MODEL_CONVEYOR);
	ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATED);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYOR);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORANIMATED);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWN);
	ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWNANIMATED);
	ScreenManager.registerFactory(DeferredRegisters.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
	ClientRegistry.bindTileEntityRenderer(DeferredRegisters.TILE_CRATE.get(), RenderCrate::new);
	ClientRegistry.bindTileEntityRenderer(DeferredRegisters.TILE_BELT.get(), RenderConveyorBelt::new);
    }
}
