package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileSorterBelt;
import assemblyline.registers.AssemblyLineMenuTypes;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ContainerSorterBelt extends GenericContainerBlockEntity<TileSorterBelt> {

	public ContainerSorterBelt(int id, PlayerInventory playerinv) {
		this(id, playerinv, new Inventory(18), new IntArray(3));
	}

	public ContainerSorterBelt(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotGeneric(inv, nextIndex(), 8 + j * 18, 17 + i * 18));
			}
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotGeneric(inv, nextIndex(), 18 * 3 + 62 + j * 18, 17 + i * 18));
			}
		}
	}
}
