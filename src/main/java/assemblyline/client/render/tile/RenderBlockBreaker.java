package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.tile.TileBlockBreaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.RenderingUtils;
import voltaic.prefab.utilities.math.MathUtils;

public class RenderBlockBreaker extends AbstractTileRenderer<TileBlockBreaker> {

    public RenderBlockBreaker(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TileBlockBreaker breaker, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        double progress = 0;
        if ((double) breaker.ticksSinceCheck.getValue() / (double) breaker.currentWaitTime.getValue() > 0) {
            progress = (breaker.<ComponentTickable>getComponent(IComponentType.Tickable).getTicks() + (breaker.works.getValue() ? partialTicks : 0)) * 20;
        }

        BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(AssemblyLineClientRegister.MODEL_BLOCKBREAKERWHEEL);
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

        if ((double) breaker.ticksSinceCheck.getValue() / (double) breaker.currentWaitTime.getValue() <= 0) {
            return;
        }

        matrixStackIn.pushPose();

        Direction breaking = breaker.getFacing().getOpposite();

        BlockPos offset = breaker.getBlockPos().relative(breaking);

        BlockState state = breaker.getLevel().getBlockState(offset);

        PoseStack.Pose pose = matrixStackIn.last();

        VertexConsumer vertexconsumer1 = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get((int) (breaker.progress.getValue() * 9))), pose, 1.0F);

        matrixStackIn.translate(breaking.getStepX(), 0, breaking.getStepZ());

        Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(state, offset, breaker.getLevel(), matrixStackIn, vertexconsumer1, level().getModelData(offset));
        //Minecraft.getInstance().renderBuffers().crumblingBufferSource().endBatch();
        matrixStackIn.popPose();
    }
}
