package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileMobGrinder;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.type.SlotRestricted;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.utilities.math.Color;

public class ContainerMobGrinder extends GenericContainerBlockEntity<TileMobGrinder> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.itemoutput, SubtypeItemUpgrade.range };

    public ContainerMobGrinder(int id, PlayerInventory playerinv, IInventory inventory, IIntArray inventorydata) {
        super(AssemblyLineMenuTypes.CONTAINER_MOBGRINDER.get(), id, playerinv, inventory, inventorydata);
    }

    public ContainerMobGrinder(int id, PlayerInventory playerinv) {
        this(id, playerinv, new Inventory(12), new IntArray(5));
    }

    @Override
    public void addInventorySlots(IInventory inv, PlayerInventory playerinv) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new SlotRestricted(inv, nextIndex(), 85 + j * 18, 17 + i * 18).setIOColor(new Color(255, 0, 0, 255)));
            }
        }
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
    }
}
