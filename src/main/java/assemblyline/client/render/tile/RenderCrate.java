package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import assemblyline.common.tile.TileCrate;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class RenderCrate extends AbstractTileRenderer<TileCrate> {

	public RenderCrate(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileCrate tileCrate, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
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
				Component displayNameIn = new TextComponent(tileCrate.getCount() + "x" + item.getHoverName().getString());

				Font fontrenderer = Minecraft.getInstance().font;

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

				Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, 0xF000F0, combinedOverlayIn, matrixStackIn, bufferIn, 0);

				matrixStackIn.popPose();
			}
		}
	}


}