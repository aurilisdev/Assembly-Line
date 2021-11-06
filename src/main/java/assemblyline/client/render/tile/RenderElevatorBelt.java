package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileElevatorBelt;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RenderElevatorBelt extends BlockEntityRenderer<TileElevatorBelt> {

    public RenderElevatorBelt(BlockEntityRenderDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    public void render(TileElevatorBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.pushPose();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	BlockPos pos = tile.getBlockPos();
	Level world = tile.getLevel();
	boolean down = !(world.getBlockEntity(pos.relative(Direction.DOWN)) instanceof TileElevatorBelt);
	boolean running = tile.currentSpread > 0;
	BakedModel model = running ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORRUNNING)
		: Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATOR);
	if (down) {
	    model = running ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORBOTTOMRUNNING)
		    : Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORBOTTOM);
	}
	UtilitiesRendering.renderModel(model, tile, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	matrixStackIn.popPose();
	ComponentInventory inv = tile.getComponent(ComponentType.Inventory);

	int totalSlotsUsed = 0;
	for (int i = 0; i < inv.getContainerSize(); i++) {
	    ItemStack stack = inv.getItem(i);
	    if (!stack.isEmpty()) {
		totalSlotsUsed++;
	    }
	}
	double progressModifier = (tile.progress + 8.0 + (tile.currentSpread <= 0 || tile.halted ? 0 : partialTicks)) / 16.0;
	BlockEntity next = world.getBlockEntity(pos.above());
	if (!(next instanceof TileConveyorBelt)) {
	    progressModifier -= 0.5;
	}
	Direction dir = tile.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	if (totalSlotsUsed > 0) {
	    for (int i = 0; i < inv.getContainerSize(); i++) {
		ItemStack stack = inv.getItem(i);
		if (totalSlotsUsed == 1) {
		    matrixStackIn.pushPose();
		    matrixStackIn.translate(0, stack.getItem() instanceof BlockItem ? 0.48 : 0.33, 0);
		    matrixStackIn.translate(0.5 + dir.getOpposite().getStepX() * 0.05, progressModifier, 0.5 + dir.getOpposite().getStepZ() * 0.05);
		    if (dir == Direction.NORTH) {
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
		    }
		    int rotate = 90;
		    matrixStackIn.mulPose(dir == Direction.NORTH ? Vector3f.ZN.rotationDegrees(rotate)
			    : dir == Direction.SOUTH ? Vector3f.ZP.rotationDegrees(-rotate)
				    : dir == Direction.WEST ? Vector3f.ZN.rotationDegrees(rotate) : Vector3f.ZP.rotationDegrees(-rotate));
		    matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(-90));
		    Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn,
			    matrixStackIn, bufferIn);
		    matrixStackIn.popPose();
		} else if (totalSlotsUsed == 2) {
		    matrixStackIn.pushPose();
		    matrixStackIn.translate(0, stack.getItem() instanceof BlockItem ? 0.48 : 0.33, 0);
		    matrixStackIn.translate(0.5 + dir.getOpposite().getStepX() * 0.05, progressModifier, 0.5 + dir.getOpposite().getStepZ() * 0.05);
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate((i == 0 ? 0.25 : 0.75) - 0.5, 0, 0);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.translate(0, 0, (i == 0 ? 0.25 : 0.75) - 0.5);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.translate(0, 0, (i == 0 ? 0.25 : 0.75) - 0.5);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
		    } else if (dir == Direction.SOUTH) {
			matrixStackIn.translate((i == 0 ? 0.25 : 0.75) - 0.5, 0, 0);
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
		    }
		    int rotate = 90;
		    matrixStackIn.mulPose(dir == Direction.NORTH ? Vector3f.ZN.rotationDegrees(rotate)
			    : dir == Direction.SOUTH ? Vector3f.ZP.rotationDegrees(-rotate)
				    : dir == Direction.WEST ? Vector3f.ZN.rotationDegrees(rotate) : Vector3f.ZP.rotationDegrees(-rotate));
		    matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(-90));
		    Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn,
			    matrixStackIn, bufferIn);
		    matrixStackIn.popPose();
		}
	    }
	}
    }
}