package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.SlotGeneric;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.utilities.math.Color;

public class ContainerBlockPlacer extends GenericContainerBlockEntity<TileBlockPlacer> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = { SubtypeItemUpgrade.iteminput };

	public ContainerBlockPlacer(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(4), new SimpleContainerData(5));
	}

	public ContainerBlockPlacer(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotGeneric(inv, nextIndex(), 103, 35).setIOColor(new Color(0, 240, 255, 255)));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
	}
}
