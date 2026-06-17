package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.TileRancher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.utilities.RenderingUtils;
import voltaic.prefab.utilities.math.MathUtils;

public class RenderRancher extends AbstractTileRenderer<TileRancher> {

    public RenderRancher(BlockEntityRendererProvider.Context context) {
	super(context);
    }

    @Override
    public void render(TileRancher rancher, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn,
	    int combinedLightIn, int combinedOverlayIn) {

	double progress = 0;
	if (rancher.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic)
		.getJoulesStored() >= AssemblyLineConstants.RANCHER_USAGE) {
	    progress = System.currentTimeMillis() % 100 / 100.0 * 40;
	}

	// progress = Math.sin(progress ) * 50;
	BakedModel ibakedmodel = Minecraft.getInstance().getModelManager()
		.getModel(AssemblyLineClientRegister.MODEL_RANCHERLEFT);
	matrixStackIn.pushPose();
	RenderingUtils.prepareRotationalTileModel(rancher, matrixStackIn);
	matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
	// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
	matrixStackIn.translate(9.0 / 16.0, 3 / 16.0, 2.5 / 16.0);
	matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) -progress - 50f, 0, 0));
	// matrixStackIn.mulPose(new Quaternion((float) -progress - 50f, 0, 0, true));
	RenderingUtils.renderModel(ibakedmodel, rancher, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn,
		combinedOverlayIn);
	matrixStackIn.popPose();
	ibakedmodel = Minecraft.getInstance().getModelManager().getModel(AssemblyLineClientRegister.MODEL_RANCHERRIGHT);
	matrixStackIn.pushPose();
	RenderingUtils.prepareRotationalTileModel(rancher, matrixStackIn);
	matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 0, 90));
	// matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
	matrixStackIn.translate(1.0 / 16.0, 3 / 16.0, -2.5 / 16.0);
	matrixStackIn.mulPose(MathUtils.rotQuaternionDeg((float) progress + 50f, 0, 0));
	// matrixStackIn.mulPose(new Quaternion((float) progress + 50f, 0, 0, true));
	RenderingUtils.renderModel(ibakedmodel, rancher, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn,
		combinedOverlayIn);
	matrixStackIn.popPose();
    }
}
