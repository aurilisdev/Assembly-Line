package assemblyline.common.inventory.container;

import assemblyline.common.tile.belt.TileSorterBelt;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import voltaic.prefab.inventory.container.slot.item.SlotGeneric;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;

public class ContainerSorterBelt extends GenericContainerBlockEntity<TileSorterBelt> {

	public ContainerSorterBelt(int id, PlayerInventory playerinv) {
		this(id, playerinv, new Inventory(19), new IntArray(5));
	}

	public ContainerSorterBelt(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotGeneric(inv, nextIndex() + 1, 8 + j * 18, 17 + i * 18));
			}
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotGeneric(inv, nextIndex() + 1, 18 * 3 + 62 + j * 18, 17 + i * 18));
			}
		}
	}
}
