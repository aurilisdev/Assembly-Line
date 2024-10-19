package assemblyline.common.tile;

import java.util.List;

import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileMobGrinder extends TileFrontHarvester {

	public TileMobGrinder(BlockPos pos, BlockState state) {
		super(AssemblyLineBlockTypes.TILE_MOBGRINDER.get(), pos, state, Constants.MOBGRINDER_USAGE * 40, (int) ElectrodynamicsCapabilities.DEFAULT_VOLTAGE, "mobgrinder");
	}

	@Override
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

		if (electro.getJoulesStored() < Constants.MOBGRINDER_USAGE || !inv.areOutputsEmpty()) {
			return;
		}

		ticksSinceCheck.set(ticksSinceCheck.get() + 1);

		if (ticksSinceCheck.get() >= currentWaitTime.get()) {
			ticksSinceCheck.set(0);
		}

		if (ticksSinceCheck.get() != 0) {
			return;
		}

		checkArea = getAABB(width.get(), length.get(), height.get(), true, false, this);
		List<Entity> entities = level.getEntities(null, checkArea);
		
		for (Entity entity : entities) {
			
			if(electro.getJoulesStored() < Constants.MOBGRINDER_USAGE) {
				break;
			}
			
			if(entity instanceof Player) {
				continue;
			}
			
			electro.joules(electro.getJoulesStored() - Constants.MOBGRINDER_USAGE);
			
			entity.getCapability(ElectrodynamicsCapabilities.LOCATION_STORAGE_CAPABILITY).ifPresent(h -> h.setLocation(0, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()));
			
			entity.kill();
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
		return new ComponentInventory(harvester, InventoryBuilder.newInv().outputs(9).upgrades(3)).setDirectionsBySlot(0, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(1, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(2, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST)
				.setDirectionsBySlot(3, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(4, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(5, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(6, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST)
				.setDirectionsBySlot(7, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).setDirectionsBySlot(8, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST).validUpgrades(ContainerFrontHarvester.VALID_UPGRADES).valid(machineValidator());
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerFrontHarvester(id, player, getComponent(IComponentType.Inventory), getCoordsArray());
	}

	@Override
	public double getUsage() {
		return Constants.MOBGRINDER_USAGE;
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
