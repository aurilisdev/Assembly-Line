package assemblyline.common.inventory.container;

import assemblyline.DeferredRegisters;
import assemblyline.common.tile.TileAutocrafter;
import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.inventory.container.slot.GenericSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerAutocrafter extends GenericContainer<TileAutocrafter> {

	public ContainerAutocrafter(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(10));
	}

	public ContainerAutocrafter(int id, Inventory playerinv, Container inventory) {
		this(id, playerinv, inventory, new SimpleContainerData(0));
	}

	public ContainerAutocrafter(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(DeferredRegisters.CONTAINER_AUTOCRAFTER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new GenericSlot(inv, nextIndex(), 8 + j * 18, 17 + i * 18));
			}
		}
		addSlot(new FurnaceResultSlot(playerinv.player, inv, nextIndex(), 120, 35));
	}
}
