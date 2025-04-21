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
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import voltaic.client.guidebook.ScreenGuidebook;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class AssemblyLineClientRegister {

    public static final ModelResourceLocation MODEL_CONVEYOR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbelt"));
    public static final ModelResourceLocation MODEL_CONVEYORCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltclear"));
    public static final ModelResourceLocation MODEL_CONVEYORRIGHTCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltrightclear"));
    public static final ModelResourceLocation MODEL_CONVEYORLEFTCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltleftclear"));
    public static final ModelResourceLocation MODEL_CONVEYORANIMATED = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltrunning"));
    public static final ModelResourceLocation MODEL_CONVEYORANIMATEDCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltrunningclear"));
    public static final ModelResourceLocation MODEL_CONVEYORANIMATEDRIGHTCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltrunningrightclear"));
    public static final ModelResourceLocation MODEL_CONVEYORANIMATEDLEFTCLEAR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltrunningleftclear"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYOR = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltsloped"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYORANIMATED = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltslopedrunning"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYORUP = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltslopedup"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYORUPANIMATED = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltslopeduprunning"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYORDOWN = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltslopeddown"));
    public static final ModelResourceLocation MODEL_SLOPEDCONVEYORDOWNANIMATED = ModelResourceLocation.standalone(AssemblyLine.rl("block/conveyorbeltslopeddownrunning"));
    public static final ModelResourceLocation MODEL_MANIPULATORINPUT = ModelResourceLocation.standalone(AssemblyLine.rl("block/manipulatorinput"));
    public static final ModelResourceLocation MODEL_MANIPULATORINPUTRUNNING = ModelResourceLocation.standalone(AssemblyLine.rl("block/manipulatorinputrunning"));
    public static final ModelResourceLocation MODEL_MANIPULATOROUTPUT = ModelResourceLocation.standalone(AssemblyLine.rl("block/manipulatoroutput"));
    public static final ModelResourceLocation MODEL_MANIPULATOROUTPUTRUNNING = ModelResourceLocation.standalone(AssemblyLine.rl("block/manipulatoroutputrunning"));
    public static final ModelResourceLocation MODEL_ELEVATOR = ModelResourceLocation.standalone(AssemblyLine.rl("block/elevatorbelt"));
    public static final ModelResourceLocation MODEL_ELEVATORRUNNING = ModelResourceLocation.standalone(AssemblyLine.rl("block/elevatorbeltrunning"));
    public static final ModelResourceLocation MODEL_ELEVATORBOTTOM = ModelResourceLocation.standalone(AssemblyLine.rl("block/elevatorbeltbottom"));
    public static final ModelResourceLocation MODEL_ELEVATORBOTTOMRUNNING = ModelResourceLocation.standalone(AssemblyLine.rl("block/elevatorbeltbottomrunning"));
    public static final ModelResourceLocation MODEL_MANIPULATOR = ModelResourceLocation.standalone(AssemblyLine.rl("block/manipulator"));


    public static final ModelResourceLocation MODEL_SORTERBELT = ModelResourceLocation.standalone(AssemblyLine.rl("block/sorterbelt"));
    public static final ModelResourceLocation MODEL_SORTERBELT_RUNNING = ModelResourceLocation.standalone(AssemblyLine.rl("block/sorterbeltrunning"));

    public static final ModelResourceLocation MODEL_BLOCKBREAKERWHEEL = ModelResourceLocation.standalone(AssemblyLine.rl("block/blockbreakerwheel"));
    public static final ModelResourceLocation MODEL_MOBGRINDERSIDEWHEEL = ModelResourceLocation.standalone(AssemblyLine.rl("block/mobgrindersidewheel"));
    public static final ModelResourceLocation MODEL_MOBGRINDERCENTERWHEEL = ModelResourceLocation.standalone(AssemblyLine.rl("block/mobgrindercenterwheel"));
    public static final ModelResourceLocation MODEL_RANCHERLEFT = ModelResourceLocation.standalone(AssemblyLine.rl("block/rancherleft"));
    public static final ModelResourceLocation MODEL_RANCHERRIGHT = ModelResourceLocation.standalone(AssemblyLine.rl("block/rancherright"));

    public static void setup() {
        AssemblyLineClientEvents.init();

        ScreenGuidebook.addGuidebookModule(new ModuleAssemblyLine());
    }

    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event) {
        event.register(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), ScreenAutocrafter::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), ScreenBlockPlacer::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), ScreenBlockBreaker::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_MOBGRINDER.get(), ScreenMobGrinder::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_RANCHER.get(), ScreenRancher::new);
        event.register(AssemblyLineMenuTypes.CONTAINER_FARMER.get(), ScreenFarmer::new);
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
        event.register(MODEL_SORTERBELT_RUNNING

        );
        event.register(MODEL_BLOCKBREAKERWHEEL);
        event.register(MODEL_MOBGRINDERCENTERWHEEL);
        event.register(MODEL_MOBGRINDERSIDEWHEEL);
        event.register(MODEL_RANCHERLEFT);
        event.register(MODEL_RANCHERRIGHT);
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
