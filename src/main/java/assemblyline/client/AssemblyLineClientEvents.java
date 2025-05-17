package assemblyline.client;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerFarmerLines;
import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import voltaic.client.event.AbstractLevelStageHandler;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AssemblyLineClientEvents {

	private static final List<AbstractLevelStageHandler> LEVEL_STAGE_RENDER_HANDLERS = new ArrayList<>();

	public static void init() {
		LEVEL_STAGE_RENDER_HANDLERS.add(HandlerHarvesterLines.INSTANCE);
		LEVEL_STAGE_RENDER_HANDLERS.add(HandlerFarmerLines.INSTANCE);
	}

	@SubscribeEvent
	public static void handleRenderEvents(RenderWorldLastEvent event) {
		LEVEL_STAGE_RENDER_HANDLERS.forEach(handler -> {
			handler.render(event.getContext(), event.getMatrixStack(), event.getPartialTicks(), event.getProjectionMatrix(), event.getFinishTimeNano());
		});
	}

	@SubscribeEvent
	public static void wipeRenderHashes(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		PlayerEntity player = event.getPlayer();
		if (player != null) {
			LEVEL_STAGE_RENDER_HANDLERS.forEach(AbstractLevelStageHandler::clear);
		}
	}

}