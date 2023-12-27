package assemblyline.common.tile;

import java.util.HashSet;

import assemblyline.registers.AssemblyLineBlockTypes;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileCrate extends GenericTile {

	public int size = 64;

	public TileCrate() {
		super(AssemblyLineBlockTypes.TILE_CRATE.get());

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().forceSize(this.size)).getSlots(this::getSlotsForFace).valid(this::isItemValidForSlot).setSlotsForAllDirections(0));
		addComponent(new ComponentTickable(this));
	}

	@Override
	public void onLoad() {
		super.onLoad();
		int size = 64;

		if (getBlockState().is(AssemblyLineBlocks.blockCrate)) {
			size = 64;
		} else if (getBlockState().is(AssemblyLineBlocks.blockCrateMedium)) {
			size = 128;
		} else if (getBlockState().is(AssemblyLineBlocks.blockCrateLarge)) {
			size = 256;
		}

		this.size = size;
	}

	public HashSet<Integer> getSlotsForFace(Direction side) {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < this.<ComponentInventory>getComponent(IComponentType.Inventory).getContainerSize(); i++) {
			set.add(i);
		}
		return set;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack, ComponentInventory inv) {
		if (stack.isEmpty()) {
			return true;
		}
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack s = inv.getItem(i);
			if (s.isEmpty()) {
				continue;
			}
			if (stack.getItem() != s.getItem()) {
				return false;
			}
		}
		return true;
	}

	public int getCount() {
		int count = 0;
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		count = 0;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				count += stack.getCount();
			}
		}

		return count;
	}

	@Override
	public int getComparatorSignal() {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		return (int) (((double) getCount() / (double) Math.max(1, inv.getContainerSize())) * 15.0);
	}

	@Override
	public ActionResultType use(PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		if (!player.isShiftKeyDown()) {
			player.setItemInHand(hand, HopperTileEntity.addItem(player.inventory, getComponent(IComponentType.Inventory), player.getItemInHand(hand), Direction.EAST));
			return ActionResultType.CONSUME;
		}
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).resolve().get().extractItem(i, inv.getMaxStackSize(), level.isClientSide());
			if (!stack.isEmpty()) {
				if (!level.isClientSide()) {
					ItemEntity item = new ItemEntity(level, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5, stack);
					level.addFreshEntity(item);
				}
				return ActionResultType.CONSUME;
			}
		}
		return ActionResultType.FAIL;
	}

}