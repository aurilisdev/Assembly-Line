package assemblyline.common.tile;

import java.util.ArrayList;

import com.mojang.math.Vector3f;

import assemblyline.DeferredRegisters;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileBetterConveyorBelt extends GenericTile {
    public int currentSpread = 0;
    public boolean running;
    public ConveyorType type = ConveyorType.Horizontal;
    public ArrayList<TileBetterConveyorBelt> inQueue = new ArrayList<>();
    public boolean isQueueReady = false;
    public boolean waiting = false;
    public boolean isPusher = false;
    public boolean isPuller = false;
    public ConveyorObject object = new ConveyorObject();

    public TileBetterConveyorBelt(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_BETTERBELT.get(), worldPosition, blockState);
	addComponent(new ComponentTickable().tickCommon(this::tickCommon));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler().guiPacketReader(this::loadFromNBT).guiPacketWriter(this::saveToNBT));
	addComponent(new ComponentInventory(this).size(1));
	addComponent(new ComponentElectrodynamic(this).input(Direction.DOWN).relativeInput(Direction.EAST).relativeInput(Direction.WEST)
		.maxJoules(Constants.CONVEYORBELT_USAGE * 100));
    }

    protected void sync() {
	this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
    }

    public ItemStack getStackOnBelt() {
	return this.<ComponentInventory>getComponent(ComponentType.Inventory).getItem(0);
    }

    public ItemStack addItemOnBelt(ItemStack add) {
	ItemStack onBelt = getStackOnBelt();
	if (onBelt.isEmpty()) {
	    object.pos = new Vector3f(0.5f + worldPosition.getX(), 0, 0.5f + worldPosition.getZ());
	}
	if (!add.isEmpty()) {
	    ComponentInventory inventory = getComponent(ComponentType.Inventory);
	    ItemStack returner = new InvWrapper(inventory).insertItem(0, add, false);
	    if (returner.getCount() != add.getCount()) {
		this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		return returner;
	    }
	}
	return add;
    }

    public ItemStack addItemOnBelt(ItemStack add, ConveyorObject object) {
	if (!add.isEmpty()) {
	    ComponentInventory inventory = getComponent(ComponentType.Inventory);
	    ItemStack returner = new InvWrapper(inventory).insertItem(0, add, false);
	    this.object.pos = object.pos.copy();
	    if (returner.getCount() != add.getCount()) {
		this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		return returner;
	    }
	}
	return add;
    }

    protected void tickCommon(ComponentTickable tickable) {
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	ItemStack stackOnBelt = getStackOnBelt();
	isQueueReady = stackOnBelt.isEmpty();
	running = currentSpread > 0 && isQueueReady;
	BlockEntity nextBlockEntity = getNextEntity();
	Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	isPusher = false;
	if (nextBlockEntity != null && !(nextBlockEntity instanceof TileBetterConveyorBelt)) {
	    LazyOptional<IItemHandler> handlerOptional = nextBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction);
	    isPusher = handlerOptional.isPresent();
	}
	if (currentSpread > 0) {
	    attemptMove();
	}
	BlockEntity lastBlockEntity = level.getBlockEntity(worldPosition.offset(direction.getNormal()));
	isPuller = false;
	if (lastBlockEntity != null && !(lastBlockEntity instanceof TileBetterConveyorBelt)) {
	    LazyOptional<IItemHandler> handlerOptional = lastBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction);
	    isPuller = handlerOptional.isPresent();
	}
	if (isQueueReady) {
	    if (!inQueue.isEmpty()) {
		while (true) {
		    TileBetterConveyorBelt queue = inQueue.get(0);
		    if (!queue.isRemoved() && queue.waiting) {
			break;
		    }
		    inQueue.remove(0);
		    if (inQueue.isEmpty()) {
			break;
		    }
		}
	    } else if (lastBlockEntity != null && isPuller) {
		LazyOptional<IItemHandler> cap = lastBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction);
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
	if (!level.isClientSide) {
	    checkForSpread();
	    if (currentSpread == 0 || currentSpread == 16) {
		if (electro.getJoulesStored() < Constants.CONVEYORBELT_USAGE) {
		    currentSpread = 0;
		} else {
		    electro.joules(electro.getJoulesStored() - Constants.CONVEYORBELT_USAGE);
		    currentSpread = 16;
		}
		sync();
	    }
	}
    }

    public Vector3f getObjectLocal() {
	return new Vector3f(object.pos.x() - worldPosition.getX(), 0, object.pos.z() - worldPosition.getZ());
    }

    public Vector3f getDirectionAsVector() {
	Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection().getOpposite();
	return new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ());
    }

    public boolean shouldTransfer() {
	TileBetterConveyorBelt belt = getNextConveyor();
	BlockPos pos = new BlockPos(Math.floor(object.pos.x()), getBlockPos().getY(), Math.floor(object.pos.z()));
	Vector3f local = getObjectLocal();
	Vector3f direction = getDirectionAsVector();
	float coordComponent = local.dot(direction);
	float value = belt != null && ((belt.inQueue.isEmpty() || belt.inQueue.get(0) == this) && belt.isQueueReady) ? 1.25F : 1;
	if (direction.x() + direction.y() + direction.z() > 0) {
	    return !pos.equals(worldPosition) && coordComponent > value;
	}
	return !pos.equals(worldPosition) && coordComponent > value - 1;
    }

    public void attemptMove() {
	Vector3f move = getDirectionAsVector();
	ItemStack stackOnBelt = getStackOnBelt();
	if (!stackOnBelt.isEmpty()) {
	    boolean shouldTransfer = shouldTransfer();
	    BlockEntity nextBlockEntity = getNextEntity();
	    if (nextBlockEntity instanceof TileBetterConveyorBelt belt) {
		if (shouldTransfer) {
		    if (!belt.inQueue.contains(this)) {
			belt.inQueue.add(this);
		    }
		    if (belt.inQueue.get(0) == this && belt.isQueueReady) {
			waiting = false;
			belt.inQueue.remove(0);
			belt.addItemOnBelt(getStackOnBelt(), object);
			this.<ComponentInventory>getComponent(ComponentType.Inventory).setItem(0, ItemStack.EMPTY);
		    } else {
			waiting = true;
		    }
		}
	    } else if (nextBlockEntity != null) {
		if (shouldTransfer) {
		    Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		    LazyOptional<IItemHandler> handlerOptional = nextBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
			    direction);
		    ComponentInventory inventory = getComponent(ComponentType.Inventory);
		    if (handlerOptional.isPresent()) {
			putItemsIntoInventory(handlerOptional, inventory);
		    } else {
			dropItem(stackOnBelt, move);
			this.<ComponentInventory>getComponent(ComponentType.Inventory).setItem(0, ItemStack.EMPTY);
		    }
		}
	    } else {
		dropItem(stackOnBelt, move);
		this.<ComponentInventory>getComponent(ComponentType.Inventory).setItem(0, ItemStack.EMPTY);
	    }
	    if (!shouldTransfer) {
		move.mul(1 / 16.0f);
		object.pos.add(move);
	    }
	}
	isQueueReady = stackOnBelt.isEmpty();
	running = currentSpread > 0 && (!waiting || isQueueReady);
    }

    public void dropItem(ItemStack stackOnBelt, Vector3f move) {
	ItemEntity entity = new ItemEntity(level, worldPosition.getX() + 0.5 + move.x() / 2.0f, worldPosition.getY() + 0.4,
		worldPosition.getZ() + 0.5 + move.z() / 2.0f, stackOnBelt);
	entity.setDeltaMovement(move.x() / 12.0, 1.5 / 16.0, move.z() / 12.0);
	entity.setPickUpDelay(20);
	level.addFreshEntity(entity);
    }

    private static void putItemsIntoInventory(LazyOptional<IItemHandler> handlerOptional, ComponentInventory inventory) {
	IItemHandler handler = handlerOptional.resolve().get();
	for (int indexHere = 0; indexHere < inventory.getContainerSize(); indexHere++) {
	    ItemStack stackHere = inventory.getItem(indexHere);
	    if (!stackHere.isEmpty()) {
		for (int indexThere = 0; indexThere < handler.getSlots(); indexThere++) {
		    ItemStack set = handler.insertItem(indexThere, stackHere, false);
		    inventory.setItem(indexHere, set);
		    stackHere = set;
		    if (inventory.getItem(indexHere).isEmpty()) {
			break;
		    }
		}
	    }
	}
    }

    public BlockPos getNextPos() {
	Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection().getOpposite();
	return switch (type) {
	case Horizontal -> worldPosition.relative(direction);
	case SlopedDown -> worldPosition.relative(direction).below();
	case SlopedUp -> worldPosition.relative(direction).above();
	case Vertical -> worldPosition.relative(Direction.UP);
	default -> null;
	};
    }

    protected BlockEntity getNextEntity() {
	BlockEntity nextBlockEntity = null;
	BlockPos nextBlockPos = getNextPos();
	if (nextBlockPos != null) {
	    nextBlockEntity = level.getBlockEntity(nextBlockPos);
	}
	return nextBlockEntity;
    }

    protected TileBetterConveyorBelt getNextConveyor() {
	return getNextEntity()instanceof TileBetterConveyorBelt belt ? belt : null;
    }

    protected void loadFromNBT(CompoundTag nbt) {
	NonNullList<ItemStack> obj = this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems();
	obj.clear();
	ContainerHelper.loadAllItems(nbt, obj);
	currentSpread = nbt.getInt("currentSpread");
	running = nbt.getBoolean("running");
	type = ConveyorType.values()[nbt.getInt("type")];
	isQueueReady = nbt.getBoolean("isQueueReady");
	waiting = nbt.getBoolean("waiting");
	object.pos = new Vector3f(nbt.getFloat("convX"), 0, nbt.getFloat("convZ"));
    }

    protected void saveToNBT(CompoundTag nbt) {
	ContainerHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	nbt.putInt("currentSpread", currentSpread);
	nbt.putBoolean("running", running);
	nbt.putInt("type", type.ordinal());
	nbt.putBoolean("isQueueReady", isQueueReady);
	nbt.putBoolean("waiting", waiting);
	nbt.putFloat("convX", object.pos.x());
	nbt.putFloat("convZ", object.pos.z());
    }

    public void checkForSpread() {
	int lastMax = currentSpread;
	int max = 0;
	for (BlockPos po : TileBetterConveyorBelt.offsets) {
	    BlockEntity at = level.getBlockEntity(worldPosition.offset(po));
	    if (at instanceof TileBetterConveyorBelt belt) {
		int their = belt.currentSpread;
		if (their - 1 > max) {
		    max = their - 1;
		}
	    } else if (at instanceof TileSorterBelt sbelt) {
		int their = sbelt.currentSpread;
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

    public enum ConveyorType {
	Horizontal,
	SlopedUp,
	SlopedDown,
	Vertical;
    }
}
