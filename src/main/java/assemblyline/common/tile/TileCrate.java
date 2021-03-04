package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.ITickableTileBase;
import electrodynamics.common.tile.generic.GenericTileInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileCrate extends GenericTileInventory implements ITickableTileBase {
    private int count = 0;

    public TileCrate() {
	super(DeferredRegisters.TILE_CRATE.get());
    }

    @Override
    public int getSizeInventory() {
	return 64;
    }

    @Override
    public int getInventoryStackLimit() {
	return 4096;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
	int[] arr = new int[64];
	for (int i = 0; i < arr.length; i++) {
	    arr[i] = i;
	}
	return arr;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
	if (stack.isEmpty()) {
	    return true;
	}
	for (int i = 0; i < 64; i++) {
	    ItemStack s = getStackInSlot(i);
	    if (s.isEmpty()) {
		continue;
	    }
	    if (stack.getItem() != s.getItem()) {
		return false;
	    }
	}
	return super.isItemValidForSlot(index, stack);
    }

    @Override
    public CompoundNBT writeCustomPacket() {
	CompoundNBT nbt = super.writeCustomPacket();
	ItemStack stack = ItemStack.EMPTY;
	for (int i = 0; i < 64; i++) {
	    if (!getStackInSlot(i).isEmpty()) {
		stack = getStackInSlot(i);
		break;
	    }
	}
	new ItemStack(stack.getItem()).write(nbt);
	nbt.putInt("acccount", getCount());
	return nbt;
    }

    @Override
    public void readCustomPacket(CompoundNBT nbt) {
	super.readCustomPacket(nbt);
	setInventorySlotContents(0, ItemStack.read(nbt));
	count = nbt.getInt("acccount");
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
	super.setInventorySlotContents(index, stack);
	sendCustomPacket();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
	return null;
    }

    @Override
    public void tickServer() {
	if (world.getWorldInfo().getGameTime() % 40 == 0) {
	    sendCustomPacket();
	}
    }

    public int getCount() {
	if (!world.isRemote) {
	    count = 0;
	    for (int i = 0; i < 64; i++) {
		ItemStack stack = getStackInSlot(i);
		if (!stack.isEmpty()) {
		    count += stack.getCount();
		}
	    }
	}
	return count;
    }

}
