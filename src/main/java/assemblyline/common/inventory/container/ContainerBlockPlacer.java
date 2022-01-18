package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerBlockPlacer extends AbstractHarvesterContainer {

	public ContainerBlockPlacer(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(4), new SimpleContainerData(3));
	}

	public ContainerBlockPlacer(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(DeferredRegisters.CONTAINER_BLOCKPLACER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotGeneric(inv, nextIndex(), 103, 35));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, SubtypeItemUpgrade.iteminput));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, SubtypeItemUpgrade.iteminput));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, SubtypeItemUpgrade.iteminput));
	}
}
