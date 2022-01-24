package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileBlockBreaker;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class RenderBlockBreaker implements BlockEntityRenderer<TileBlockBreaker> {
	public RenderBlockBreaker(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(TileBlockBreaker tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		double progress = (tileEntityIn.clientRunningTicks + (tileEntityIn.works ? partialTicks : 0)) * 20;
		BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_BLOCKBREAKERWHEEL);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 2.5 / 16.0);
		matrixStackIn.mulPose(new Quaternion((float) -progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, -2.5 / 16.0);
		matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 1.0 / 16.0, 0);
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(-90, 0, 90, true));
		ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_BLOCKBREAKERBASE);
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.translate(0, progress / 8.0 - 1 / 8.0, 0);
		matrixStackIn.popPose();
		if (tileEntityIn.progress > 0) {
			matrixStackIn.pushPose();
			ComponentDirection direction = tileEntityIn.getComponent(ComponentType.Direction);
			Vec3i norm = direction.getDirection().getOpposite().getNormal();
			BlockPos off = tileEntityIn.getBlockPos().offset(norm);
			BlockState state = tileEntityIn.getLevel().getBlockState(off);
			PoseStack.Pose pose = matrixStackIn.last();
			VertexConsumer vertexconsumer1 = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(Math.min(9, (int) (tileEntityIn.progress * 9)))), pose.pose(), pose.normal());
			matrixStackIn.translate(norm.getX(), norm.getY(), norm.getZ());
			Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(state, off, tileEntityIn.getLevel(), matrixStackIn, vertexconsumer1);
			Minecraft.getInstance().renderBuffers().crumblingBufferSource().endBatch();
			matrixStackIn.popPose();
		}
	}
}
