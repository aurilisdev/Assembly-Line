package assemblyline.client.render.tile;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.belt.TileSorterBelt;
import assemblyline.common.tile.belt.utils.ConveyorType;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderSorterBelt extends AbstractTileRenderer<TileSorterBelt> {

    public RenderSorterBelt(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileSorterBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

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

                move = move.add(0, type == ConveyorType.SLOPED_DOWN ? -1 : 1, 0);

            }

            move = move.mul(1.0F / 16.0F);

            if (tile.running.get()) {

                itemVec = itemVec.add(move);

            }

            boolean blockItem = stack.getItem() instanceof BlockItem;

            switch (type) {

                case HORIZONTAL:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 5.0f / 16.0f) + move.y(), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    matrixStackIn.translate(0, 5.0f / (16.0f * 0.35f), 0);

                    if (!blockItem) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    if (direct == Direction.EAST || direct == Direction.WEST) {
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YN));
                    }

                    break;

                case SLOPED_DOWN:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 2.0f / 16.0f), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    if (!blockItem) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    int rotate = -45;

                    if (direct == Direction.NORTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(180, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));

                    } else if (direct == Direction.EAST) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));

                    } else if (direct == Direction.WEST) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-90, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));

                    } else if (direct == Direction.SOUTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));

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

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    rotate = 45;

                    if (direct == Direction.NORTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(180, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));

                    } else if (direct == Direction.EAST) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));

                    } else if (direct == Direction.WEST) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-90, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));

                    } else if (direct == Direction.SOUTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));

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

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    break;

                default:

                    break;

            }

            minecraft().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, tile.getLevel(), 0);
        }

        matrixStackIn.popPose();

        matrixStackIn.pushPose();

        matrixStackIn.translate(0, 1 / 16.0, 0);

        RenderingUtils.prepareRotationalTileModel(tile, matrixStackIn);

        RenderingUtils.renderModel(getModel(tile.running.get() ? ClientRegister.MODEL_SORTERBELT_RUNNING : ClientRegister.MODEL_SORTERBELT), tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

        matrixStackIn.popPose();

    }
}
