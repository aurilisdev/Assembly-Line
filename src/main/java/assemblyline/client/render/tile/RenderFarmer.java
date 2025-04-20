package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import assemblyline.common.tile.TileFarmer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.utilities.RenderingUtils;

public class RenderFarmer extends AbstractTileRenderer<TileFarmer> {

    private static final float MIN_X = 3.0F / 16.0F;
    private static final float MAX_X = 13.0F / 16.0F;
    private static final float MIN_Y = 3.001F / 16.0F;
    private static final float MAX_Y = 15.5F / 16.0F;
    private static final float MIN_Z = 3.0F / 16.0F;
    private static final float MAX_Z = 13.0F / 16.0F;

    public RenderFarmer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TileFarmer entity, float ticks, PoseStack stack, MultiBufferSource source, int light, int overlay) {
        AABB aabb = new AABB(MIN_X, MIN_Y, MIN_Z, MAX_X, MAX_Y, MAX_Z);
        VertexConsumer builder = source.getBuffer(Sheets.translucentCullBlockSheet());
        RenderingUtils.renderFluidBox(stack, Minecraft.getInstance(), builder, aabb, new FluidStack(Fluids.WATER, 1000), light, overlay, RenderingUtils.ALL_FACES);
    }

}
