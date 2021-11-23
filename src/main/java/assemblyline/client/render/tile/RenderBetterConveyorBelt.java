package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileBetterConveyorBelt;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class RenderBetterConveyorBelt implements BlockEntityRenderer<TileBetterConveyorBelt> {

    public RenderBetterConveyorBelt(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileBetterConveyorBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	ResourceLocation location = ClientRegister.MODEL_CONVEYOR;
	if (tile.running) {
	    location = ClientRegister.MODEL_CONVEYORANIMATED;
	}
	BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
	ComponentInventory inv = tile.getComponent(ComponentType.Inventory);
	ItemStack stack = inv.getItem(0);
	matrixStackIn.pushPose();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	UtilitiesRendering.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	matrixStackIn.popPose();

	Vector3f itemVec = tile.getObjectLocal();
	Vector3f move = tile.getDirectionAsVector();
	move.mul(partialTicks / 16.0f);
	if (!tile.waiting) {
	    itemVec.add(move);
	}
	matrixStackIn.pushPose();
	switch (tile.type) {
	case Horizontal:
	    matrixStackIn.translate(itemVec.x(), (stack.getItem() instanceof BlockItem ? 0.48 : 0.33), itemVec.z());
	    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
	    if (!(stack.getItem() instanceof BlockItem)) {
		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
	    }
	    break;
	case SlopedDown:
	    break;
	case SlopedUp:
	    break;
	case Vertical:
	    break;
	default:
	    break;
	}

	Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn,
		0);
	matrixStackIn.popPose();
    }
}