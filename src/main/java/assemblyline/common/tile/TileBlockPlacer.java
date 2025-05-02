package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineTiles;
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
import voltaic.common.item.ItemUpgrade;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentContainerProvider;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentPacketHandler;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.registers.VoltaicCapabilities;

public class TileBlockPlacer extends TileOutlineArea {

    public SingleProperty<Integer> ticksSinceCheck = property(new SingleProperty<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
    public SingleProperty<Integer> currentWaitTime = property(new SingleProperty<>(PropertyTypes.INTEGER, "currentWaitTime", 0));

    public TileBlockPlacer(BlockPos pos, BlockState state) {
        super(AssemblyLineTiles.TILE_BLOCKPLACER.get(), pos, state);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(VoltaicCapabilities.DEFAULT_VOLTAGE).maxJoules(AssemblyLineConstants.BLOCKPLACER_USAGE * 2));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(1).upgrades(3))
                //
                .setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).validUpgrades(ContainerBlockPlacer.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentContainerProvider("blockplacer", this).createMenu((id, player) -> new ContainerBlockPlacer(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentForgeEnergy(this));
        height.setValue(1);
    }

    public void tickServer(ComponentTickable tickable) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        // we can add speed upgrade functionality if you want
        currentWaitTime.setValue(20);

        for (ItemStack stack : inv.getUpgradeContents()) {
            if (!stack.isEmpty()) {
                ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                switch (upgrade.subtype) {
                    case iteminput:
                        upgrade.subtype.applyUpgrade.accept(this, stack, 0);
                        break;
                    default:
                        break;
                }
            }
        }


        if (electro.getJoulesStored() < AssemblyLineConstants.BLOCKPLACER_USAGE || inv.areInputsEmpty()) {
            return;
        }

        ticksSinceCheck.setValue(ticksSinceCheck.getValue() + 1);

        if (ticksSinceCheck.getValue() >= currentWaitTime.getValue()) {
            ticksSinceCheck.setValue(0);
        }

        if (ticksSinceCheck.getValue() != 0) {
            return;
        }

        Direction facing = getFacing();
        BlockPos off = worldPosition.offset(facing.getOpposite().getNormal());
        BlockState state = level.getBlockState(off);
        electro.setJoulesStored(electro.getJoulesStored() - AssemblyLineConstants.BLOCKBREAKER_USAGE);
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
