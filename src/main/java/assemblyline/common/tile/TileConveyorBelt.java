package assemblyline.common.tile;

import java.util.ArrayList;

import assemblyline.DeferredRegisters;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentElectrodynamic;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.api.tile.components.type.ComponentPacketHandler;
import electrodynamics.api.tile.components.type.ComponentTickable;
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

public class TileConveyorBelt extends GenericTileTicking {
    public int currentSpread = 0;
    public double progress = 0;
    public boolean halted;
    public boolean isManipulator;
    public boolean isManipulatorOutput;

    public TileConveyorBelt() {
	super(DeferredRegisters.TILE_BELT.get());
	addComponent(new ComponentTickable().tickCommon(this::tickCommon));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler().guiPacketReader(this::loadFromNBT).guiPacketWriter(this::saveToNBT));
	addComponent(new ComponentInventory(this).size(2));
	addComponent(new ComponentElectrodynamic(this).input(Direction.DOWN).relativeInput(Direction.EAST).relativeInput(Direction.WEST)
		.maxJoules(Constants.CONVEYORBELT_USAGE * 100));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
	return super.getRenderBoundingBox().expand(0, -1, 0).expand(0, 1, 0);
    }

    protected boolean moveItemsIntoNextBelt(ComponentInventory inventory, ComponentDirection direction, BlockPos pos, boolean check) {
	TileEntity next = world.getTileEntity(pos);
	if (next instanceof TileConveyorBelt && !check) {
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
	}
	return next instanceof TileConveyorBelt;
    }

    protected boolean moveItemsIntoInventory(ComponentInventory inventory, Direction direction, BlockPos pos, boolean check) {
	TileEntity chestTile = world.getTileEntity(pos);
	if (chestTile != null) {
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
	ComponentDirection direction = getComponent(ComponentType.Direction);
	flag = moveItemsIntoInventory(inventory, direction.getDirection(), pos.offset(direction.getDirection().getOpposite()), true);
	if (!flag) {
	    flag = moveItemsIntoInventory(inventory, direction.getDirection().getOpposite(), pos.offset(direction.getDirection()), true);
	    isManipulator = flag;
	    isManipulatorOutput = true;
	} else {
	    isManipulator = true;
	    isManipulatorOutput = false;
	}
	if (progress >= 15) {
	    flag = moveItemsIntoNextBelt(inventory, direction, pos.offset(direction.getDirection().getOpposite()), false)
		    || moveItemsIntoNextBelt(inventory, direction, pos.offset(Direction.UP).offset(direction.getDirection().getOpposite()), false)
		    || moveItemsIntoNextBelt(inventory, direction, pos.offset(Direction.DOWN).offset(direction.getDirection().getOpposite()), false);
	    if (!flag) {
		flag = moveItemsIntoInventory(inventory, direction.getDirection(), pos.offset(direction.getDirection().getOpposite()), false);
		if (!flag) {
		    moveItemsIntoInventory(inventory, direction.getDirection().getOpposite(), pos.offset(direction.getDirection()), false);
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
		} else {
		    progress -= 1;
		    updateStatus(true);
		}
	    }
	    if (!halted) {
		progress = -1;
		this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
	    }
	}
	if (progress == -1 && isManipulatorOutput && isManipulator && currentSpread > 0) {
	    TileEntity from = world.getTileEntity(pos.offset(direction.getDirection()));
	    if (from != null) {
		LazyOptional<IItemHandler> cap = from.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
			direction.getDirection().getOpposite());
		if (cap.isPresent()) {
		    IItemHandler handler = cap.resolve().get();
		    for (int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack returned = handler.extractItem(slot, 64, false);
			if (!returned.isEmpty()) {
			    addItemOnBelt(returned);
			    break;
			}
		    }
		}
	    }
	}
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	if (!world.isRemote && tickable.getTicks() % 20 == 0) {
	    checkForSpread();
	    if (currentSpread == 0 || currentSpread == 16) {
		if (electro.getJoulesStored() < Constants.CONVEYORBELT_USAGE * 20) {
		    currentSpread = 0;
		} else {
		    electro.joules(electro.getJoulesStored() - Constants.CONVEYORBELT_USAGE * 20);
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
	isManipulator = nbt.getBoolean("isManipulator");
	isManipulatorOutput = nbt.getBoolean("isManipulatorOutput");
    }

    protected void saveToNBT(CompoundNBT nbt) {
	ItemStackHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	nbt.putDouble("progress", progress);
	nbt.putInt("currentSpread", currentSpread);
	nbt.putBoolean("halted", halted);
	nbt.putBoolean("isManipulator", isManipulator);
	nbt.putBoolean("isManipulatorOutput", isManipulatorOutput);
    }

    public void checkForSpread() {
	int lastMax = currentSpread;
	int max = 0;
	for (BlockPos po : TileConveyorBelt.offsets) {
	    TileEntity at = world.getTileEntity(pos.add(po));
	    if (at instanceof TileConveyorBelt) {
		TileConveyorBelt belt = (TileConveyorBelt) at;
		int their = belt.currentSpread;
		if (their - 1 > max) {
		    max = their - 1;
		}
	    } else if (at instanceof TileSorterBelt) {
		TileSorterBelt belt = (TileSorterBelt) at;
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
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.DOWN).offset(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.DOWN).offset(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.DOWN).offset(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.DOWN).offset(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.UP).offset(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.UP).offset(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.UP).offset(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).offset(Direction.UP).offset(Direction.SOUTH));
    }

}
