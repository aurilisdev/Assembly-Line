package assemblyline.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.common.tile.TileCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.StringTextComponent;

public class RenderCache extends TileEntityRenderer<TileCache> {

	public RenderCache(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileCache tileCrate, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		StringTextComponent displayNameIn = new StringTextComponent("HELLOHELLO");
		for (Direction dir : Direction.values()) {
			matrixStackIn.push();
			matrixStackIn.translate(0.5 + dir.getXOffset() / 2.0, 0.5 + dir.getYOffset() / 2.0,
					0.5 + dir.getZOffset() / 2.0);
			switch (dir) {
			case DOWN:
				matrixStackIn.rotate(new Quaternion(-90, 0, 0, true));
				break;
			case EAST:
				matrixStackIn.rotate(new Quaternion(0, -90, 0, true));
				break;
			case NORTH:
				break;
			case SOUTH:
				matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
				break;
			case UP:
				matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
				break;
			case WEST:
				matrixStackIn.rotate(new Quaternion(0, 90, 0, true));
				break;
			default:
				break;
			}
			FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
			float scale = 0.025f / (fontrenderer.getStringPropertyWidth(displayNameIn) / 25f);
			matrixStackIn.scale(-scale, -scale, scale);
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f2 = -fontrenderer.getStringPropertyWidth(displayNameIn) / 2;
			int i1 = NativeImage.getCombined(255, 0,0,0);
			fontrenderer.func_243247_a(displayNameIn, f2, 0, i1, false, matrix4f, bufferIn, true, 0, combinedLightIn);
			matrixStackIn.pop();
		}
	}

}
