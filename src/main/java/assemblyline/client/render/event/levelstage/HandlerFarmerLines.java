package assemblyline.client.render.event.levelstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import electrodynamics.client.render.event.levelstage.AbstractLevelStageHandler;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;

public class HandlerFarmerLines extends AbstractLevelStageHandler {

	public static final HandlerFarmerLines INSTANCE = new HandlerFarmerLines();

	private final HashMap<BlockPos, Pair<int[], List<AABB>>> farmerLines = new HashMap<>();

	@Override
	public void render(Camera camera, Frustum frustum, LevelRenderer renderer, PoseStack stack, Matrix4f projectionMatrix, Minecraft minecraft, int renderTick, float partialTick) {

		stack.pushPose();

		MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(RenderType.LINES);
		Vec3 camPos = camera.getPosition();

		stack.translate(-camPos.x, -camPos.y, -camPos.z);

		for (Entry<BlockPos, Pair<int[], List<AABB>>> en : farmerLines.entrySet()) {
			int[] rgbaValues = en.getValue().getFirst();
			List<AABB> lines = en.getValue().getSecond();
			for (int i = 0; i < lines.size(); i++) {
				AABB box = lines.get(i).deflate(0.01);
				float[] rgba = RenderingUtils.getColorArray(rgbaValues[i]);

				LevelRenderer.renderLineBox(stack, builder, box, rgba[0], rgba[1], rgba[2], rgba[3]);

			}
		}

		stack.popPose();

	}

	@Override
	public boolean shouldRender(Stage stage) {
		return stage == Stage.AFTER_TRIPWIRE_BLOCKS;
	}

	@Override
	public void clear() {
		farmerLines.clear();
	}

	public static boolean isBeingRendered(BlockPos pos) {
		return INSTANCE.farmerLines.containsKey(pos);
	}

	public static void remove(BlockPos pos) {
		INSTANCE.farmerLines.remove(pos);
	}

	public static void addRenderData(BlockPos pos, Pair<int[], List<AABB>> data) {
		INSTANCE.farmerLines.put(pos, data);
	}

}
