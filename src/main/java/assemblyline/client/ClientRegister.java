package assemblyline.client;

import assemblyline.References;
import assemblyline.client.guidebook.ModuleAssemblyLine;
import assemblyline.client.render.tile.RenderBlockBreaker;
import assemblyline.client.render.tile.RenderConveyorBelt;
import assemblyline.client.render.tile.RenderCrate;
import assemblyline.client.render.tile.RenderFarmer;
import assemblyline.client.render.tile.RenderMobGrinder;
import assemblyline.client.render.tile.RenderRancher;
import assemblyline.client.screen.ScreenAutocrafter;
import assemblyline.client.screen.ScreenBlockBreaker;
import assemblyline.client.screen.ScreenBlockPlacer;
import assemblyline.client.screen.ScreenFarmer;
import assemblyline.client.screen.ScreenFrontHarvester;
import assemblyline.client.screen.ScreenSorterBelt;
import assemblyline.registers.AssemblyLineBlockTypes;
import assemblyline.registers.AssemblyLineMenuTypes;
import electrodynamics.client.guidebook.ScreenGuidebook;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterAdditional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = Bus.MOD, value = { Dist.CLIENT })
public class ClientRegister {
	@SubscribeEvent
	public static void onModelEvent(RegisterAdditional event) {
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
		event.register(MODEL_BLOCKBREAKERWHEEL);
		event.register(MODEL_BLOCKBREAKERBASE);
		event.register(MODEL_MOBGRINDERBASE);
		event.register(MODEL_MOBGRINDERCENTERWHEEL);
		event.register(MODEL_MOBGRINDERSIDEWHEEL);
		event.register(MODEL_RANCHER);
		event.register(MODEL_RANCHERLEFT);
		event.register(MODEL_RANCHERRIGHT);
	}

	public static final ResourceLocation MODEL_CONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbelt");
	public static final ResourceLocation MODEL_CONVEYORCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltclear");
	public static final ResourceLocation MODEL_CONVEYORRIGHTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrightclear");
	public static final ResourceLocation MODEL_CONVEYORLEFTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltleftclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltrunning");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrunningclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDRIGHTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrunningrightclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDLEFTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrunningleftclear");
	public static final ResourceLocation MODEL_SLOPEDCONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbeltsloped");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopedrunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUP = new ResourceLocation(References.ID + ":block/conveyorbeltslopedup");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUPANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopeduprunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWN = new ResourceLocation(References.ID + ":block/conveyorbeltslopeddown");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopeddownrunning");
	public static final ResourceLocation MODEL_MANIPULATORINPUT = new ResourceLocation(References.ID + ":block/manipulatorinput");
	public static final ResourceLocation MODEL_MANIPULATORINPUTRUNNING = new ResourceLocation(References.ID + ":block/manipulatorinputrunning");
	public static final ResourceLocation MODEL_MANIPULATOROUTPUT = new ResourceLocation(References.ID + ":block/manipulatoroutput");
	public static final ResourceLocation MODEL_MANIPULATOROUTPUTRUNNING = new ResourceLocation(References.ID + ":block/manipulatoroutputrunning");
	public static final ResourceLocation MODEL_ELEVATOR = new ResourceLocation(References.ID + ":block/elevatorbelt");
	public static final ResourceLocation MODEL_ELEVATORRUNNING = new ResourceLocation(References.ID + ":block/elevatorbeltrunning");
	public static final ResourceLocation MODEL_ELEVATORBOTTOM = new ResourceLocation(References.ID + ":block/elevatorbeltbottom");
	public static final ResourceLocation MODEL_ELEVATORBOTTOMRUNNING = new ResourceLocation(References.ID + ":block/elevatorbeltbottomrunning");
	public static final ResourceLocation MODEL_MANIPULATOR = new ResourceLocation(References.ID + ":block/manipulator");
	public static final ResourceLocation MODEL_BLOCKBREAKERWHEEL = new ResourceLocation(References.ID + ":block/blockbreakerwheel");
	public static final ResourceLocation MODEL_BLOCKBREAKERBASE = new ResourceLocation(References.ID + ":block/blockbreakerbase");
	public static final ResourceLocation MODEL_MOBGRINDERBASE = new ResourceLocation(References.ID + ":block/mobgrinderbase");
	public static final ResourceLocation MODEL_MOBGRINDERSIDEWHEEL = new ResourceLocation(References.ID + ":block/mobgrindersidewheel");
	public static final ResourceLocation MODEL_MOBGRINDERCENTERWHEEL = new ResourceLocation(References.ID + ":block/mobgrindercenterwheel");
	public static final ResourceLocation MODEL_RANCHER = new ResourceLocation(References.ID + ":block/rancher");
	public static final ResourceLocation MODEL_RANCHERLEFT = new ResourceLocation(References.ID + ":block/rancherleft");
	public static final ResourceLocation MODEL_RANCHERRIGHT = new ResourceLocation(References.ID + ":block/rancherright");

	public static void setup() {
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), ScreenAutocrafter::new);
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), ScreenBlockPlacer::new);
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), ScreenBlockBreaker::new);
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_HARVESTER.get(), ScreenFrontHarvester::new);
		MenuScreens.register(AssemblyLineMenuTypes.CONTAINER_FARMER.get(), ScreenFarmer::new);

		ScreenGuidebook.addGuidebookModule(new ModuleAssemblyLine());
	}

	@SubscribeEvent
	public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_CRATE.get(), RenderCrate::new);
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_BELT.get(), RenderConveyorBelt::new);
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_BLOCKBREAKER.get(), RenderBlockBreaker::new);
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_MOBGRINDER.get(), RenderMobGrinder::new);
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_RANCHER.get(), RenderRancher::new);
		event.registerBlockEntityRenderer(AssemblyLineBlockTypes.TILE_FARMER.get(), RenderFarmer::new);
	}
}
