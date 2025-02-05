package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineTiles;
import electrodynamics.common.item.ItemUpgrade;
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
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TileBlockPlacer extends TileOutlineArea {

    public Property<Integer> ticksSinceCheck = property(new Property<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
    public Property<Integer> currentWaitTime = property(new Property<>(PropertyTypes.INTEGER, "currentWaitTime", 0));

    public TileBlockPlacer(BlockPos pos, BlockState state) {
        super(AssemblyLineTiles.TILE_BLOCKPLACER.get(), pos, state);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.BLOCKPLACER_USAGE * 2));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(1).upgrades(3))
                //
                .setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).validUpgrades(ContainerBlockPlacer.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentContainerProvider("container.blockplacer", this).createMenu((id, player) -> new ContainerBlockPlacer(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        height.set(1);
    }

    public void tickServer(ComponentTickable tickable) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        // we can add speed upgrade functionality if you want
        currentWaitTime.set(20);

        for (ItemStack stack : inv.getUpgradeContents()) {
            if (!stack.isEmpty()) {
                ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                switch (upgrade.subtype) {
                    case iteminput:
                        upgrade.subtype.applyUpgrade.accept(this, null, stack);
                        break;
                    default:
                        break;
                }
            }
        }


        if (electro.getJoulesStored() < Constants.BLOCKPLACER_USAGE || inv.areInputsEmpty()) {
            return;
        }

        ticksSinceCheck.set(ticksSinceCheck.get() + 1);

        if (ticksSinceCheck.get() >= currentWaitTime.get()) {
            ticksSinceCheck.set(0);
        }

        if (ticksSinceCheck.get() != 0) {
            return;
        }

        Direction facing = getFacing();
        BlockPos off = worldPosition.offset(facing.getOpposite().getNormal());
        BlockState state = level.getBlockState(off);
        electro.setJoulesStored(electro.getJoulesStored() - Constants.BLOCKBREAKER_USAGE);
        if (!state.isAir()) {
            return;
        }

        ItemStack stack = inv.getItem(0);

        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem bi) {
            Block b = bi.getBlock();
            BlockState newState = b.getStateForPlacement(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, stack, new BlockHitResult(Vec3.ZERO, facing, off, false)));
            if (newState.canSurvive(level, off)) {
                level.setBlockAndUpdate(off, newState);
                stack.shrink(1);
            }
        }


    }

}
