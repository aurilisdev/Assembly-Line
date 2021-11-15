package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RenderConveyorBelt implements BlockEntityRenderer<TileConveyorBelt> {

    public RenderConveyorBelt(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileConveyorBelt tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn,
	    int combinedOverlayIn) {
	matrixStackIn.pushPose();
	matrixStackIn.translate(0, 1 / 16.0, 0);
	UtilitiesRendering.prepareRotationalTileModel(tile, matrixStackIn);
	BakedModel model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_CONVEYOR);
	ComponentDirection direction = tile.getComponent(ComponentType.Direction);
	BlockPos pos = tile.getBlockPos();
	Level world = tile.getLevel();
	boolean isSloped = false;
	if (world.getBlockEntity(pos.relative(Direction.UP).relative(direction.getDirection().getOpposite())) instanceof TileConveyorBelt
		|| world.getBlockEntity(pos.relative(Direction.UP).relative(direction.getDirection())) instanceof TileConveyorBelt) {
	    isSloped = true;
	}
	boolean up = world.getBlockEntity(pos.relative(Direction.UP).relative(direction.getDirection().getOpposite())) instanceof TileConveyorBelt;
	boolean running = tile.currentSpread > 0;
	if (tile.isManipulator) {
	    if (tile.isManipulatorOutput) {
		if (running) {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATOROUTPUTRUNNING);
		} else {
		    model = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MANIPULATOROUTPUT);
		}
		matrixStackIn.mulPose(new Quaternion(0, 180, 0, true));
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
	    BlockEntity right = world.getBlockEntity(pos.relative(dir.getOpposite().getClockWise()));
	    BlockEntity rightdown = world.getBlockEntity(pos.relative(dir.getOpposite().getClockWise()).below());
	    BlockEntity left = world.getBlockEntity(pos.relative(dir.getOpposite().getCounterClockWise()));
	    BlockEntity leftdown = world.getBlockEntity(pos.relative(dir.getOpposite().getCounterClockWise()).below());
	    boolean rightClear = right instanceof TileConveyorBelt || right instanceof TileSorterBelt || rightdown instanceof TileConveyorBelt
		    || rightdown instanceof TileSorterBelt;
	    boolean leftClear = left instanceof TileConveyorBelt || left instanceof TileSorterBelt || leftdown instanceof TileConveyorBelt
		    || leftdown instanceof TileSorterBelt;
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
	BlockEntity next = world.getBlockEntity(pos.relative(dir.getOpposite()));
	if (next instanceof TileConveyorBelt conv) {
	    Direction direct = conv.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	    boolean nextSloped = false;
	    if (world.getBlockEntity(
		    pos.relative(direct.getOpposite()).relative(Direction.UP).relative(direct.getOpposite())) instanceof TileConveyorBelt
		    || world.getBlockEntity(pos.relative(direct.getOpposite()).relative(Direction.UP).relative(direct)) instanceof TileConveyorBelt) {
		nextSloped = true;
	    }
	    if (nextSloped) {
		progressModifier = Mth.clampedLerp(0.25f, 1f, progressModifier / 1.5f);
	    }
	    if (isSloped) {
		progressModifier -= 0.1;
	    }
	} else {
	    boolean shouldBeNormal = isSloped || !(world.getBlockEntity(pos.relative(dir)) instanceof TileConveyorBelt);
	    if (shouldBeNormal) {
		progressModifier -= 0.5;
	    }
	    if (tile.isManipulator) {
		progressModifier = Mth.clampedLerp(0.25f, 1f, progressModifier / 1.5f);
	    }
	}
	if (totalSlotsUsed > 0) {
	    for (int i = 0; i < inv.getContainerSize(); i++) {
		ItemStack stack = inv.getItem(i);
		if (totalSlotsUsed == 1) {
		    matrixStackIn.pushPose();
		    matrixStackIn.translate(0,
			    (stack.getItem() instanceof BlockItem ? 0.48 : 0.33)
				    + (isSloped ? up ? progressModifier : progressModifier - 1 : 0) * (up ? 1 : -1)
				    + (isSloped ? up ? 2.5 / 16.0 : 1 / 16.0 : 0),
			    0);
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate(0.5, 0, progressModifier);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.translate(1 - progressModifier, 0, 0.5);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.translate(progressModifier, 0, 0.5);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
		    } else if (dir == Direction.SOUTH) {
			matrixStackIn.translate(0.5, 0, 1 - progressModifier);
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
		    }
		    if (isSloped) {
			int rotate = 45;
			if (up) {
			    rotate = -45;
			}
			matrixStackIn.mulPose(dir == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate)
				: dir == Direction.SOUTH ? Vector3f.XP.rotationDegrees(-rotate)
					: dir == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) : Vector3f.XP.rotationDegrees(-rotate));
		    }
		    Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn,
			    matrixStackIn, bufferIn, 0);
		    matrixStackIn.popPose();
		} else if (totalSlotsUsed == 2) {
		    matrixStackIn.pushPose();
		    matrixStackIn.translate(0,
			    (stack.getItem() instanceof BlockItem ? 0.48 : 0.33)
				    + (isSloped ? up ? progressModifier : progressModifier - 1 : 0) * (up ? 1 : -1)
				    + (isSloped ? up ? 2.5 / 16.0 : 1 / 16.0 : 0),
			    0);
		    if (dir == Direction.NORTH) {
			matrixStackIn.translate(i == 0 ? 0.25 : 0.75, 0, progressModifier);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
		    } else if (dir == Direction.EAST) {
			matrixStackIn.translate(1 - progressModifier, 0, i == 0 ? 0.25 : 0.75);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
		    } else if (dir == Direction.WEST) {
			matrixStackIn.translate(progressModifier, 0, i == 0 ? 0.25 : 0.75);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
		    } else if (dir == Direction.SOUTH) {
			matrixStackIn.translate(i == 0 ? 0.25 : 0.75, 0, 1 - progressModifier);
		    }
		    if (isSloped) {
			int rotate = 45;
			if (up) {
			    rotate = -45;
			}
			matrixStackIn.mulPose(dir == Direction.NORTH ? Vector3f.XN.rotationDegrees(rotate)
				: dir == Direction.SOUTH ? Vector3f.XP.rotationDegrees(-rotate)
					: dir == Direction.WEST ? Vector3f.XN.rotationDegrees(rotate) : Vector3f.XP.rotationDegrees(-rotate));
		    }
		    matrixStackIn.scale(0.35f, 0.35f, 0.35f);
		    if (!(stack.getItem() instanceof BlockItem)) {
			matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
		    }
		    Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn,
			    matrixStackIn, bufferIn, 0);
		    matrixStackIn.popPose();
		}
	    }
	}
    }
}