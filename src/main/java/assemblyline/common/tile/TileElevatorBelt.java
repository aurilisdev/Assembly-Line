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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileElevatorBelt extends GenericTileTicking {
    public int currentSpread = 0;
    public double progress = 0;
    public boolean halted;

    public TileElevatorBelt(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_ELEVATORBELT.get(), worldPosition, blockState);
	addComponent(new ComponentTickable().tickCommon(this::tickCommon));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler().guiPacketReader(this::loadFromNBT).guiPacketWriter(this::saveToNBT));
	addComponent(new ComponentInventory(this).size(1));
	addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).relativeInput(Direction.EAST).relativeInput(Direction.WEST)
		.maxJoules(Constants.CONVEYORBELT_USAGE * 100));
    }

    @Override
    public AABB getRenderBoundingBox() {
	return super.getRenderBoundingBox().expandTowards(0, -1, 0).expandTowards(0, 1, 0);
    }

    protected boolean moveItemsIntoNextBelt(ComponentInventory inventory, ComponentDirection direction, BlockPos pos, boolean check) {
	BlockEntity next = level.getBlockEntity(pos);
	if (!check) {
	    if (next instanceof TileConveyorBelt nextBelt) {
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
	    } else if (next instanceof TileElevatorBelt nextBelt) {
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
	return next instanceof TileConveyorBelt || next instanceof TileElevatorBelt;
    }

    protected boolean moveItemsIntoInventory(ComponentInventory inventory, Direction direction, BlockPos pos, boolean check) {
	BlockEntity chestTile = level.getBlockEntity(pos);
	if (chestTile != null && !(chestTile instanceof TileElevatorBelt)) {
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
	    flag = moveItemsIntoNextBelt(inventory, direction, worldPosition.above(), false);
	    if (!flag) {
		if (!moveItemsIntoNextBelt(inventory, direction, worldPosition.above().relative(direction.getDirection().getOpposite()), false)) {
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
    }

    protected void saveToNBT(CompoundTag nbt) {
	ContainerHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	nbt.putDouble("progress", progress);
	nbt.putInt("currentSpread", currentSpread);
	nbt.putBoolean("halted", halted);
    }

    public void checkForSpread() {
	int lastMax = currentSpread;
	int max = 0;
	for (BlockPos po : TileElevatorBelt.offsets) {
	    BlockEntity at = level.getBlockEntity(worldPosition.offset(po));
	    if (at instanceof TileElevatorBelt belt) {
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
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.UP));
	offsets.add(new BlockPos(0, 0, 0).relative(Direction.DOWN));
    }

}
