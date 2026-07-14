package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;

public class ContainerBlockBreaker extends GenericContainerBlockEntity<TileBlockBreaker> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = { SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.advancedspeed };

	public ContainerBlockBreaker(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(3), new SimpleContainerData(5));
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
