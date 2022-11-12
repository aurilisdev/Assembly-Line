package assemblyline.common.tile;

import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
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
	}

	@Override
	public void tickServer(ComponentTickable tickable) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		// ignore dims; for rendering purposes
		currentLength = DEFAULT_CHECK_LENGTH;
		currentWidth = DEFAULT_CHECK_WIDTH;
		currentHeight = 2;
		// we can add speed upgrade functionality if you want
		currentWaitTime = 20;

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
		if (tickable.getTicks() % 20 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		if (!inv.areInputsEmpty() && electro.getJoulesStored() >= Constants.BLOCKPLACER_USAGE) {
			if (ticksSinceCheck == 0) {
				ComponentDirection dir = getComponent(ComponentType.Direction);
				BlockPos off = worldPosition.offset(dir.getDirection().getOpposite().getNormal());
				BlockState state = level.getBlockState(off);
				electro.extractPower(TransferPack.joulesVoltage(Constants.BLOCKBREAKER_USAGE, ElectrodynamicsCapabilities.DEFAULT_VOLTAGE), false);
				if (state.isAir()) {
					ItemStack stack = inv.getItem(0);
					if (!stack.isEmpty() && stack.getItem() instanceof BlockItem bi) {
						Block b = bi.getBlock();
						BlockState newState = b.getStateForPlacement(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, stack, new BlockHitResult(Vec3.ZERO, dir.getDirection(), off, false)));
						if (newState.canSurvive(level, off)) {
							level.setBlockAndUpdate(off, newState);
							stack.shrink(1);
						}
					}
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
		return new ComponentInventory(harvester).size(4).inputs(1).upgrades(3).validUpgrades(ContainerBlockPlacer.VALID_UPGRADES).valid(machineValidator());
	}

	@Override
	public AbstractHarvesterContainer getContainer(int id, Inventory player) {
		return new ContainerBlockPlacer(id, player, getComponent(ComponentType.Inventory), getCoordsArray());
	}

	@Override
	public double getUsage() {
		return Constants.BLOCKPLACER_USAGE;
	}

}
