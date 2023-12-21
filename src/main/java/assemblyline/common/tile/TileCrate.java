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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileCrate extends GenericTile {

	public final int size;

	public TileCrate(BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineBlockTypes.TILE_CRATE.get(), worldPosition, blockState);
		
		//TODO unique tiles
		
		int size = 64;
		
		if(blockState.is(AssemblyLineBlocks.blockCrate)) {
			size = 64;
		} else if (blockState.is(AssemblyLineBlocks.blockCrateMedium)) {
			size = 128;
		} else if (blockState.is(AssemblyLineBlocks.blockCrateLarge)) {
			size = 256;
		}
		
		this.size = size;
		
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().forceSize(this.size)).getSlots(this::getSlotsForFace).valid(this::isItemValidForSlot).setSlotsForAllDirections(0));
		addComponent(new ComponentTickable(this));
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
	public InteractionResult use(Player player, InteractionHand hand, BlockHitResult result) {
		if (!player.isShiftKeyDown()) {
			player.setItemInHand(hand, HopperBlockEntity.addItem(player.getInventory(), getComponent(IComponentType.Inventory), player.getItemInHand(hand), Direction.EAST));
			return InteractionResult.CONSUME;
		}
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).resolve().get().extractItem(i, inv.getMaxStackSize(), level.isClientSide());
			if (!stack.isEmpty()) {
				if (!level.isClientSide()) {
					ItemEntity item = new ItemEntity(level, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5, stack);
					level.addFreshEntity(item);
				}
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.FAIL;
	}

}