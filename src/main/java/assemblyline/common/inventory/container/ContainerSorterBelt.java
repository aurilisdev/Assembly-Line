package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerSorterBelt extends GenericContainerBlockEntity<TileSorterBelt> {

	public ContainerSorterBelt(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(18));
	}

	public ContainerSorterBelt(int id, Inventory playerinv, Container inventory) {
		this(id, playerinv, inventory, new SimpleContainerData(0));
	}

	public ContainerSorterBelt(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(DeferredRegisters.CONTAINER_SORTERBELT.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
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
