package assemblyline.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class RenderConveyorBelt extends TileEntityRenderer<TileConveyorBelt> {

    public RenderConveyorBelt(TileEntityRendererDispatcher rendererDispatcherIn) {
	super(rendererDispatcherIn);
    }

    @Override
    public void render(TileConveyorBelt tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.push();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	IBakedModel model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYOR);
	ComponentDirection direction = tile.getComponent(ComponentType.Direction);
	BlockPos pos = tile.getPos();
	World world = tile.getWorld();
	boolean isSloped = false;
	if (world.getTileEntity(pos.offset(Direction.UP).offset(direction.getDirection().getOpposite())) instanceof TileConveyorBelt
		|| world.getTileEntity(pos.offset(Direction.UP).offset(direction.getDirection())) instanceof TileConveyorBelt) {
	    isSloped = true;
	}
	boolean up = world.getTileEntity(pos.offset(Direction.UP).offset(direction.getDirection().getOpposite())) instanceof TileConveyorBelt;
	boolean running = tile.currentSpread > 0;
	if (tile.isManipulator) {
	    if (tile.isManipulatorOutput) {
		if (running) {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATOROUTPUTRUNNING);
		} else {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATOROUTPUT);
		}
		matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
	    } else {
		if (running) {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATORINPUTRUNNING);
		} else {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATORINPUT);
		}
	    }
	} else {
	    if (isSloped) {
		if (up) {
		    if (running) {
			model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYORANIMATED);
		    } else {
			model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYOR);
		    }
		} else {
		    if (running) {
			model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYORDOWNANIMATED);
		    } else {
			model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_SLOPEDCONVEYORDOWN);
		    }
		}
	    } else if (running) {
		model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATED);
	    }
	}
	Direction dir = direction.getDirection();
	if (model == Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYOR)
		|| model == Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATED)) {
	    TileEntity right = world.getTileEntity(pos.offset(dir.getOpposite().rotateY()));
	    TileEntity left = world.getTileEntity(pos.offset(dir.getOpposite().rotateYCCW()));
	    boolean rightClear = right instanceof TileConveyorBelt || right instanceof TileSorterBelt;
	    boolean leftClear = left instanceof TileConveyorBelt || left instanceof TileSorterBelt;
	    if (rightClear || leftClear) {
		if (model == Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYOR)) {
		    model = rightClear && leftClear ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORCLEAR)
			    : rightClear ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORRIGHTCLEAR)
				    : Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORLEFTCLEAR);
		} else if (model == Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATED)) {
		    model = rightClear && leftClear ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATEDCLEAR)
			    : rightClear ? Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATEDRIGHTCLEAR)
				    : Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYORANIMATEDLEFTCLEAR);
		}
	    }
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
	TileEntity next = world.getTileEntity(pos.offset(dir.getOpposite()));
	if (next instanceof TileConveyorBelt) {
	    TileConveyorBelt conv = (TileConveyorBelt) next;
	    Direction direct = conv.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	    boolean nextSloped = false;
	    if (world.getTileEntity(pos.offset(direct.getOpposite()).offset(Direction.UP).offset(direct.getOpposite())) instanceof TileConveyorBelt
		    || world.getTileEntity(pos.offset(direct.getOpposite()).offset(Direction.UP).offset(direct)) instanceof TileConveyorBelt) {
		nextSloped = true;
	    }
	    if (nextSloped) {
		progressModifier = MathHelper.clamp(progressModifier, 0.0, 1.0);
	    }
	    if (isSloped) {
		progressModifier -= 0.5;
	    }
	} else {
	    boolean shouldBeNormal = isSloped || !(world.getTileEntity(pos.offset(dir)) instanceof TileConveyorBelt);
	    if (shouldBeNormal) {
		progressModifier -= 0.5;
	    }
	    if (tile.isManipulator) {
		progressModifier = MathHelper.clamp(progressModifier, 0.0, 1.0);
	    }
	}
	if (totalSlotsUsed > 0) {
	    for (int i = 0; i < inv.getSizeInventory(); i++) {
		ItemStack stack = inv.getStackInSlot(i);
		if (totalSlotsUsed == 1) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0, (stack.getItem() instanceof BlockItem ? 0.48 : 0.33)
			    + (isSloped ? up ? progressModifier : progressModifier - 1 : 0) * (up ? 1 : -1) + (isSloped ? 1 / 16.0 : 0), 0);
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
		    if (isSloped) {
			int rotate = 45;
			if (up) {
			    rotate = -45;
			}
			matrixStackIn.rotate(dir == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate)
				: dir == Direction.SOUTH ? Vector3f.XP.rotationDegrees(-rotate)
					: dir == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) : Vector3f.XP.rotationDegrees(-rotate));
		    }
		    Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn,
			    bufferIn);
		    matrixStackIn.pop();
		} else if (totalSlotsUsed == 2) {
		    matrixStackIn.push();
		    matrixStackIn.translate(0, (stack.getItem() instanceof BlockItem ? 0.48 : 0.33)
			    + (isSloped ? up ? progressModifier : progressModifier - 1 : 0) * (up ? 1 : -1) + (isSloped ? 1 / 16.0 : 0), 0);
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
		    if (isSloped) {
			int rotate = 45;
			if (up) {
			    rotate = -45;
			}
			matrixStackIn.rotate(dir == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate)
				: dir == Direction.SOUTH ? Vector3f.XP.rotationDegrees(-rotate)
					: dir == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) : Vector3f.XP.rotationDegrees(-rotate));
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