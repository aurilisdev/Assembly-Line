package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;

public class ContainerBlockBreaker extends GenericContainerBlockEntity<TileBlockBreaker> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = { SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.advancedspeed };

	public ContainerBlockBreaker(int id, PlayerInventory playerinv) {
		this(id, playerinv, new Inventory(3), new IntArray(5));
	}

	public ContainerBlockBreaker(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
		super(AssemblyLineMenuTypes.CONTAINER_BLOCKBREAKER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54));
	}

}
