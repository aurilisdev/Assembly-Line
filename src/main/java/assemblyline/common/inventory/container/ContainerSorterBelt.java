package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import electrodynamics.common.inventory.container.GenericContainerInventory;
import electrodynamics.common.inventory.container.slot.GenericSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ContainerSorterBelt extends GenericContainerInventory {

    public ContainerSorterBelt(int id, PlayerInventory playerinv) {
	this(id, playerinv, new Inventory(18));
    }

    public ContainerSorterBelt(int id, PlayerInventory playerinv, IInventory inventory) {
	this(id, playerinv, inventory, new IntArray(0));
    }

    public ContainerSorterBelt(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
	super(DeferredRegisters.CONTAINER_SORTERBELT.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
	for (int i = 0; i < 3; ++i) {
	    for (int j = 0; j < 3; ++j) {
		addSlot(new GenericSlot(inv, nextIndex(), 8 + j * 18, 17 + i * 18));
	    }
	}
	for (int i = 0; i < 3; ++i) {
	    for (int j = 0; j < 3; ++j) {
		addSlot(new GenericSlot(inv, nextIndex(), 18 * 3 + 62 + j * 18, 17 + i * 18));
	    }
	}
    }
}
