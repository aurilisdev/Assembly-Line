package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.SlotGeneric;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.utilities.math.Color;

public class ContainerBlockPlacer extends GenericContainerBlockEntity<TileBlockPlacer> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = { SubtypeItemUpgrade.iteminput };

	public ContainerBlockPlacer(int id, PlayerInventory playerinv) {
		this(id, playerinv, new Inventory(4), new IntArray(5));
	}

	public ContainerBlockPlacer(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_BLOCKPLACER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
		addSlot(new SlotGeneric(inv, nextIndex(), 103, 35).setIOColor(new Color(0, 240, 255, 255)));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
	}
}
