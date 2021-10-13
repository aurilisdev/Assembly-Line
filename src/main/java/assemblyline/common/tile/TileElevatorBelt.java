package assemblyline.common.tile;

import java.util.ArrayList;

import assemblyline.DeferredRegisters;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTileTicking;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileElevatorBelt extends GenericTileTicking {
    public int currentSpread = 0;
    public double progress = 0;
    public boolean halted;

    public TileElevatorBelt() {
	super(DeferredRegisters.TILE_ELEVATORBELT.get());
	addComponent(new ComponentTickable().tickCommon(this::tickCommon));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler().guiPacketReader(this::loadFromNBT).guiPacketWriter(this::saveToNBT));
	addComponent(new ComponentInventory(this).size(2));
	addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).relativeInput(Direction.EAST).relativeInput(Direction.WEST)
		.maxJoules(Constants.CONVEYORBELT_USAGE * 100));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
	return super.getRenderBoundingBox().expand(0, -1, 0).expand(0, 1, 0);
    }

    protected boolean moveItemsIntoNextBelt(ComponentInventory inventory, ComponentDirection direction, BlockPos pos, boolean check) {
	TileEntity next = world.getTileEntity(pos);
	if (!check) {
	    if (next instanceof TileConveyorBelt) {
		TileConveyorBelt nextBelt = (TileConveyorBelt) next;
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
	    } else if (next instanceof TileElevatorBelt) {
		TileElevatorBelt nextBelt = (TileElevatorBelt) next;
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
	return next instanceof TileConveyorBelt || next instanceof TileElevatorBelt;
    }

    protected boolean moveItemsIntoInventory(ComponentInventory inventory, Direction direction, BlockPos pos, boolean check) {
	TileEntity chestTile = world.getTileEntity(pos);
	if (chestTile != null && !(chestTile instanceof TileElevatorBelt)) {
	    LazyOptional<IItemHandler> handlerOptional = chestTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction);
	    if (handlerOptional.isPresent() && !check) {
		IItemHandler handler = handlerOptional.resolve().get();
		for (int indexHere = 0; indexHere < inventory.getSizeInventory(); indexHere++) {
		    ItemStack stackHere = inventory.getStackInSlot(indexHere);
		    if (!stackHere.isEmpty()) {
			for (int indexThere = 0; indexThere < handler.getSlots(); indexThere++) {
			    inventory.setInventorySlotContents(indexHere, handler.insertItem(indexThere, stackHere, false));
			    if (inventory.getStackInSlot(indexHere).isEmpty()) {
				break;
			    }
			}

		    }
		}
	    }
	    return handlerOptional.isPresent();
	}
	return false;
    }

    protected void updateStatus(boolean newStatus) {
	halted = newStatus;
	this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
    }

    protected void tickCommon(ComponentTickable tickable) {
	if (progress < 15 && !halted && currentSpread > 0) {
	    progress += 17.0 / 14.0;
	}
	ComponentDirection direction = getComponent(ComponentType.Direction);
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
	} else {
	    halted = false;
	}
	if (progress >= 15) {
	    flag = moveItemsIntoNextBelt(inventory, direction, pos.up(), false);
	    if (!flag) {
		if (!moveItemsIntoNextBelt(inventory, direction, pos.up().offset(direction.getDirection().getOpposite()), false)) {
		    for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			Direction proper = direction.getDirection().getOpposite();
			ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5 + proper.getXOffset() / 2.0, pos.getY() + 0.4,
				pos.getZ() + 0.5 + proper.getZOffset() / 2.0, stack);
			entity.setMotion(proper.getXOffset() / 16.0, 1 / 16.0, proper.getZOffset() / 16.0);
			entity.setPickupDelay(20);
			world.addEntity(entity);
			inventory.setInventorySlotContents(i, ItemStack.EMPTY);
		    }
		    updateStatus(false);
		}
	    } else {
		progress -= 1;
		updateStatus(true);
	    }
	    if (!halted) {
		progress = -1;
		this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
	    }
	}

	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	if (!world.isRemote) {
	    checkForSpread();
	    if (currentSpread == 0 || currentSpread == 16) {
		if (electro.getJoulesStored() < Constants.CONVEYORBELT_USAGE) {
		    currentSpread = 0;
		} else {
		    electro.joules(electro.getJoulesStored() - Constants.CONVEYORBELT_USAGE);
		    currentSpread = 16;
		}
	    }
	}
    }

    public ItemStack addItemOnBelt(ItemStack add) {
	if (!add.isEmpty()) {
	    ComponentInventory inventory = getComponent(ComponentType.Inventory);
	    for (int i = 0; i < inventory.getSizeInventory(); i++) {
		ItemStack returner = new InvWrapper(inventory).insertItem(i, add, false);
		if (returner.getCount() != add.getCount()) {
		    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
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
	currentSpread = nbt.getInt("currentSpread");
	halted = nbt.getBoolean("halted");
    }

    protected void saveToNBT(CompoundNBT nbt) {
	ItemStackHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	nbt.putDouble("progress", progress);
	nbt.putInt("currentSpread", currentSpread);
	nbt.putBoolean("halted", halted);
    }

    public void checkForSpread() {
	int lastMax = currentSpread;
	int max = 0;
	for (BlockPos po : TileElevatorBelt.offsets) {
	    TileEntity at = world.getTileEntity(pos.add(po));
	    if (at instanceof TileElevatorBelt) {
		TileElevatorBelt belt = (TileElevatorBelt) at;
		int their = belt.currentSpread;
		if (their - 1 > max) {
		    max = their - 1;
		}
	    }
	}
	currentSpread = max;
	if (lastMax > currentSpread) {
	    currentSpread = 0;
	}
    }

    public static ArrayList<BlockPos> offsets = new ArrayList<>();
    static {
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.UP));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.DOWN));
    }

}
