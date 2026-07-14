package assemblyline.client;

import assemblyline.AssemblyLine;
import assemblyline.client.guidebook.ModuleAssemblyLine;
import assemblyline.client.render.tile.RenderBlockBreaker;
import assemblyline.client.render.tile.RenderConveyorBelt;
import assemblyline.client.render.tile.RenderCrate;
import assemblyline.client.render.tile.RenderFarmer;
import assemblyline.client.render.tile.RenderMobGrinder;
import assemblyline.client.render.tile.RenderRancher;
import assemblyline.client.render.tile.RenderSorterBelt;
import assemblyline.client.screen.ScreenAutocrafter;
import assemblyline.client.screen.ScreenBlockBreaker;
import assemblyline.client.screen.ScreenBlockPlacer;
import assemblyline.client.screen.ScreenFarmer;
import assemblyline.client.screen.ScreenMobGrinder;
import assemblyline.client.screen.ScreenRancher;
import assemblyline.client.screen.ScreenSorterBelt;
import assemblyline.registers.AssemblyLineMenuTypes;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import voltaic.client.guidebook.ScreenGuidebook;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class AssemblyLineClientRegister {

	public static final ResourceLocation MODEL_CONVEYOR = AssemblyLine.rl("block/conveyorbelt");
	public static final ResourceLocation MODEL_CONVEYORCLEAR = AssemblyLine.rl("block/conveyorbeltclear");
	public static final ResourceLocation MODEL_CONVEYORRIGHTCLEAR = AssemblyLine.rl("block/conveyorbeltrightclear");
	public static final ResourceLocation MODEL_CONVEYORLEFTCLEAR = AssemblyLine.rl("block/conveyorbeltleftclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATED = AssemblyLine.rl("block/conveyorbeltrunning");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDCLEAR = AssemblyLine.rl("block/conveyorbeltrunningclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDRIGHTCLEAR = AssemblyLine.rl("block/conveyorbeltrunningrightclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDLEFTCLEAR = AssemblyLine.rl("block/conveyorbeltrunningleftclear");
	public static final ResourceLocation MODEL_SLOPEDCONVEYOR = AssemblyLine.rl("block/conveyorbeltsloped");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORANIMATED = AssemblyLine.rl("block/conveyorbeltslopedrunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUP = AssemblyLine.rl("block/conveyorbeltslopedup");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUPANIMATED = AssemblyLine.rl("block/conveyorbeltslopeduprunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWN = AssemblyLine.rl("block/conveyorbeltslopeddown");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = AssemblyLine.rl("block/conveyorbeltslopeddownrunning");
	public static final ResourceLocation MODEL_MANIPULATORINPUT = AssemblyLine.rl("block/manipulatorinput");
	public static final ResourceLocation MODEL_MANIPULATORINPUTRUNNING = AssemblyLine.rl("block/manipulatorinputrunning");
	public static final ResourceLocation MODEL_MANIPULATOROUTPUT = AssemblyLine.rl("block/manipulatoroutput");
	public static final ResourceLocation MODEL_MANIPULATOROUTPUTRUNNING = AssemblyLine.rl("block/manipulatoroutputrunning");
	public static final ResourceLocation MODEL_ELEVATOR = AssemblyLine.rl("block/elevatorbelt");
	public static final ResourceLocation MODEL_ELEVATORRUNNING = AssemblyLine.rl("block/elevatorbeltrunning");
	public static final ResourceLocation MODEL_ELEVATORBOTTOM = AssemblyLine.rl("block/elevatorbeltbottom");
	public static final ResourceLocation MODEL_ELEVATORBOTTOMRUNNING = AssemblyLine.rl("block/elevatorbeltbottomrunning");
	public static final ResourceLocation MODEL_MANIPULATOR = AssemblyLine.rl("block/manipulator");

	public static final ResourceLocation MODEL_SORTERBELT = AssemblyLine.rl("block/sorterbelt");
	public static final ResourceLocation MODEL_SORTERBELT_RUNNING = AssemblyLine.rl("block/sorterbeltrunning");

	public static final ResourceLocation MODEL_BLOCKBREAKERWHEEL = AssemblyLine.rl("block/blockbreakerwheel");
	public static final ResourceLocation MODEL_MOBGRINDERSIDEWHEEL = AssemblyLine.rl("block/mobgrindersidewheel");
	public static final ResourceLocation MODEL_MOBGRINDERCENTERWHEEL = AssemblyLine.rl("block/mobgrindercenterwheel");
	public static final ResourceLocation MODEL_RANCHERLEFT = AssemblyLine.rl("block/rancherleft");
	public static final ResourceLocation MODEL_RANCHERRIGHT = AssemblyLine.rl("block/rancherright");

	public static void setup() {
		AssemblyLineClientEvents.init();

		ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), ScreenAutocrafter::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), ScreenBlockPlacer::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), ScreenBlockBreaker::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_MOBGRINDER.get(), ScreenMobGrinder::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_RANCHER.get(), ScreenRancher::new);
        ScreenManager.register(AssemblyLineMenuTypes.CONTAINER_FARMER.get(), ScreenFarmer::new);
        
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_CRATE.get(), RenderCrate::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_BELT.get(), RenderConveyorBelt::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_BLOCKBREAKER.get(), RenderBlockBreaker::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_MOBGRINDER.get(), RenderMobGrinder::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_RANCHER.get(), RenderRancher::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_FARMER.get(), RenderFarmer::new);
        ClientRegistry.bindTileEntityRenderer(AssemblyLineTiles.TILE_SORTERBELT.get(), RenderSorterBelt::new);

		ScreenGuidebook.addGuidebookModule(new ModuleAssemblyLine());
	}
	
	@SubscribeEvent
	public static void onModelEvent(ModelRegistryEvent event) {
		ModelLoader.addSpecialModel(MODEL_CONVEYOR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORCLEAR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORRIGHTCLEAR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORLEFTCLEAR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATED);
		ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATEDCLEAR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATEDRIGHTCLEAR);
		ModelLoader.addSpecialModel(MODEL_CONVEYORANIMATEDLEFTCLEAR);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYOR);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORANIMATED);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORUP);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORUPANIMATED);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWN);
		ModelLoader.addSpecialModel(MODEL_SLOPEDCONVEYORDOWNANIMATED);
		ModelLoader.addSpecialModel(MODEL_MANIPULATORINPUT);
		ModelLoader.addSpecialModel(MODEL_MANIPULATORINPUTRUNNING);
		ModelLoader.addSpecialModel(MODEL_MANIPULATOROUTPUT);
		ModelLoader.addSpecialModel(MODEL_MANIPULATOROUTPUTRUNNING);
		ModelLoader.addSpecialModel(MODEL_ELEVATOR);
		ModelLoader.addSpecialModel(MODEL_ELEVATORRUNNING);
		ModelLoader.addSpecialModel(MODEL_ELEVATORBOTTOM);
		ModelLoader.addSpecialModel(MODEL_ELEVATORBOTTOMRUNNING);
		ModelLoader.addSpecialModel(MODEL_MANIPULATOR);
		ModelLoader.addSpecialModel(MODEL_SORTERBELT);
		ModelLoader.addSpecialModel(MODEL_SORTERBELT_RUNNING);
		ModelLoader.addSpecialModel(MODEL_BLOCKBREAKERWHEEL);
		ModelLoader.addSpecialModel(MODEL_MOBGRINDERCENTERWHEEL);
		ModelLoader.addSpecialModel(MODEL_MOBGRINDERSIDEWHEEL);
		ModelLoader.addSpecialModel(MODEL_RANCHERLEFT);
		ModelLoader.addSpecialModel(MODEL_RANCHERRIGHT);
	}

}
