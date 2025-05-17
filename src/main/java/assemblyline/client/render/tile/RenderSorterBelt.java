package assemblyline.client.render.tile;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.tile.belt.TileSorterBelt;
import assemblyline.common.tile.belt.utils.ConveyorType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.utilities.RenderingUtils;

public class RenderSorterBelt extends AbstractTileRenderer<TileSorterBelt> {

    public RenderSorterBelt(TileEntityRendererDispatcher context) {
        super(context);
    }

    @Override
    public void render(@Nonnull TileSorterBelt tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

        ItemStack stack = inv.getItem(0);

        Vector3f move;

        ConveyorType type = tile.getConveyorType();

        matrixStackIn.pushPose();

        if (!stack.isEmpty()) {

            Vector3f itemVec = tile.getLocalItemLocationVector();

            move = tile.getDirectionVector();

            Direction direct = tile.getDirectionForNext();

            if (type != ConveyorType.HORIZONTAL) {

                move.add(0, type == ConveyorType.SLOPED_DOWN ? -1 : 1, 0);

            }

            move.mul(1.0F / 16.0F);

            if (tile.running.getValue()) {

                itemVec.add(move);

            }

            boolean blockItem = stack.getItem() instanceof BlockItem;

            switch (type) {

                case HORIZONTAL:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 5.0f / 16.0f) + move.y(), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    matrixStackIn.translate(0, 5.0f / (16.0f * 0.35f), 0);

                    if (!blockItem) {

                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    if (direct == Direction.EAST || direct == Direction.WEST) {
                        matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(90));
                    }

                    break;

                case SLOPED_DOWN:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 2.0f / 16.0f), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    if (!blockItem) {

                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    int rotate = -45;

                    if (direct == Direction.NORTH) {

                        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));

                    } else if (direct == Direction.EAST) {

                    	matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
                        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));

                    } else if (direct == Direction.WEST) {

                    	matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));

                    } else if (direct == Direction.SOUTH) {

                    	matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-rotate));

                    }

                    // matrixStackIn.mulPose(direct == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate) : direct == Direction.SOUTH ?
                    // Vector3f.XP.rotationDegrees(-rotate) : direct == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) :
                    // Vector3f.XP.rotationDegrees(-rotate));

                    matrixStackIn.translate(0, 2.0f / (16.0f * 0.35f), 0);

                    break;

                case SLOPED_UP:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 7.0f / 16.0f), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    if (!blockItem) {

                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    rotate = 45;

                    if (direct == Direction.NORTH) {

                    	matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));

                    } else if (direct == Direction.EAST) {

                    	matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
                        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));

                    } else if (direct == Direction.WEST) {

                    	matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(rotate));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));

                    } else if (direct == Direction.SOUTH) {

                    	matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-rotate));

                    }

                    // matrixStackIn.mulPose(direct == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate) : direct == Direction.SOUTH ?
                    // Vector3f.XP.rotationDegrees(-rotate) : direct == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) :
                    // Vector3f.XP.rotationDegrees(-rotate));

                    matrixStackIn.translate(0, 5.0f / (16.0f * 0.35f), 0);

                    break;

                case VERTICAL:

                    matrixStackIn.translate(0.5, itemVec.y() + (blockItem ? 0.167 : 5.0f / 16.0f) + 5.0f / 16.0f, 0.5);

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    if (!blockItem) {

                        matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    break;

                default:

                    break;

            }

            minecraft().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        }

        matrixStackIn.popPose();

        matrixStackIn.pushPose();

        matrixStackIn.translate(0, 1 / 16.0, 0);

        RenderingUtils.prepareRotationalTileModel(tile, matrixStackIn);

        RenderingUtils.renderModel(getModel(tile.running.getValue() ? AssemblyLineClientRegister.MODEL_SORTERBELT_RUNNING : AssemblyLineClientRegister.MODEL_SORTERBELT), tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

        matrixStackIn.popPose();

    }
}
