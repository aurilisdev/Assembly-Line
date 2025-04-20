package assemblyline.common.inventory.container;

import assemblyline.common.tile.TileMobGrinder;
import assemblyline.registers.AssemblyLineMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.inventory.container.slot.item.type.SlotRestricted;
import voltaic.prefab.inventory.container.slot.item.type.SlotUpgrade;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.utilities.math.Color;

public class ContainerMobGrinder extends GenericContainerBlockEntity<TileMobGrinder> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.itemoutput, SubtypeItemUpgrade.range };

    public ContainerMobGrinder(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(AssemblyLineMenuTypes.CONTAINER_MOBGRINDER.get(), id, playerinv, inventory, inventorydata);
    }

    public ContainerMobGrinder(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(12), new SimpleContainerData(3));
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
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
