package assemblyline.common.tile;

import java.util.HashSet;

import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

//TODO: Make this use the property system...
public class TileCrate extends GenericTile {
	private int lastCheckCount = 0;
	private int count = 0;
	private boolean updatingWithScheduler = false;

	public TileCrate(BlockPos worldPosition, BlockState blockState) {
		this(64, worldPosition, blockState);
	}

	public TileCrate(int size, BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineBlockTypes.TILE_CRATE.get(), worldPosition, blockState);
		addComponent(new ComponentPacketHandler().addGuiPacketWriter(this::writeCustomPacket).addGuiPacketReader(this::readCustomPacket).addCustomPacketReader(this::readCustomPacket).addCustomPacketWriter(this::writeCustomPacket));
		addComponent(new ComponentInventory(this).size(size).getSlots(this::getSlotsForFace).valid(this::isItemValidForSlot).slotFaces(0, Direction.values()));
		addComponent(new ComponentTickable().tickServer(this::tickServer));
	}

	public HashSet<Integer> getSlotsForFace(Direction side) {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < this.<ComponentInventory>getComponent(ComponentType.Inventory).getContainerSize(); i++) {
			set.add(i);
		}
		if (!updatingWithScheduler) {
			updatingWithScheduler = true;
			Scheduler.schedule(1, () -> {
				this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
				updatingWithScheduler = false;
			});
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

	public void writeCustomPacket(CompoundTag nbt) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ItemStack stack = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (!inv.getItem(i).isEmpty()) {
				stack = inv.getItem(i);
				break;
			}
		}
		new ItemStack(stack.getItem()).save(nbt);
		nbt.putInt("acccount", getCount());
	}

	public void readCustomPacket(CompoundTag nbt) {
		this.<ComponentInventory>getComponent(ComponentType.Inventory).setItem(0, ItemStack.of(nbt));
		count = nbt.getInt("acccount");
	}

	public void tickServer(ComponentTickable tickable) {
		if (tickable.getTicks() % 40 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
			count = lastCheckCount;
		}
	}

	public int getCount() {
		if (!level.isClientSide) {
			ComponentInventory inv = getComponent(ComponentType.Inventory);
			count = 0;
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack stack = inv.getItem(i);
				if (!stack.isEmpty()) {
					count += stack.getCount();
				}
			}
		}
		return count;
	}

}
