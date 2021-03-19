package assemblyline.common.tile;

import java.util.HashSet;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.api.tile.components.type.ComponentPacketHandler;
import electrodynamics.api.tile.components.type.ComponentTickable;
import electrodynamics.api.utilities.Scheduler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileCrate extends GenericTileTicking {
    private int lastCheckCount = 0;
    private int count = 0;

    public TileCrate() {
	super(DeferredRegisters.TILE_CRATE.get());
	addComponent(new ComponentPacketHandler().addGuiPacketWriter(this::writeCustomPacket).addGuiPacketReader(this::readCustomPacket)
		.addCustomPacketReader(this::readCustomPacket).addCustomPacketWriter(this::writeCustomPacket));
	addComponent(new ComponentInventory().setInventorySize(64).setGetSlotsFunction(this::getSlotsForFace)
		.setItemValidPredicate(this::isItemValidForSlot).addSlotsOnFace(Direction.DOWN, 0).addSlotsOnFace(Direction.SOUTH, 0)
		.addSlotsOnFace(Direction.UP, 0).addSlotsOnFace(Direction.NORTH, 0).addSlotsOnFace(Direction.EAST, 0)
		.addSlotsOnFace(Direction.WEST, 0));
	addComponent(new ComponentTickable().addTickServer(this::tickServer));
    }

    public HashSet<Integer> getSlotsForFace(Direction side) {
	HashSet<Integer> set = new HashSet<>();
	for (int i = 0; i < this.<ComponentInventory>getComponent(ComponentType.Inventory).getSizeInventory(); i++) {
	    set.add(i);
	}
	Scheduler.schedule(1, () -> this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking());
	return set;
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
	ComponentInventory inv = getComponent(ComponentType.Inventory);
	if (stack.isEmpty()) {
	    return true;
	}
	for (int i = 0; i < inv.getSizeInventory(); i++) {
	    ItemStack s = inv.getStackInSlot(i);
	    if (s.isEmpty()) {
		continue;
	    }
	    if (stack.getItem() != s.getItem()) {
		return false;
	    }
	}
	return true;
    }

    public void writeCustomPacket(CompoundNBT nbt) {
	ComponentInventory inv = getComponent(ComponentType.Inventory);
	ItemStack stack = ItemStack.EMPTY;
	for (int i = 0; i < inv.getSizeInventory(); i++) {
	    if (!inv.getStackInSlot(i).isEmpty()) {
		stack = inv.getStackInSlot(i);
		break;
	    }
	}
	new ItemStack(stack.getItem()).write(nbt);
	nbt.putInt("acccount", getCount());
    }

    public void readCustomPacket(CompoundNBT nbt) {
	this.<ComponentInventory>getComponent(ComponentType.Inventory).setInventorySlotContents(0, ItemStack.read(nbt));
	count = nbt.getInt("acccount");
    }

    public void tickServer(ComponentTickable tickable) {
	if (tickable.getTicks() % 40 == 0) {
	    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
	    count = lastCheckCount;
	}
    }

    public int getCount() {
	if (!world.isRemote) {
	    ComponentInventory inv = getComponent(ComponentType.Inventory);
	    count = 0;
	    for (int i = 0; i < inv.getSizeInventory(); i++) {
		ItemStack stack = inv.getStackInSlot(i);
		if (!stack.isEmpty()) {
		    count += stack.getCount();
		}
	    }
	}
	return count;
    }

}
