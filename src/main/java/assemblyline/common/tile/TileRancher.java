package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.List;

import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.InventoryUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;

public class TileRancher extends TileFrontHarvester {

	private static ItemStack SHEERS = new ItemStack(Items.SHEARS);

	public TileRancher(BlockPos pos, BlockState state) {
		super(AssemblyLineBlockTypes.TILE_RANCHER.get(), pos, state, Constants.RANCHER_USAGE * 20, (int) ElectrodynamicsCapabilities.DEFAULT_VOLTAGE, "rancher");
	}

	@Override
	public void tickServer(ComponentTickable tickable) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		currentWaitTime.set(DEFAULT_WAIT_TICKS);
		width.set(DEFAULT_CHECK_WIDTH, true);
		length.set(DEFAULT_CHECK_LENGTH, true);
		height.set(DEFAULT_CHECK_HEIGHT);
		powerUsageMultiplier.set(1.0, true);
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty()) {
				ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
				switch (upgrade.subtype) {
				case advancedspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime.set(Math.max(currentWaitTime.get() / 3, FASTEST_WAIT_TICKS));
						powerUsageMultiplier.set(powerUsageMultiplier.get() * 1.5);
					}
					break;
				case basicspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime.set((int) Math.max(currentWaitTime.get() / 1.25, FASTEST_WAIT_TICKS));
						powerUsageMultiplier.set(powerUsageMultiplier.get() * 1.5);
					}
					break;
				case range:
					for (int i = 0; i < stack.getCount(); i++) {
						length.set(Math.min(length.get() + 2, MAX_CHECK_LENGTH));
						width.set(Math.min(width.get() + 2, MAX_CHECK_WIDTH));
						powerUsageMultiplier.set(powerUsageMultiplier.get() * 1.3);
					}
					break;
				case itemoutput:
					upgrade.subtype.applyUpgrade.accept(this, null, stack);
					break;
				default:
					break;
				}
			}
		}
		if (inv.areOutputsEmpty() && electro.getJoulesStored() >= Constants.RANCHER_USAGE) {
			if (ticksSinceCheck.get() == 0) {
				checkArea = getAABB(width.get(), length.get(), height.get(), true, false, this);
				List<Entity> entities = level.getEntities(null, checkArea);
				List<ItemStack> collectedItems = new ArrayList<>();
				for (Entity entity : entities) {
					if (electro.getJoulesStored() >= Constants.RANCHER_USAGE && entity instanceof IForgeShearable sheep && sheep.isShearable(SHEERS, level, entity.blockPosition())) {
						collectedItems.addAll(sheep.onSheared(null, SHEERS, level, entity.blockPosition(), 0));
						electro.extractPower(TransferPack.joulesVoltage(Constants.RANCHER_USAGE, electro.getVoltage()), false);
					}
				}
				if (!collectedItems.isEmpty()) {
					InventoryUtils.addItemsToInventory(inv, collectedItems, inv.getOutputStartIndex(), inv.getOutputContents().size());
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
		return new ComponentInventory(this).size(12).outputs(9).upgrades(3).validUpgrades(ContainerFrontHarvester.VALID_UPGRADES).valid(machineValidator()).shouldSendInfo();
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerFrontHarvester(id, player, getComponent(ComponentType.Inventory), getCoordsArray());
	}

	@Override
	public double getUsage() {
		return Constants.RANCHER_USAGE;
	}

}
