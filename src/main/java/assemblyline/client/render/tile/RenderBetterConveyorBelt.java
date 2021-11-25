package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileBetterConveyorBelt;
import assemblyline.common.tile.TileBetterConveyorBelt.ConveyorType;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class RenderBetterConveyorBelt implements BlockEntityRenderer<TileBetterConveyorBelt> {

    public RenderBetterConveyorBelt(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileBetterConveyorBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.pushPose();
	ComponentInventory inv = tile.getComponent(ComponentType.Inventory);
	ItemStack stack = inv.getItem(0);
	Vector3f itemVec = tile.getObjectLocal();
	Vector3f move = tile.getDirectionAsVector();
	move.mul(partialTicks / 16.0f);
	if (tile.running) {
	    itemVec.add(move);
	}
	matrixStackIn.pushPose();
	ResourceLocation location = ClientRegister.MODEL_CONVEYOR;
	if (tile.running) {
	    location = ClientRegister.MODEL_CONVEYORANIMATED;
	}
	switch (tile.type) {
	case Horizontal:
	    matrixStackIn.translate(itemVec.x(), stack.getItem() instanceof BlockItem ? 0.48 : 0.33, itemVec.z());
	    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
	    if (!(stack.getItem() instanceof BlockItem)) {
		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
	    }
	    break;
	case SlopedDown:
	    location = tile.running ? ClientRegister.MODEL_SLOPEDCONVEYORDOWNANIMATED : ClientRegister.MODEL_SLOPEDCONVEYORDOWN;
	    break;
	case SlopedUp:
	    double verticalComponent = Math.sqrt(itemVec.dot(itemVec));
	    Vector3f direction = tile.getDirectionAsVector();
	    verticalComponent = Mth.clampedLerp(-5 / 16.0, 1, verticalComponent) * (direction.x() + direction.y() + direction.z() > 0 ? 1 : -1);
	    if (direction.x() + direction.y() + direction.z() < 0) {
		verticalComponent += 1 + 5 / 16.0;
	    }
	    matrixStackIn.translate(itemVec.x(), (stack.getItem() instanceof BlockItem ? 0.48 : 0.33) + verticalComponent, itemVec.z());
	    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
	    if (!(stack.getItem() instanceof BlockItem)) {
		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
	    }
	    location = tile.running ? ClientRegister.MODEL_SLOPEDCONVEYORUPANIMATED : ClientRegister.MODEL_SLOPEDCONVEYORUP;
	    break;
	case Vertical:
	    break;
	default:
	    break;
	}
	Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn,
		0);
	matrixStackIn.popPose();

	BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);

	matrixStackIn.pushPose();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	if (tile.type == ConveyorType.SlopedDown) {
	    matrixStackIn.translate(0, -1, 0);
	    matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
	}
	UtilitiesRendering.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	matrixStackIn.popPose();

	if (tile.isPusher || tile.isPuller) {
	    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATOR);
	    move = tile.getDirectionAsVector();
	    if (tile.isPusher) {
		BlockPos nextBlockPos = tile.getNextPos().subtract(tile.getBlockPos());
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 1 / 16.0, 0);
		matrixStackIn.translate(nextBlockPos.getX() - move.x(), nextBlockPos.getY() - move.y(), nextBlockPos.getZ() - move.z());
		UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
		UtilitiesRendering.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
	    }
	    if (tile.isPuller) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 1 / 16.0, 0);
		UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
		if (tile.type == ConveyorType.SlopedUp) {
		    matrixStackIn.translate(0, 0.4, 0);
		}
		matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
		UtilitiesRendering.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
	    }
	}
	matrixStackIn.popPose();

    }
}