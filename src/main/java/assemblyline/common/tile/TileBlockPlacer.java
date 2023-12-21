package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TileBlockPlacer extends TileFrontHarvester {

	public TileBlockPlacer(BlockPos pos, BlockState state) {
		super(AssemblyLineBlockTypes.TILE_BLOCKPLACER.get(), pos, state, Constants.BLOCKPLACER_USAGE * 20, (int) ElectrodynamicsCapabilities.DEFAULT_VOLTAGE, "blockplacer");
		height.set(2);
	}

	@Override
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
		if (!inv.areInputsEmpty() && electro.getJoulesStored() >= Constants.BLOCKPLACER_USAGE) {
			if (ticksSinceCheck.get() == 0) {
				Direction facing = getFacing();
				BlockPos off = worldPosition.offset(facing.getOpposite().getNormal());
				BlockState state = level.getBlockState(off);
				electro.extractPower(TransferPack.joulesVoltage(Constants.BLOCKBREAKER_USAGE, ElectrodynamicsCapabilities.DEFAULT_VOLTAGE), false);
				if (state.isAir()) {
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
			ticksSinceCheck.set(ticksSinceCheck.get() + 1);
			if (ticksSinceCheck.get() >= currentWaitTime.get()) {
				ticksSinceCheck.set(0);
			}
		}
	}

	@Override
	public void tickClient(ComponentTickable tickable) {
	}

	@Override
	public void tickCommon(ComponentTickable tickable) {
	}

	@Override
	public ComponentInventory getInv(TileFrontHarvester harvester) {
		return new ComponentInventory(harvester, InventoryBuilder.newInv().inputs(1).upgrades(3)).setDirectionsBySlot(0, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).validUpgrades(ContainerBlockPlacer.VALID_UPGRADES).valid(machineValidator());
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerBlockPlacer(id, player, getComponent(IComponentType.Inventory), getCoordsArray());
	}

	@Override
	public double getUsage() {
		return Constants.BLOCKPLACER_USAGE;
	}

}