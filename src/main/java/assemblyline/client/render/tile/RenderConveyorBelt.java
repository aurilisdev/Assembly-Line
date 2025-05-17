package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import assemblyline.client.AssemblyLineClientRegister;
import assemblyline.common.tile.belt.TileConveyorBelt;
import assemblyline.common.tile.belt.utils.ConveyorType;
import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import voltaic.client.render.AbstractTileRenderer;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.utilities.RenderingUtils;

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

			minecraft().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
		}

		matrixStackIn.popPose();

		matrixStackIn.pushPose();

		matrixStackIn.translate(0, 1 / 16.0, 0);

		RenderingUtils.prepareRotationalTileModel(tile, matrixStackIn);

		if (type == ConveyorType.SLOPED_DOWN) {

			matrixStackIn.translate(0, -1, 0);

			matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));

		}

		ResourceLocation location = switch (type) {

		case SLOPED_DOWN -> tile.running.getValue() ? AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORDOWNANIMATED : AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORDOWN;
		case SLOPED_UP -> tile.running.getValue() ? AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORUPANIMATED : AssemblyLineClientRegister.MODEL_SLOPEDCONVEYORUP;
		case VERTICAL -> {

			if (tile.getLevel().getBlockEntity(tile.getBlockPos().below()) instanceof GenericTileConveyorBelt belt && belt.getConveyorType() == ConveyorType.VERTICAL) {

				yield tile.running.getValue() ? AssemblyLineClientRegister.MODEL_ELEVATORRUNNING : AssemblyLineClientRegister.MODEL_ELEVATOR;

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

			matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));

			RenderingUtils.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

			matrixStackIn.popPose();

		}

		matrixStackIn.popPose();

	}

	public int getInventorySize() {
		return 1;
	}

}