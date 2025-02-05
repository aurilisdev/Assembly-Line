package assemblyline.common.tile;

import java.util.List;

import assemblyline.common.inventory.container.ContainerMobGrinder;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineAttachmentTypes;
import assemblyline.registers.AssemblyLineTiles;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileMobGrinder extends TileOutlineArea {

    public static final int DEFAULT_WAIT_TICKS = 600;
    public static final int FASTEST_WAIT_TICKS = 60;

    public Property<Double> powerUsageMultiplier = property(new Property<>(PropertyTypes.DOUBLE, "powerUsageMultiplier", 1.0));
    public Property<Integer> ticksSinceCheck = property(new Property<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
    public Property<Integer> currentWaitTime = property(new Property<>(PropertyTypes.INTEGER, "currentWaitTime", 0));

    public TileMobGrinder(BlockPos pos, BlockState state) {
        super(AssemblyLineTiles.TILE_MOBGRINDER.get(), pos, state);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.MOBGRINDER_USAGE * 40));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().outputs(9).upgrades(3))
                //
                .setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(1, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(2, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(3, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(4, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(5, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(6, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(7, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
                //
                .setDirectionsBySlot(8, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).validUpgrades(ContainerMobGrinder.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentContainerProvider("container.mobgrinder", this).createMenu((id, player) -> new ContainerMobGrinder(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    public void tickServer(ComponentTickable tickable) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);

        for (ItemStack stack : inv.getUpgradeContents()) {
            if (!stack.isEmpty()) {
                ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                if (upgrade.subtype == SubtypeItemUpgrade.itemoutput) {
                    upgrade.subtype.applyUpgrade.accept(this, null, stack);
                }
            }
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < Constants.MOBGRINDER_USAGE * powerUsageMultiplier.get() || !inv.areOutputsEmpty()) {
            return;
        }

        ticksSinceCheck.set(ticksSinceCheck.get() + 1);

        if (ticksSinceCheck.get() >= currentWaitTime.get()) {
            ticksSinceCheck.set(0);
        }

        if (ticksSinceCheck.get() != 0) {
            return;
        }

        checkArea = getAABB(width.get(), length.get(), height.get(), true).inflate(1);
        List<Entity> entities = level.getEntities(null, checkArea);

        for (Entity entity : entities) {

            if (electro.getJoulesStored() < Constants.MOBGRINDER_USAGE) {
                break;
            }

            if (entity instanceof Player) {
                continue;
            }

            electro.joules(electro.getJoulesStored() - Constants.MOBGRINDER_USAGE);

            entity.setData(AssemblyLineAttachmentTypes.GRINDER_KILLED_MOB, getBlockPos());

            entity.kill();
        }

    }

    @Override
    public void onInventoryChange(ComponentInventory inv, int slot) {
        super.onInventoryChange(inv, slot);

        if (slot == -1 || slot >= inv.getUpgradeSlotStartIndex()) {
            int waitTime = DEFAULT_WAIT_TICKS;
            int newWidth = DEFAULT_CHECK_WIDTH;
            int newLength = DEFAULT_CHECK_LENGTH;
            int newHeight = DEFAULT_CHECK_HEIGHT;
            double powerMultiplier = 1.0;
            for (ItemStack stack : inv.getUpgradeContents()) {
                if (!stack.isEmpty()) {
                    ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                    switch (upgrade.subtype) {
                        case advancedspeed:
                            for (int i = 0; i < stack.getCount(); i++) {
                                waitTime = Math.max(waitTime / 3, FASTEST_WAIT_TICKS);
                                powerMultiplier *= 1.5;
                            }
                            break;
                        case basicspeed:
                            for (int i = 0; i < stack.getCount(); i++) {
                                waitTime = (int) Math.max(waitTime / 1.25, FASTEST_WAIT_TICKS);
                                powerMultiplier *= 1.5;
                            }
                            break;
                        case range:
                            for (int i = 0; i < stack.getCount(); i++) {
                                newLength = Math.min(newLength + 2, MAX_CHECK_LENGTH);
                                newWidth = Math.min(newWidth + 2, MAX_CHECK_WIDTH);
                                powerMultiplier *= 1.3;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            currentWaitTime.set(waitTime);
            width.set(newWidth);
            length.set(newLength);
            height.set(newHeight);
            powerUsageMultiplier.set(powerMultiplier);
        }

    }

}
