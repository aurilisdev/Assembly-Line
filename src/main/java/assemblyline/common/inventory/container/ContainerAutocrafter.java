package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileAutocrafter;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
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

    public ContainerAutocrafter(int id, PlayerInventory playerinv) {
        this(id, playerinv, new Inventory(10), new IntArray(5));
    }

    public ContainerAutocrafter(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
        super(AssemblyLineMenuTypes.CONTAINER_AUTOCRAFTER.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
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
