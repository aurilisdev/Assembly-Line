package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileAutocrafter;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import voltaic.prefab.inventory.container.slot.item.SlotGeneric;
import voltaic.prefab.inventory.container.slot.item.type.SlotRestricted;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.utilities.math.Color;

public class ContainerAutocrafter extends GenericContainerBlockEntity<TileAutocrafter> {

    public static final Color[] COLORS = {
	    //
	    new Color(50, 50, 50, 255),
	    //
	    new Color(255, 0, 0, 255),
	    //
	    new Color(120, 0, 255, 255),
	    //
	    new Color(0, 240, 0, 255),
	    //
	    new Color(220, 0, 255, 255),
	    //
	    new Color(255, 120, 0, 255),
	    //
	    new Color(0, 0, 255, 255),
	    //
	    new Color(240, 255, 0, 255),
	    //
	    new Color(0, 240, 255, 255)
//
    };

    public ContainerAutocrafter(int id, Inventory playerinv) {
	this(id, playerinv, new SimpleContainer(10), new SimpleContainerData(3));
    }

    public ContainerAutocrafter(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
	super(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
	int index = 0;
	for (int i = 0; i < 3; ++i) {
	    for (int j = 0; j < 3; ++j) {
		addSlot(new SlotGeneric(inv, nextIndex(), 8 + j * 18, 17 + i * 18).setIOColor(COLORS[index]));
		index++;
	    }
	}
	addSlot(new SlotRestricted(inv, nextIndex(), 120, 35).setIOColor(new Color(156, 255, 160, 255)));
    }
}
