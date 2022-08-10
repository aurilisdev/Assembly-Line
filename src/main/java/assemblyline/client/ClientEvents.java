package assemblyline.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

	public static HashMap<BlockPos, AABB> outlines = new HashMap<>();
	public static HashMap<BlockPos, Pair<List<List<Integer>>, List<AABB>>> farmerLines = new HashMap<>();

	@SubscribeEvent
	public static void renderSelectedBlocks(RenderLevelStageEvent event) {
		if (event.getStage() == Stage.AFTER_WEATHER) {
			PoseStack matrix = event.getPoseStack();
			MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
			VertexConsumer builder = buffer.getBuffer(RenderType.LINES);
			Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			for (Entry<BlockPos, AABB> en : outlines.entrySet()) {
				AABB box = en.getValue().deflate(0.001);
				matrix.pushPose();
				matrix.translate(-camera.x, -camera.y, -camera.z);
				LevelRenderer.renderLineBox(matrix, builder, box, 1.0F, 1.0F, 1.0F, 1.0F);
				matrix.popPose();
			}
			for (Entry<BlockPos, Pair<List<List<Integer>>, List<AABB>>> en : farmerLines.entrySet()) {
				List<List<Integer>> rgbaValues = en.getValue().getFirst();
				List<AABB> lines = en.getValue().getSecond();
				for (int i = 0; i < lines.size(); i++) {
					AABB box = lines.get(i).deflate(0.01);
					List<Integer> rgba = rgbaValues.get(i);
					matrix.pushPose();
					matrix.translate(-camera.x, -camera.y, -camera.z);
					LevelRenderer.renderLineBox(matrix, builder, box, rgba.get(1) / 255.0F, rgba.get(2) / 255.0F, rgba.get(3) / 255.0F, rgba.get(0) / 255.0F);
					matrix.popPose();
				}
			}
			buffer.endBatch(RenderType.LINES);
		}
	}

}
