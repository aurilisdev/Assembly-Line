package assemblyline.common.inventory.container;

import assemblyline.common.tile.belt.TileSorterBelt;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import voltaic.prefab.inventory.container.slot.item.SlotGeneric;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;

public class ContainerSorterBelt extends GenericContainerBlockEntity<TileSorterBelt> {

    public ContainerSorterBelt(int id, Inventory playerinv) {
	this(id, playerinv, new SimpleContainer(19), new SimpleContainerData(3));
    }

    public ContainerSorterBelt(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
	super(AssemblyLineMenuTypes.CONTAINER_SORTERBELT.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
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
