package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileBlockBreaker;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class RenderBlockBreaker extends AbstractTileRenderer<TileBlockBreaker> {

	public RenderBlockBreaker(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileBlockBreaker breaker, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

		double progress = 0;
		if (breaker.getProgress() > 0) {
			progress = (breaker.<ComponentTickable>getComponent(IComponentType.Tickable).getTicks() + (breaker.works.get() ? partialTicks : 0)) * 20;
		}

		BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_BLOCKBREAKERWHEEL);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(breaker, matrixStackIn);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
		// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 2.5 / 16.0);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) -progress, 0, 0));
		// matrixStackIn.mulPose(new Quaternion((float) -progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, breaker, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(breaker, matrixStackIn);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
		// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, -2.5 / 16.0);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) progress, 0, 0));
		// matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, breaker, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();

		if (breaker.getProgress() <= 0) {
			return;
		}

		matrixStackIn.pushPose();
		Vec3i norm = breaker.getFacing().getOpposite().getNormal();
		BlockPos off = breaker.getBlockPos().offset(norm);
		BlockState state = breaker.getLevel().getBlockState(off);
		PoseStack.Pose pose = matrixStackIn.last();
		VertexConsumer vertexconsumer1 = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(Math.min(9, (int) (breaker.progress.get() * 9)))), pose.pose(), pose.normal(), 1.0F);
		matrixStackIn.translate(norm.getX(), norm.getY(), norm.getZ());
		Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(state, off, breaker.getLevel(), matrixStackIn, vertexconsumer1);
		Minecraft.getInstance().renderBuffers().crumblingBufferSource().endBatch();
		matrixStackIn.popPose();
	}
}
