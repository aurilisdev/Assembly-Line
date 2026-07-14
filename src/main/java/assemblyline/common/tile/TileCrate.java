package assemblyline.common.tile;

import java.util.HashSet;

import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.registers.AssemblyLineBlocks;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.items.CapabilityItemHandler;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentPacketHandler;
import voltaic.prefab.tile.components.type.ComponentTickable;

public class TileCrate extends GenericTile {
	
	public int size;

	public TileCrate() {
		super(AssemblyLineTiles.TILE_CRATE.get());
		
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().forceSize(this.size)).getSlots(this::getSlotsForFace).valid(this::isItemValidForSlot).setSlotsForAllDirections(0));
		addComponent(new ComponentTickable(this));
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		int size = 64;

		if(getBlockState().is(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.crate))) {
			size = 64;
		} else if (getBlockState().is(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratemedium))) {
			size = 128;
		} else if (getBlockState().is(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratelarge))) {
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
		return (int) ((double) getCount() / (double) Math.max(1, inv.getContainerSize()) * 15.0);
	}

	@Override
	public ActionResultType use(PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		if (!player.isShiftKeyDown() && !level.isClientSide) {
			player.setItemInHand(hand, HopperTileEntity.addItem(player.inventory, getComponent(IComponentType.Inventory), player.getItemInHand(hand), Direction.EAST));
			return ActionResultType.CONSUME;
		}
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, null).resolve().get().extractItem(i, inv.getMaxStackSize(), level.isClientSide());
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
