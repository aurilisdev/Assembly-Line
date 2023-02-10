package assemblyline.common.tile;

import java.util.HashSet;

import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileCrate extends GenericTile {

	public TileCrate(BlockPos worldPosition, BlockState blockState) {
		this(64, worldPosition, blockState);
	}

	public TileCrate(int size, BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineBlockTypes.TILE_CRATE.get(), worldPosition, blockState);
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentInventory(this).size(size).getSlots(this::getSlotsForFace).valid(this::isItemValidForSlot).slotFaces(0, Direction.values()));
		addComponent(new ComponentTickable());
	}

	public HashSet<Integer> getSlotsForFace(Direction side) {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < this.<ComponentInventory>getComponent(ComponentType.Inventory).getContainerSize(); i++) {
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
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		count = 0;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				count += stack.getCount();
			}
		}

		return count;
	}

}
