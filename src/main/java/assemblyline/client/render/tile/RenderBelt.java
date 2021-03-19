package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileBelt;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.api.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class RenderBelt extends TileEntityRenderer<TileBelt> {

    public RenderBelt(TileEntityRendererDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    public void render(TileBelt tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.push();
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	IBakedModel model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYOR);
	ComponentDirection direction = tile.getComponent(ComponentType.Direction);
	BlockPos pos = tile.getPos();
	World world = tile.getWorld();
	boolean isSloped = !(world.getTileEntity(pos.offset(direction.getDirection().getOpposite())) instanceof TileBelt)
		&& (world.getTileEntity(pos.offset(Direction.UP).offset(direction.getDirection().getOpposite())) instanceof TileBelt
			|| world.getTileEntity(pos.offset(Direction.DOWN).offset(direction.getDirection().getOpposite())) instanceof TileBelt);
	boolean up = world.getTileEntity(pos.offset(Direction.UP).offset(direction.getDirection().getOpposite())) instanceof TileBelt;
	if (isSloped) {
	    if (tile.progress > -1) {
		model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYORANIMATED);
	    } else {
		model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYOR);
	    }
	} else if (tile.progress > -1) {
	    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATED);
	}
	UtilitiesRendering.renderModel(model, tile, RenderType.getSolid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	matrixStackIn.pop();
	ComponentInventory inv = tile.getComponent(ComponentType.Inventory);

	Direction dir = direction.getDirection();
	int totalSlotsUsed = 0;
	for (int i = 0; i < inv.getSizeInventory(); i++) {
	    ItemStack stack = inv.getStackInSlot(i);
	    if (!stack.isEmpty()) {
		totalSlotsUsed++;
	    }
	}
	double progressModifier = (tile.progress + (tile.halted ? 0 : partialTicks)) / 16.0;
	if (totalSlotsUsed > 0) {
	    for (int i = 0; i < inv.getSizeInventory(); i++) {
		ItemStack stack = inv.getStackInSlot(i);
		if (totalSlotsUsed == 1) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0,
			    (stack.getItem() instanceof BlockItem ? 0.48 : 0.33) + (isSloped ? progressModifier : 0) * (up ? 1 : -1), 0);
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate(0.5, 0, progressModifier);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.translate(1 - progressModifier, 0, 0.5);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.translate(progressModifier, 0, 0.5);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90));
		    } else if (dir == Direction.SOUTH) {
			matrixStackIn.translate(0.5, 0, 1 - progressModifier);
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90));
		    }
		    Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn,
			    bufferIn);
		    matrixStackIn.pop();
		} else if (totalSlotsUsed == 2) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0, stack.getItem() instanceof BlockItem ? 0.48 : 0.33 + (isSloped ? progressModifier : 0) * (up ? 1 : -1),
			    0);
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate(i == 0 ? 0.25 : 0.75, 0, progressModifier);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.translate(1 - progressModifier, 0, i == 0 ? 0.25 : 0.75);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.translate(progressModifier, 0, i == 0 ? 0.25 : 0.75);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90));
		    } else if (dir == Direction.SOUTH) {
			matrixStackIn.translate(i == 0 ? 0.25 : 0.75, 0, 1 - progressModifier);
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90));
		    }
		    Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn,
			    bufferIn);
		    matrixStackIn.pop();
		}
	    }
	}
    }
}