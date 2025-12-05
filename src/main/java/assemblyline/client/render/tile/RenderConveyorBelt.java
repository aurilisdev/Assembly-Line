package assemblyline.client.render.tile;

import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.tile.belt.TileConveyorBelt;
import assemblyline.common.tile.belt.utils.ConveyorType;
import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.utilities.RenderingUtils;
import voltaic.prefab.utilities.math.MathUtils;

public class RenderConveyorBelt extends AbstractTileRenderer<TileConveyorBelt> {

    public RenderConveyorBelt(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TileConveyorBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStackIn.pushPose();

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

        ItemStack stack = inv.getItem(0);

        Vector3f move;

        ConveyorType type = tile.getConveyorType();

        matrixStackIn.pushPose();

        if (!stack.isEmpty()) {

            Vector3f itemVec = tile.getLocalItemLocationVector();

            move = tile.getDirectionVector();

            Direction direct = tile.getFacing().getOpposite();

            if (type != ConveyorType.HORIZONTAL) {

                move = move.add(0, type == ConveyorType.SLOPED_DOWN ? -1 : 1, 0);

            }

            move.mul(partialTicks/16.0f); // Must add tile speed later aka multiply by properties.conveyorClass.speed

            if (tile.running.getValue()) {

                itemVec = itemVec.add(move);

            }

            boolean blockItem = stack.getItem() instanceof BlockItem;

            switch (type) {

                case HORIZONTAL:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 0.05f / 16.0f) + move.y(), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    matrixStackIn.translate(0, 5.0f / (16.0f * 0.35f), 0);

                    if (!blockItem) {
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                    }

                    break;

                case SLOPED_DOWN:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.167 : 1.0f / 16.0f), itemVec.z());

                    matrixStackIn.scale(0.35f, 0.35f, 0.35f);

                    if (!blockItem) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.XN));
                        // matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));

                    }

                    int rotate = -45;

                    if (direct == Direction.NORTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(180, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                	if(!blockItem)
                	{
                	    matrixStackIn.translate(0, 0, -0.3);
                	}
                    } else if (direct == Direction.EAST) {
                	if(blockItem)
                	{
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YP));
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                	} else {
              	 	  matrixStackIn.translate(0, -0.4, 0.4);
            	 	  matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(45, MathUtils.YP));
            		}
                    } else if (direct == Direction.WEST) {
                	if(blockItem)
                	{
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-90, MathUtils.YP));
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                	} else {
                	    matrixStackIn.translate(0, -0.3, 0.5);
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-45, MathUtils.YP));
              		}

                    } else if (direct == Direction.SOUTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                	if(!blockItem)
                	{
                	    matrixStackIn.translate(0, 0, 0.4);
                	}
                    }

                    matrixStackIn.translate(0, 2.0f / (16.0f * 0.35f), 0);

                    break;

                case SLOPED_UP:

                    matrixStackIn.translate(itemVec.x(), itemVec.y() + (blockItem ? 0.4 : 7.0f / 16.0f), itemVec.z());

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
                	if(!blockItem)
                	{
                	    matrixStackIn.translate(0, 0, -0.2);
                	}
                    } else if (direct == Direction.EAST) {
                	if(blockItem)
                	{
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(90, MathUtils.YP));
                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                	} else {
            	 	  matrixStackIn.translate(0, -0.9, 0.3);
            	 	  matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-45, MathUtils.YP));
            		}
                    } else if (direct == Direction.WEST) {

                	if(blockItem)
                	{
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-90, MathUtils.YP));
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(rotate, MathUtils.XN));
                	} else {
                	    matrixStackIn.translate(0, -1, 0.4);
                	    matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(45, MathUtils.YP));
              		}

                    } else if (direct == Direction.SOUTH) {

                        matrixStackIn.mulPose(MathUtils.rotVectorQuaternionDeg(-rotate, MathUtils.XP));
                	if(!blockItem)
                	{
                	    matrixStackIn.translate(0, 0, 0.2);
                	}
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

        if (type == ConveyorType.SLOPED_DOWN) {

            matrixStackIn.translate(0, -1, 0);

            matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 180, 0));
            // matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));

        }

        ModelResourceLocation location = switch (type) {

            case SLOPED_DOWN -> tile.running.getValue() ? AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORDOWNANIMATED : AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORDOWN;
            case SLOPED_UP -> tile.running.getValue() ? AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORUPANIMATED : AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORUP;
            case VERTICAL -> {

                if (tile.getLevel().getBlockEntity(tile.getBlockPos().below()) instanceof GenericTileConveyorBelt belt && belt.getConveyorType() == ConveyorType.VERTICAL) {
                    if(!(tile.getLevel().getBlockEntity(tile.getBlockPos().above()) instanceof GenericTileConveyorBelt)){
    			yield tile.running.getValue() ? AssemblyLineClientRegister.MODEL_ELEVATORRUNNINGBUTLAST : AssemblyLineClientRegister.MODEL_ELEVATORBUTLAST;

                    }
                    yield tile.running.getValue() ? AssemblyLineClientRegister.MODEL_ELEVATORRUNNING : AssemblyLineClientRegister.MODEL_ELEVATOR;

                } else if(!(tile.getLevel().getBlockEntity(tile.getBlockPos().above()) instanceof GenericTileConveyorBelt)){
    			yield tile.running.getValue() ? AssemblyLineClientRegister.MODEL_ELEVATORBOTTOMRUNNINGBUTLAST : AssemblyLineClientRegister.MODEL_ELEVATORBOTTOMBUTLAST;

                }
		yield tile.running.getValue() ? AssemblyLineClientRegister.MODEL_ELEVATORBOTTOMRUNNING : AssemblyLineClientRegister.MODEL_ELEVATORBOTTOM;

            }
            default -> tile.running.getValue() ? AssemblyLineClientRegister.MODEL_CONVEYORANIMATED : AssemblyLineClientRegister.MODEL_CONVEYOR;
        };

        RenderingUtils.renderModel(getModel(location), tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

        matrixStackIn.popPose();

        move = tile.getDirectionVector();

        BakedModel model = getModel(AssemblyLineClientRegister.MODEL_MANIPULATOR);

        if (tile.isPusher.getValue()) {

            BlockPos nextBlockPos = tile.getNextPos().subtract(tile.getBlockPos());

            matrixStackIn.pushPose();

            matrixStackIn.translate(0, 1 / 16.0, 0);

            if (type == ConveyorType.SLOPED_DOWN) {

                matrixStackIn.translate(0, 0.4, 0);

            }

            matrixStackIn.translate(nextBlockPos.getX() - move.x(), nextBlockPos.getY() - move.y(), nextBlockPos.getZ() - move.z());

            RenderingUtils.prepareRotationalTileModel(tile, matrixStackIn);

            RenderingUtils.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

            matrixStackIn.popPose();

        }
        if (tile.isPuller.getValue()) {

            matrixStackIn.pushPose();

            matrixStackIn.translate(0, 1 / 16.0, 0);

            RenderingUtils.prepareRotationalTileModel(tile, matrixStackIn);

            if (type == ConveyorType.SLOPED_UP) {

                matrixStackIn.translate(0, 0.4, 0);

            }

            matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, 180, 0));
            // matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));

            RenderingUtils.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

            matrixStackIn.popPose();

        }

        matrixStackIn.popPose();

    }

    @Override
    public AABB getRenderBoundingBox(TileConveyorBelt blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(3);
    }

    public int getInventorySize() {
        return 1;
    }

}