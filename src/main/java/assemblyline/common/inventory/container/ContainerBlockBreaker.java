package assemblyline.common.inventory.container;

import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.registers.AssemblyLineMenuTypes;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerBlockBreaker extends AbstractHarvesterContainer {

	public ContainerBlockBreaker(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(3), new SimpleContainerData(3));
	}

	public ContainerBlockBreaker(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54));
	}

}
