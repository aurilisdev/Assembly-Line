package assemblyline.client;

import java.util.HashMap;
import java.util.Map.Entry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

	public static HashMap<BlockPos, AABB> outlines = new HashMap<>();

	@SubscribeEvent
	public static void renderSelectedBlocks(RenderLevelLastEvent event) {
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
		buffer.endBatch(RenderType.LINES);
	}

}
