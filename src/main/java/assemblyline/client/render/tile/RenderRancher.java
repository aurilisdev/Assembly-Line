package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileRancher;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;

public class RenderRancher extends AbstractTileRenderer<TileRancher> {

	public RenderRancher(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileRancher rancher, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

		double progress = 0;
		if (rancher.<ComponentElectrodynamic>getComponent(ComponentType.Electrodynamic).getJoulesStored() >= rancher.getUsage()) {
			progress = rancher.getLevel().getDayTime() + partialTicks;
		}

		progress = Math.sin(progress / 80) * 50;
		BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_RANCHERLEFT);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(rancher, matrixStackIn);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
		// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(9.0 / 16.0, 3 / 16.0, 2.5 / 16.0);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) -progress - 50f, 0, 0));
		// matrixStackIn.mulPose(new Quaternion((float) -progress - 50f, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, rancher, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_RANCHERRIGHT);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(rancher, matrixStackIn);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
		// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 3 / 16.0, -2.5 / 16.0);
		matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) progress + 50f, 0, 0));
		// matrixStackIn.mulPose(new Quaternion((float) progress + 50f, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, rancher, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
	}
}
