package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.TileMobGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Quaternion;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.utilities.RenderingUtils;

public class RenderMobGrinder extends AbstractTileRenderer<TileMobGrinder> {

    public RenderMobGrinder(TileEntityRendererDispatcher context) {
        super(context);
    }

    @Override
    public void render(TileMobGrinder grinder, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        double progress = 0;

        if (grinder.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored() >= AssemblyLineConstants.MOBGRINDER_USAGE * grinder.powerUsageMultiplier.getValue()) {
            progress = System.currentTimeMillis() % 150 / 150.0 * 360.0;
        }

        IBakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(AssemblyLineClientRegister.MODEL_MOBGRINDERSIDEWHEEL);
        matrixStackIn.pushPose();
        RenderingUtils.prepareRotationalTileModel(grinder, matrixStackIn);
        matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
        matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 2.5 / 16.0);
        matrixStackIn.mulPose(new Quaternion((float) -progress, 0, 0, true));
        RenderingUtils.renderModel(ibakedmodel, grinder, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        RenderingUtils.prepareRotationalTileModel(grinder, matrixStackIn);
        matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
        matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, -2.5 / 16.0);
        matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
        RenderingUtils.renderModel(ibakedmodel, grinder, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        ibakedmodel = Minecraft.getInstance().getModelManager().getModel(AssemblyLineClientRegister.MODEL_MOBGRINDERCENTERWHEEL);
        matrixStackIn.pushPose();
        RenderingUtils.prepareRotationalTileModel(grinder, matrixStackIn);
        matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
        matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 0);
        matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
        RenderingUtils.renderModel(ibakedmodel, grinder, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();

    }
}
