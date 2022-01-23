package assemblyline.client;

import assemblyline.DeferredRegisters;
import assemblyline.References;
import assemblyline.client.render.tile.RenderBlockBreaker;
import assemblyline.client.render.tile.RenderConveyorBelt;
import assemblyline.client.render.tile.RenderCrate;
import assemblyline.client.render.tile.RenderMobGrinder;
import assemblyline.client.render.tile.RenderRancher;
import assemblyline.client.screen.ScreenAutocrafter;
import assemblyline.client.screen.ScreenBlockBreaker;
import assemblyline.client.screen.ScreenBlockPlacer;
import assemblyline.client.screen.ScreenFrontHarvester;
import assemblyline.client.screen.ScreenSorterBelt;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = Bus.MOD, value = { Dist.CLIENT })
public class ClientRegister {
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
		ForgeModelBakery.addSpecialModel(MODEL_BLOCKBREAKERWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_BLOCKBREAKERBASE);
		ForgeModelBakery.addSpecialModel(MODEL_MOBGRINDERBASE);
		ForgeModelBakery.addSpecialModel(MODEL_MOBGRINDERCENTERWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_MOBGRINDERSIDEWHEEL);
		ForgeModelBakery.addSpecialModel(MODEL_RANCHER);
		ForgeModelBakery.addSpecialModel(MODEL_RANCHERLEFT);
		ForgeModelBakery.addSpecialModel(MODEL_RANCHERRIGHT);
	}

	public static final ResourceLocation MODEL_CONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbelt");
	public static final ResourceLocation MODEL_CONVEYORCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltclear");
	public static final ResourceLocation MODEL_CONVEYORRIGHTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrightclear");
	public static final ResourceLocation MODEL_CONVEYORLEFTCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltleftclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltrunning");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDCLEAR = new ResourceLocation(References.ID + ":block/conveyorbeltrunningclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDRIGHTCLEAR = new ResourceLocation(
			References.ID + ":block/conveyorbeltrunningrightclear");
	public static final ResourceLocation MODEL_CONVEYORANIMATEDLEFTCLEAR = new ResourceLocation(
			References.ID + ":block/conveyorbeltrunningleftclear");
	public static final ResourceLocation MODEL_SLOPEDCONVEYOR = new ResourceLocation(References.ID + ":block/conveyorbeltsloped");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopedrunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUP = new ResourceLocation(References.ID + ":block/conveyorbeltslopedup");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORUPANIMATED = new ResourceLocation(References.ID + ":block/conveyorbeltslopeduprunning");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWN = new ResourceLocation(References.ID + ":block/conveyorbeltslopeddown");
	public static final ResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = new ResourceLocation(
			References.ID + ":block/conveyorbeltslopeddownrunning");
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
		MenuScreens.register(DeferredRegisters.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
		MenuScreens.register(DeferredRegisters.CONTAINER_AUTOCRAFTER.get(), ScreenAutocrafter::new);
		MenuScreens.register(DeferredRegisters.CONTAINER_BLOCKPLACER.get(), ScreenBlockPlacer::new);
		MenuScreens.register(DeferredRegisters.CONTAINER_BLOCKBREAKER.get(), ScreenBlockBreaker::new);
		MenuScreens.register(DeferredRegisters.CONTAINER_HARVESTER.get(), ScreenFrontHarvester::new);
	}

	@SubscribeEvent
	public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(DeferredRegisters.TILE_CRATE.get(), RenderCrate::new);
		event.registerBlockEntityRenderer(DeferredRegisters.TILE_BELT.get(), RenderConveyorBelt::new);
		event.registerBlockEntityRenderer(DeferredRegisters.TILE_BLOCKBREAKER.get(), RenderBlockBreaker::new);
		event.registerBlockEntityRenderer(DeferredRegisters.TILE_MOBGRINDER.get(), RenderMobGrinder::new);
		event.registerBlockEntityRenderer(DeferredRegisters.TILE_RANCHER.get(), RenderRancher::new);
	}
}
