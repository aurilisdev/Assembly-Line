package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileElevatorBelt;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class RenderElevatorBelt extends TileEntityRenderer<TileElevatorBelt> {

    public RenderElevatorBelt(TileEntityRendererDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    public void render(TileElevatorBelt tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.push();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	BlockPos pos = tile.getPos();
	World world = tile.getWorld();
	boolean down = !(world.getTileEntity(pos.offset(Direction.DOWN)) instanceof TileElevatorBelt);
	boolean running = tile.currentSpread > 0;
	IBakedModel model = running ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORRUNNING)
		: Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATOR);
	if (down) {
	    model = running ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORBOTTOMRUNNING)
		    : Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_ELEVATORBOTTOM);
	}
	UtilitiesRendering.renderModel(model, tile, RenderType.getSolid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	matrixStackIn.pop();
	ComponentInventory inv = tile.getComponent(ComponentType.Inventory);

	int totalSlotsUsed = 0;
	for (int i = 0; i < inv.getSizeInventory(); i++) {
	    ItemStack stack = inv.getStackInSlot(i);
	    if (!stack.isEmpty()) {
		totalSlotsUsed++;
	    }
	}
	double progressModifier = (tile.progress + 8.0 + (tile.currentSpread <= 0 || tile.halted ? 0 : partialTicks)) / 16.0;
	TileEntity next = world.getTileEntity(pos.up());
	if (!(next instanceof TileConveyorBelt)) {
	    progressModifier -= 0.5;
	}
	if (totalSlotsUsed > 0) {
	    for (int i = 0; i < inv.getSizeInventory(); i++) {
		ItemStack stack = inv.getStackInSlot(i);
		if (totalSlotsUsed == 1) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0.5, progressModifier, 0.5);
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