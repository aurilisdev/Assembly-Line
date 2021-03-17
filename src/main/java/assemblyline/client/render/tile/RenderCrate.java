package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.common.tile.TileCrate;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

public class RenderCrate extends TileEntityRenderer<TileCrate> {

    public RenderCrate(TileEntityRendererDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    public void render(TileCrate tileCrate, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	if (tileCrate.getCount() > 0) {
	    for (Direction dir : Direction.values()) {
		if (dir != Direction.UP && dir != Direction.DOWN) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0.5 + dir.getXOffset() / 1.999, 0.3 + dir.getYOffset() / 2.0, 0.5 + dir.getZOffset() / 1.999);
		    switch (dir) {
		    case EAST:
			matrixStackIn.rotate(new Quaternion(0, -90, 0, true));
			break;
		    case NORTH:
			break;
		    case SOUTH:
			matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
			break;
		    case WEST:
			matrixStackIn.rotate(new Quaternion(0, 90, 0, true));
			break;
		    default:
			break;
		    }
		    StringTextComponent displayNameIn = new StringTextComponent(tileCrate.getCount() + "x"
			    + tileCrate.<ComponentInventory>getComponent(ComponentType.Inventory).getStackInSlot(0).getDisplayName().getString());
		    FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
		    float scale = 0.025f / (fontrenderer.getStringPropertyWidth(displayNameIn) / 32f);
		    matrixStackIn.scale(-scale, -scale, scale);
		    Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
		    float f2 = -fontrenderer.getStringPropertyWidth(displayNameIn) / 2.0f;
		    fontrenderer.func_243247_a(displayNameIn, f2, 0, 0, false, matrix4f, bufferIn, false, 0, combinedLightIn);
		    matrixStackIn.pop();
		    ItemStack stack = tileCrate.<ComponentInventory>getComponent(ComponentType.Inventory).getStackInSlot(0).getStack();
		    matrixStackIn.push();
		    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-180));
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate(-1, 0, 0);
		    }
		    if (dir == Direction.EAST) {
			matrixStackIn.translate(-1, 0, -1);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90));
		    }
		    if (dir == Direction.SOUTH) {
			matrixStackIn.translate(0, 0, -1);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-180));
		    }
		    if (dir == Direction.WEST) {
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
		    }
		    matrixStackIn.translate(0.5, 0.6, 0);
		    if (stack.getItem() instanceof BlockItem) {
			matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    } else {
			matrixStackIn.scale(0.4f, 0.4f, 0.4f);
		    }
		    Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.NONE, 0Xffffff, combinedOverlayIn, matrixStackIn,
			    bufferIn);
		    matrixStackIn.pop();
		}
	    }
	}
    }

}
