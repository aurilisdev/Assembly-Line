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
import assemblyline.registers.AssemblyLineTiles;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), ScreenAutocrafter::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), ScreenBlockPlacer::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), ScreenBlockBreaker::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_MOBGRINDER.get(), ScreenMobGrinder::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_RANCHER.get(), ScreenRancher::new);
        MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_FARMER.get(), ScreenFarmer::new);

		ScreenGuidebook.addGuidebookModule(new ModuleAssemblyLine());
	}
	
	@SubscribeEvent
	public static void onModelEvent(ModelRegistryEvent event) {
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYOR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORRIGHTCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORLEFTCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORANIMATED);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORANIMATEDCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORANIMATEDRIGHTCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_CONVEYORANIMATEDLEFTCLEAR);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYOR);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYORANIMATED);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYORUP);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYORUPANIMATED);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYORDOWN);
		ForgeModelBakery.addSpecialModel(MODEL_SLOPEDCONVEYORDOWNANIMATED);
		ForgeModelBakery.addSpecialModel(MODEL_MANIPULATORINPUT);
		ForgeModelBakery.addSpecialModel(MODEL_MANIPULATORINPUTRUNNING);
		ForgeModelBakery.addSpecialModel(MODEL_MANIPULATOROUTPUT);
		ForgeModelBakery.addSpecialModel(MODEL_MANIPULATOROUTPUTRUNNING);
		ForgeModelBakery.addSpecialModel(MODEL_ELEVATOR);
		ForgeModelBakery.addSpecialModel(MODEL_ELEVATORRUNNING);
		ForgeModelBakery.addSpecialModel(MODEL_ELEVATORBOTTOM);
		ForgeModelBakery.addSpecialModel(MODEL_ELEVATORBOTTOMRUNNING);
		ForgeModelBakery.addSpecialModel(MODEL_MANIPULATOR);
		ForgeModelBakery.addSpecialModel(MODEL_SORTERBELT);
		ForgeModelBakery.addSpecialModel(MODEL_SORTERBELT_RUNNING);
		ForgeModelBakery.addSpecialModel(MODEL_BLOCKBREAKERWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_MOBGRINDERCENTERWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_MOBGRINDERSIDEWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_RANCHERLEFT);
		ForgeModelBakery.addSpecialModel(MODEL_RANCHERRIGHT);
	}

	@SubscribeEvent
	public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_CRATE.get(), RenderCrate::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_BELT.get(), RenderConveyorBelt::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_BLOCKBREAKER.get(), RenderBlockBreaker::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_MOBGRINDER.get(), RenderMobGrinder::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_RANCHER.get(), RenderRancher::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_FARMER.get(), RenderFarmer::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_SORTERBELT.get(), RenderSorterBelt::new);
	}
}
