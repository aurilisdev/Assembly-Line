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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
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
    public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_CRATE.get(), RenderCrate::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_BELT.get(), RenderConveyorBelt::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_BLOCKBREAKER.get(), RenderBlockBreaker::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_MOBGRINDER.get(), RenderMobGrinder::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_RANCHER.get(), RenderRancher::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_FARMER.get(), RenderFarmer::new);
        event.registerBlockEntityRenderer(AssemblyLineTiles.TILE_SORTERBELT.get(), RenderSorterBelt::new);
    }

	@SubscribeEvent
	public static void onModelEvent(ModelEvent.RegisterAdditional event) {
		event.register(MODEL_CONVEYOR);
		event.register(MODEL_CONVEYORCLEAR);
		event.register(MODEL_CONVEYORRIGHTCLEAR);
		event.register(MODEL_CONVEYORLEFTCLEAR);
		event.register(MODEL_CONVEYORANIMATED);
		event.register(MODEL_CONVEYORANIMATEDCLEAR);
		event.register(MODEL_CONVEYORANIMATEDRIGHTCLEAR);
		event.register(MODEL_CONVEYORANIMATEDLEFTCLEAR);
		event.register(MODEL_SLOPEDCONVEYOR);
		event.register(MODEL_SLOPEDCONVEYORANIMATED);
		event.register(MODEL_SLOPEDCONVEYORUP);
		event.register(MODEL_SLOPEDCONVEYORUPANIMATED);
		event.register(MODEL_SLOPEDCONVEYORDOWN);
		event.register(MODEL_SLOPEDCONVEYORDOWNANIMATED);
		event.register(MODEL_MANIPULATORINPUT);
		event.register(MODEL_MANIPULATORINPUTRUNNING);
		event.register(MODEL_MANIPULATOROUTPUT);
		event.register(MODEL_MANIPULATOROUTPUTRUNNING);
		event.register(MODEL_ELEVATOR);
		event.register(MODEL_ELEVATORRUNNING);
		event.register(MODEL_ELEVATORBOTTOM);
		event.register(MODEL_ELEVATORBOTTOMRUNNING);
		event.register(MODEL_MANIPULATOR);
		event.register(MODEL_SORTERBELT);
		event.register(MODEL_SORTERBELT_RUNNING);
		event.register(MODEL_BLOCKBREAKERWHEEL);
		event.register(MODEL_MOBGRINDERCENTERWHEEL);
		event.register(MODEL_MOBGRINDERSIDEWHEEL);
		event.register(MODEL_RANCHERLEFT);
		event.register(MODEL_RANCHERRIGHT);
	}
}
