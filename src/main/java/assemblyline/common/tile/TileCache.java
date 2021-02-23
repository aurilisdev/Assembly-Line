package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.common.tile.generic.GenericTileInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileCache extends GenericTileInventory {

	public TileCache() {
		super(DeferredRegisters.TILE_CACHE.get());
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
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		sendUpdatePacket();
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
	public CompoundNBT createUpdateTag() {
		CompoundNBT nbt = super.createUpdateTag();
		getStackInSlot(0).write(nbt);
		return nbt;
	}

	@Override
	public void handleUpdatePacket(CompoundNBT nbt) {
		super.handleUpdatePacket(nbt);
		ItemStack.read(nbt);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		sendUpdatePacket();
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return null;
	}

}
