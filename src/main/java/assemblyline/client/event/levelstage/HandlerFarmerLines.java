package assemblyline.client.event.levelstage;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import voltaic.client.event.AbstractLevelStageHandler;
import voltaic.prefab.utilities.math.Color;

public class HandlerFarmerLines extends AbstractLevelStageHandler {

	public static final HandlerFarmerLines INSTANCE = new HandlerFarmerLines();

	private final HashMap<BlockPos, Pair<Color[], List<AxisAlignedBB>>> farmerLines = new HashMap<>();

	@Override
	public void render(WorldRenderer context, MatrixStack stack, float partialTicks, Matrix4f projectionMatrix, long finishTimeNano) {

		stack.pushPose();

		Minecraft minecraft = Minecraft.getInstance();
		IRenderTypeBuffer.Impl buffer = minecraft.renderBuffers().bufferSource();
		IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);
		Vector3d camPos = minecraft.gameRenderer.getMainCamera().getPosition();

		stack.translate(-camPos.x, -camPos.y, -camPos.z);

		for (Entry<BlockPos, Pair<Color[], List<AxisAlignedBB>>> en : farmerLines.entrySet()) {
			Color[] rgbaValues = en.getValue().getFirst();
			List<AxisAlignedBB> lines = en.getValue().getSecond();
			for (int i = 0; i < lines.size(); i++) {
				AxisAlignedBB box = lines.get(i).deflate(0.01);
				float[] rgba = rgbaValues[i].colorFloatArr();

				WorldRenderer.renderLineBox(stack, builder, box, rgba[0], rgba[1], rgba[2], rgba[3]);

			}
		}

		stack.popPose();

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

	public static void addRenderData(BlockPos pos, Pair<Color[], List<AxisAlignedBB>> data) {
		INSTANCE.farmerLines.put(pos, data);
	}

}
