package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.common.tile.TileCrate;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RenderCrate extends AbstractTileRenderer<TileCrate> {

	public RenderCrate(TileEntityRendererDispatcher context) {
		super(context);
	}

	@Override
	public void render(TileCrate tileCrate, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (tileCrate.getCount() <= 0) {
			return;
		}
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.5 + dir.getStepX() / 1.999, 0.3 + dir.getStepY() / 2.0, 0.5 + dir.getStepZ() / 1.999);
				switch (dir) {
				case EAST:
					matrixStackIn.mulPose(new Quaternion(0, -90, 0, true));
					break;
				case NORTH:
					break;
				case SOUTH:
					matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
					break;
				case WEST:
					matrixStackIn.mulPose(new Quaternion(0, 90, 0, true));
					break;
				default:
					break;
				}

				ItemStack item = ItemStack.EMPTY;

				for (ItemStack stack : tileCrate.<ComponentInventory>getComponent(IComponentType.Inventory).getItems()) {
					if (!stack.isEmpty()) {
						item = stack;
						break;
					}
				}
				ITextComponent displayNameIn = new StringTextComponent(tileCrate.getCount() + "x" + item.getHoverName().getString());

				FontRenderer fontrenderer = Minecraft.getInstance().font;

				float scale = 0.025f / (fontrenderer.width(displayNameIn) / 32f);

				matrixStackIn.scale(-scale, -scale, scale);

				Matrix4f matrix4f = matrixStackIn.last().pose();

				float f2 = -fontrenderer.width(displayNameIn) / 2.0f;

				fontrenderer.drawInBatch(displayNameIn, f2, 0, 0, false, matrix4f, bufferIn, false, 0, combinedLightIn);

				matrixStackIn.popPose();

				ItemStack stack = tileCrate.<ComponentInventory>getComponent(IComponentType.Inventory).getItem(0);

				matrixStackIn.pushPose();

				matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-180));

				if (dir == Direction.NORTH) {
					matrixStackIn.translate(-1, 0, 0);
				}

				if (dir == Direction.EAST) {
					matrixStackIn.translate(-1, 0, -1);
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
				}

				if (dir == Direction.SOUTH) {
					matrixStackIn.translate(0, 0, -1);
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-180));
				}

				if (dir == Direction.WEST) {
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
				}

				matrixStackIn.translate(0.5, 0.6, 0);

				if (stack.getItem() instanceof BlockItem) {

					matrixStackIn.scale(0.35f, 0.35f, 0.35f);

				} else {

					matrixStackIn.scale(0.4f, 0.4f, 0.4f);

				}

				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, 0xF000F0, combinedOverlayIn, matrixStackIn, bufferIn);

				matrixStackIn.popPose();
			}
		}
	}


}