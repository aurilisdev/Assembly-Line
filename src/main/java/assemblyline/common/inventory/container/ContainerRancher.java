package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileRancher;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerRancher extends GenericContainerBlockEntity<TileRancher> {

	public ContainerRancher(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(DeferredRegisters.CONTAINER_RANCHER.get(), id, playerinv, inventory, inventorydata);
	}
	
	public ContainerRancher(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(9), new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 3; ++j) {
	            this.addSlot(new SlotRestricted(inv, nextIndex(), 30 + j * 18, 17 + i * 18));
	         }
	      }
	}

}
