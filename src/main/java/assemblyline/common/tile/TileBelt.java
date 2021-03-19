package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.api.tile.components.type.ComponentPacketHandler;
import electrodynamics.api.tile.components.type.ComponentTickable;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileBelt extends GenericTileTicking {
    public double progress = 0;
    public boolean halted;

    public TileBelt() {
	super(DeferredRegisters.TILE_BELT.get());
	addComponent(new ComponentTickable().addTickCommon(this::tickCommon).addTickServer(this::tickServer));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler().addCustomPacketReader(this::loadFromNBT).addCustomPacketWriter(this::saveToNBT));
	addComponent(new ComponentInventory().setInventorySize(2));
    }

    protected void tickServer(ComponentTickable tickable) {
	ComponentInventory inventory = getComponent(ComponentType.Inventory);
	if (progress >= 16) {
	    ComponentDirection direction = getComponent(ComponentType.Direction);
	    boolean flag = moveItemsIntoNextBelt(inventory, direction, pos.offset(direction.getDirection().getOpposite()))
		    || moveItemsIntoNextBelt(inventory, direction, pos.offset(Direction.UP).offset(direction.getDirection().getOpposite()))
		    || moveItemsIntoNextBelt(inventory, direction, pos.offset(Direction.DOWN).offset(direction.getDirection().getOpposite()));
	    if (!flag) {
		updateStatus(true);
	    }
	    if (!halted) {
		progress = -1;
		this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendCustomPacket();
	    }
	}
    }

    protected boolean moveItemsIntoNextBelt(ComponentInventory inventory, ComponentDirection direction, BlockPos pos) {
	TileEntity next = world.getTileEntity(pos);
	if (next instanceof TileBelt) {
	    TileBelt nextBelt = (TileBelt) next;
	    Direction other = nextBelt.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	    if (other != direction.getDirection().getOpposite()) {
		for (int indexHere = 0; indexHere < inventory.getSizeInventory(); indexHere++) {
		    ItemStack stackHere = inventory.getStackInSlot(indexHere);
		    if (!stackHere.isEmpty()) {
			inventory.setInventorySlotContents(indexHere, nextBelt.addItemOnBelt(stackHere));
		    }
		}
		boolean flag = false;
		for (ItemStack stack : inventory.getItems()) {
		    if (!stack.isEmpty()) {
			flag = true;
			break;
		    }
		}
		updateStatus(flag);
	    } else {
		updateStatus(true);
	    }
	}
	return next instanceof TileBelt;
    }

    protected void updateStatus(boolean newStatus) {
	boolean shouldUpdate = halted != newStatus;
	halted = newStatus;
	if (shouldUpdate) {
	    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendCustomPacket();
	}
    }

    protected void tickCommon(ComponentTickable tickable) {
	if (progress < 16 && !halted) {
	    progress += 17.0 / 14.0;
	}
	boolean flag = true;
	ComponentInventory inventory = getComponent(ComponentType.Inventory);
	for (ItemStack stack : inventory.getItems()) {
	    if (!stack.isEmpty()) {
		flag = false;
		break;
	    }
	}
	if (flag) {
	    progress = -1;
	}
    }

    public ItemStack addItemOnBelt(ItemStack add) {
	if (!add.isEmpty()) {
	    if (halted && progress == -1) {
		progress = 0;
	    }
	    ComponentInventory inventory = getComponent(ComponentType.Inventory);
	    for (int i = 0; i < inventory.getSizeInventory(); i++) {
		ItemStack returner = new InvWrapper(inventory).insertItem(i, add, false);
		if (returner.getCount() != add.getCount()) {
		    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendCustomPacket();
		    return returner;
		}
	    }
	}
	return add;
    }

    protected void loadFromNBT(CompoundNBT nbt) {
	NonNullList<ItemStack> obj = this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems();
	obj.clear();
	ItemStackHelper.loadAllItems(nbt, obj);

	progress = nbt.getDouble("progress");
	halted = nbt.getBoolean("halted");
    }

    protected void saveToNBT(CompoundNBT nbt) {
	ItemStackHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	nbt.putDouble("progress", progress);
	nbt.putBoolean("halted", halted);
    }

}
