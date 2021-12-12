package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.inventory.container.slot.GenericSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerBlockPlacer extends GenericContainer<TileSorterBelt> {

    public ContainerBlockPlacer(int id, Inventory playerinv) {
	this(id, playerinv, new SimpleContainer(1));
    }

    public ContainerBlockPlacer(int id, Inventory playerinv, Container inventory) {
	this(id, playerinv, inventory, new SimpleContainerData(3));
    }

    public ContainerBlockPlacer(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
	super(DeferredRegisters.CONTAINER_BLOCKPLACER.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
	addSlot(new GenericSlot(inv, nextIndex(), 8 + 3 * 18, 17 + 18));
    }
}
