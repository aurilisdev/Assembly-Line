package assemblyline.tile;

import assemblyline.DeferredRegisters;
import assemblyline.block.BlockConveyorBelt;
import electrodynamics.api.tile.ITickableTileBase;
import electrodynamics.common.tile.generic.GenericTileBase;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileManipulator extends GenericTileBase implements ITickableTileBase {
	private long ticks = 0;

	public TileManipulator() {
		super(DeferredRegisters.TILE_MANIPULATOR.get());
	}

	@Override
	public void tickServer() {
		ticks++;
		if (ticks % 20 == 0) {
			Direction dir = getFacing().getOpposite();
			TileEntity facing = world.getTileEntity(pos.offset(dir));
			TileEntity opposite = world.getTileEntity(pos.offset(dir.getOpposite()));
			if (opposite instanceof TileConveyorBelt && opposite.getBlockState().get(BlockConveyorBelt.FACING).getOpposite() != dir) {
				if (facing instanceof IInventory) {
					IInventory inv = (IInventory) facing;
					if (inv instanceof ISidedInventory) {
						ISidedInventory sided = (ISidedInventory) inv;
						for (int slot : sided.getSlotsForFace(dir.getOpposite())) {
							ItemStack stack = inv.getStackInSlot(slot);
							if (!stack.isEmpty()) {
								if (sided.canExtractItem(slot, stack, dir.getOpposite())) {
									BlockPos offset = pos.offset(dir.getOpposite());
									ItemEntity entity = new ItemEntity(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
									entity.setMotion(0, 0, 0);
									entity.setItem(stack);
									world.addEntity(entity);
									inv.setInventorySlotContents(slot, ItemStack.EMPTY);
									break;
								}
							}
						}
					} else {
						for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
							ItemStack stack = inv.getStackInSlot(slot);
							if (!stack.isEmpty()) {
								BlockPos offset = pos.offset(dir.getOpposite());
								ItemEntity entity = new ItemEntity(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
								entity.setMotion(0, 0, 0);
								entity.setItem(stack);
								world.addEntity(entity);
								inv.setInventorySlotContents(slot, ItemStack.EMPTY);
								break;
							}
						}
					}
				}
			}
		}
	}

}
