package assemblyline.common.inventory.container.generic;

import assemblyline.common.tile.generic.TileFrontHarvester;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

public abstract class AbstractHarvesterContainer extends GenericContainerBlockEntity<TileFrontHarvester> {

	public AbstractHarvesterContainer(MenuType<?> type, int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(type, id, playerinv, inventory, inventorydata);
	}

}
