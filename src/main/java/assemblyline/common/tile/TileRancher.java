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
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
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
		if (tickable.getTicks() % 20 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		currentWaitTime = DEFAULT_WAIT_TICKS;
		currentWidth = DEFAULT_CHECK_WIDTH;
		currentLength = DEFAULT_CHECK_LENGTH;
		currentHeight = DEFAULT_CHECK_HEIGHT;
		powerUsageMultiplier = 1;
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty()) {
				ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
				switch (upgrade.subtype) {
				case advancedspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime = Math.max(currentWaitTime / 3, FASTEST_WAIT_TICKS);
						powerUsageMultiplier *= 1.5;
					}
					break;
				case basicspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime = (int) Math.max(currentWaitTime / 1.25, FASTEST_WAIT_TICKS);
						powerUsageMultiplier *= 1.5;
					}
					break;
				case range:
					for (int i = 0; i < stack.getCount(); i++) {
						currentLength = Math.min(currentLength + 2, MAX_CHECK_LENGTH);
						currentWidth = Math.min(currentWidth + 2, MAX_CHECK_WIDTH);
						powerUsageMultiplier *= 1.3;
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
			if (ticksSinceCheck == 0) {
				checkArea = getAABB(currentWidth, currentLength, currentHeight, true, false, this);
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

			ticksSinceCheck++;
			if (ticksSinceCheck >= currentWaitTime) {
				ticksSinceCheck = 0;
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
