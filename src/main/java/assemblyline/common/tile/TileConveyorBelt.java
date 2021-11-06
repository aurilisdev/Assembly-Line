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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
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
    public AABB getRenderBoundingBox() {
	return super.getRenderBoundingBox().expandTowards(0, -1, 0).expandTowards(0, 1, 0);
    }

    protected boolean moveItemsIntoNextBelt(ComponentInventory inventory, ComponentDirection direction, BlockPos pos, boolean check) {
	BlockEntity next = level.getBlockEntity(pos);
	if (!check) {
	    if (next instanceof TileConveyorBelt) {
		TileConveyorBelt nextBelt = (TileConveyorBelt) next;
		Direction other = nextBelt.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		if (other != direction.getDirection().getOpposite()) {
		    for (int indexHere = 0; indexHere < inventory.getContainerSize(); indexHere++) {
			ItemStack stackHere = inventory.getItem(indexHere);
			if (!stackHere.isEmpty()) {
			    inventory.setItem(indexHere, nextBelt.addItemOnBelt(stackHere));
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
		Direction other = nextBelt.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		if (other == direction.getDirection()) {
		    for (int indexHere = 0; indexHere < inventory.getContainerSize(); indexHere++) {
			ItemStack stackHere = inventory.getItem(indexHere);
			if (!stackHere.isEmpty()) {
			    inventory.setItem(indexHere, nextBelt.addItemOnBelt(stackHere));
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
	}
	return next instanceof TileConveyorBelt || next instanceof TileElevatorBelt;
    }

    protected boolean moveItemsIntoInventory(ComponentInventory inventory, Direction direction, BlockPos pos, boolean check) {
	BlockEntity chestTile = level.getBlockEntity(pos);
	if (chestTile != null && !(chestTile instanceof TileConveyorBelt)) {
	    LazyOptional<IItemHandler> handlerOptional = chestTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction);
	    if (handlerOptional.isPresent() && !check) {
		IItemHandler handler = handlerOptional.resolve().get();
		for (int indexHere = 0; indexHere < inventory.getContainerSize(); indexHere++) {
		    ItemStack stackHere = inventory.getItem(indexHere);
		    if (!stackHere.isEmpty()) {
			for (int indexThere = 0; indexThere < handler.getSlots(); indexThere++) {
			    inventory.setItem(indexHere, handler.insertItem(indexThere, stackHere, false));
			    if (inventory.getItem(indexHere).isEmpty()) {
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
	flag = moveItemsIntoInventory(inventory, direction.getDirection(), worldPosition.relative(direction.getDirection().getOpposite()), true);
	if (!flag) {
	    flag = moveItemsIntoInventory(inventory, direction.getDirection().getOpposite(), worldPosition.relative(direction.getDirection()), true);
	    isManipulator = flag;
	    isManipulatorOutput = true;
	} else {
	    isManipulator = true;
	    isManipulatorOutput = false;
	}
	if (progress >= 15) {
	    flag = moveItemsIntoNextBelt(inventory, direction, worldPosition.relative(direction.getDirection().getOpposite()), false)
		    || moveItemsIntoNextBelt(inventory, direction,
			    worldPosition.relative(Direction.UP).relative(direction.getDirection().getOpposite()), false)
		    || moveItemsIntoNextBelt(inventory, direction,
			    worldPosition.relative(Direction.DOWN).relative(direction.getDirection().getOpposite()), false);
	    if (!flag) {
		flag = moveItemsIntoInventory(inventory, direction.getDirection(), worldPosition.relative(direction.getDirection().getOpposite()),
			false);
		if (!flag) {
		    moveItemsIntoInventory(inventory, direction.getDirection().getOpposite(), worldPosition.relative(direction.getDirection()),
			    false);
		    for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			Direction proper = direction.getDirection().getOpposite();
			ItemEntity entity = new ItemEntity(level, worldPosition.getX() + 0.5 + proper.getStepX() / 2.0, worldPosition.getY() + 0.4,
				worldPosition.getZ() + 0.5 + proper.getStepZ() / 2.0, stack);
			entity.setDeltaMovement(proper.getStepX() / 16.0, 1 / 16.0, proper.getStepZ() / 16.0);
			entity.setPickUpDelay(20);
			level.addFreshEntity(entity);
			inventory.setItem(i, ItemStack.EMPTY);
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
	    BlockEntity from = level.getBlockEntity(worldPosition.relative(direction.getDirection()));
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
	if (!level.isClientSide) {
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
	    for (int i = 0; i < inventory.getContainerSize(); i++) {
		ItemStack returner = new InvWrapper(inventory).insertItem(i, add, false);
		if (returner.getCount() != add.getCount()) {
		    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		    return returner;
		}
	    }
	}
	return add;
    }

    protected void loadFromNBT(CompoundTag nbt) {
	NonNullList<ItemStack> obj = this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems();
	obj.clear();
	ContainerHelper.loadAllItems(nbt, obj);
	progress = nbt.getDouble("progress");
	currentSpread = nbt.getInt("currentSpread");
	halted = nbt.getBoolean("halted");
	isManipulator = nbt.getBoolean("isManipulator");
	isManipulatorOutput = nbt.getBoolean("isManipulatorOutput");
    }

    protected void saveToNBT(CompoundTag nbt) {
	ContainerHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
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
	    BlockEntity at = level.getBlockEntity(worldPosition.offset(po));
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
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.SOUTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.EAST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.WEST));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.NORTH));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.SOUTH));
    }

}
