package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import assemblyline.common.tile.TileFarmer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.utilities.RenderingUtils;

public class RenderFarmer extends AbstractTileRenderer<TileFarmer> {

    private static final float MIN_X = 3.0F / 16.0F;
    private static final float MAX_X = 13.0F / 16.0F;
    private static final float MIN_Y = 3.001F / 16.0F;
    private static final float MAX_Y = 15.5F / 16.0F;
    private static final float MIN_Z = 3.0F / 16.0F;
    private static final float MAX_Z = 13.0F / 16.0F;

    public RenderFarmer(TileEntityRendererDispatcher context) {
        super(context);
    }

    @Override
    public void render(TileFarmer entity, float ticks, MatrixStack stack, IRenderTypeBuffer source, int light, int overlay) {
        AxisAlignedBB aabb = new AxisAlignedBB(MIN_X, MIN_Y, MIN_Z, MAX_X, MAX_Y, MAX_Z);
        IVertexBuilder builder = source.getBuffer(Atlases.translucentCullBlockSheet());
        RenderingUtils.renderFluidBox(stack, Minecraft.getInstance(), builder, aabb, new FluidStack(Fluids.WATER, 1000), light, overlay, RenderingUtils.ALL_FACES);
    }

}
