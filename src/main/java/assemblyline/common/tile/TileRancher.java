package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.List;

import assemblyline.common.inventory.container.ContainerRancher;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.common.tile.util.TileOutlineArea;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IShearable;
import voltaic.common.item.ItemUpgrade;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentContainerProvider;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.prefab.utilities.ItemUtils;
import voltaic.registers.VoltaicCapabilities;

public class TileRancher extends TileOutlineArea {

	public static final int DEFAULT_WAIT_TICKS = 600;
	public static final int FASTEST_WAIT_TICKS = 60;

	public SingleProperty<Double> powerUsageMultiplier = property(new SingleProperty<>(PropertyTypes.DOUBLE, "powerUsageMultiplier", 1.0));
	public SingleProperty<Integer> ticksSinceCheck = property(new SingleProperty<>(PropertyTypes.INTEGER, "ticksSinceCheck", 0));
	public SingleProperty<Integer> currentWaitTime = property(new SingleProperty<>(PropertyTypes.INTEGER, "currentWaitTime", 0));

	public TileRancher(BlockPos pos, BlockState state) {
		super(AssemblyLineTiles.TILE_RANCHER.get(), pos, state);
		//addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(VoltaicCapabilities.DEFAULT_VOLTAGE).maxJoules(AssemblyLineConfig.INSTANCE.RANCHER_USAGE.getAsDouble()	 * 20));
		addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().outputs(9).upgrades(3))
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
				.setDirectionsBySlot(8, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).validUpgrades(ContainerRancher.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider("rancher", this).createMenu((id, player) -> new ContainerRancher(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
		addComponent(new ComponentForgeEnergy(this));
	}

	public void tickServer(ComponentTickable tickable) {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty()) {
				ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
				if (upgrade.subtype == SubtypeItemUpgrade.itemoutput) {
					upgrade.subtype.applyUpgrade.accept(this, stack, 0);
				}
			}
		}

		if (electro.getJoulesStored() < AssemblyLineConfig.INSTANCE.RANCHER_USAGE.getAsDouble() || !inv.areOutputsEmpty()) {
			return;
		}

		ticksSinceCheck.setValue(ticksSinceCheck.getValue() + 1);

		if (ticksSinceCheck.getValue() >= currentWaitTime.getValue()) {
			ticksSinceCheck.setValue(0);
		}

		if (ticksSinceCheck.getValue() != 0) {
			return;
		}

		checkArea = getAABB(width.getValue(), length.getValue(), height.getValue(), true);
		
		List<Entity> entities = level.getEntities(null, checkArea);
		
		List<ItemStack> collectedItems = new ArrayList<>();
		
		for (Entity entity : entities) {
			
			if(electro.getJoulesStored() < AssemblyLineConfig.INSTANCE.RANCHER_USAGE.getAsDouble()) {
				break;
			}
			
			if (entity instanceof IShearable sheep && sheep.isShearable(null, new ItemStack(Items.SHEARS), level, entity.blockPosition())) {
				
				collectedItems.addAll(sheep.onSheared(null, new ItemStack(Items.SHEARS), level, entity.blockPosition()));
				
				electro.joules(electro.getJoulesStored() - AssemblyLineConfig.INSTANCE.RANCHER_USAGE.getAsDouble());
				
			}
		}
		if (!collectedItems.isEmpty()) {
			int max = inv.getOutputStartIndex() + inv.getOutputContents().size();

			for(ItemStack item : collectedItems) {

				for (int i = inv.getOutputStartIndex(); i < max; i++) {

					ItemStack contained = inv.getItem(i);

					int room = contained.getMaxStackSize() - contained.getCount();

					int amtAccepted = Math.min(room, item.getCount());

					if(amtAccepted == 0) {
						continue;
					}

					if (contained.isEmpty()) {

						inv.setItem(i, new ItemStack(item.getItem(), amtAccepted));

						item.shrink(amtAccepted);

					} else if (ItemUtils.testItems(item.getItem(), contained.getItem())) {

						contained.grow(amtAccepted);

						item.shrink(amtAccepted);

						inv.setChanged();

					}

					if(item.isEmpty()) {
						break;
					}

				}

			}
		}
	}

	@Override
	public void onInventoryChange(ComponentInventory inv, int slot) {
		super.onInventoryChange(inv, slot);

		if (slot == -1 || slot >= inv.getUpgradeSlotStartIndex()) {
			int waitTime = DEFAULT_WAIT_TICKS;
			int newWidth = DEFAULT_CHECK_WIDTH;
			int newHeight = DEFAULT_CHECK_HEIGHT;
			int newLength = DEFAULT_CHECK_LENGTH;
			double powerUsage = 1.0;

			for (ItemStack stack : inv.getUpgradeContents()) {
				if (!stack.isEmpty()) {
					ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
					switch (upgrade.subtype) {
					case advancedspeed:
						for (int i = 0; i < stack.getCount(); i++) {
							waitTime = Math.max(waitTime / 3, FASTEST_WAIT_TICKS);
							powerUsage *= 1.5;
						}
						break;
					case basicspeed:
						for (int i = 0; i < stack.getCount(); i++) {
							waitTime = (int) Math.max(waitTime / 1.25, FASTEST_WAIT_TICKS);
							powerUsage *= 1.5;
						}
						break;
					case range:
						for (int i = 0; i < stack.getCount(); i++) {
							newLength = Math.min(newLength + 2, MAX_CHECK_LENGTH);
							newWidth = Math.min(newWidth + 2, MAX_CHECK_WIDTH);
							powerUsage *= 1.3;
						}
						break;
					default:
						break;
					}
				}
			}

			currentWaitTime.setValue(waitTime);
			width.setValue(newWidth);
			length.setValue(newLength);
			height.setValue(newHeight);
			powerUsageMultiplier.setValue(powerUsage);

		}
	}

}
