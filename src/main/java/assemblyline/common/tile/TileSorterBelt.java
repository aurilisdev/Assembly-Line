package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.GenericTile;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentContainerProvider;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentElectrodynamic;
import electrodynamics.api.tile.components.type.ComponentInventory;
import electrodynamics.common.block.BlockGenericMachine;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

public class TileSorterBelt extends GenericTile {
    public int currentSpread = 0;
    public long lastTime = 0;

    public TileSorterBelt() {
	super(DeferredRegisters.TILE_SORTERBELT.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.CONVEYORBELT_USAGE * 20).input(Direction.DOWN));
	addComponent(new ComponentInventory(this).size(18));
	addComponent(new ComponentContainerProvider("container.sorterbelt")
		.createMenu((id, player) -> new ContainerSorterBelt(id, player, getComponent(ComponentType.Inventory), new IntArray(0))));
    }

    public void onEntityCollision(Entity entityIn, boolean running) {
	ComponentInventory inv = getComponent(ComponentType.Inventory);
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	Direction facing = getBlockState().get(BlockGenericMachine.FACING);
	if (running && entityIn.getPosY() > pos.getY() + 4.0 / 16.0) {
	    Direction dir = facing.getOpposite();
	    BlockPos next = pos.offset(dir);
	    BlockState side = world.getBlockState(next);
	    if (entityIn instanceof ItemEntity) {
		ItemEntity itemEntity = (ItemEntity) entityIn;
		boolean hasRight = false;
		boolean hasLeft = false;
		for (int i = 0; i < 9; i++) {
		    ItemStack s = inv.getStackInSlot(i);
		    if (s.getItem() == itemEntity.getItem().getItem()) {
			hasLeft = true;
			break;
		    }
		}
		for (int i = 9; i < 18; i++) {
		    ItemStack s = inv.getStackInSlot(i);
		    if (s.getItem() == itemEntity.getItem().getItem()) {
			hasRight = true;
			break;
		    }
		}
		if (hasLeft) {
		    entityIn.addVelocity(dir.rotateYCCW().getXOffset() / 20.0, 0, dir.rotateYCCW().getZOffset() / 20.0);
		} else if (hasRight) {
		    entityIn.addVelocity(dir.rotateY().getXOffset() / 20.0, 0, dir.rotateY().getZOffset() / 20.0);
		} else {
		    entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
		}
		if (!itemEntity.getItem().isEmpty() && side.getBlock() instanceof BlockManipulator
			&& side.get(BlockGenericMachine.FACING) == dir.getOpposite()) {
		    BlockPos chestPos = next.offset(dir);
		    TileEntity chestTile = world.getTileEntity(chestPos);
		    if (chestTile instanceof IInventory) {
			itemEntity.setItem(
				HopperTileEntity.putStackInInventoryAllSlots(null, (IInventory) chestTile, itemEntity.getItem(), dir.getOpposite()));
			if (itemEntity.getItem().isEmpty()) {
			    itemEntity.remove();
			}
		    }
		}
	    } else {
		entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
	    }
	}
	checkForSpread();
	if (currentSpread == 0 || currentSpread == 16) {
	    if (electro.getJoulesStored() < Constants.SORTERBELT_USAGE) {
		if (running) {
		    world.setBlockState(pos, DeferredRegisters.blockSorterBelt.getDefaultState().with(BlockGenericMachine.FACING, facing), 2 | 16);
		    currentSpread = 0;
		}
	    } else {
		if (lastTime != world.getGameTime()) {
		    electro.joules(electro.getJoulesStored() - Constants.SORTERBELT_USAGE);
		    lastTime = world.getGameTime();
		    if (!running) {
			world.setBlockState(pos, DeferredRegisters.blockSorterBeltRunning.getDefaultState().with(BlockGenericMachine.FACING, facing),
				2 | 16);
		    }
		    currentSpread = 16;
		}
	    }
	} else {
	    if (currentSpread > 0 && !running) {
		world.setBlockState(pos, DeferredRegisters.blockSorterBeltRunning.getDefaultState().with(BlockGenericMachine.FACING, facing), 2 | 16);
	    }
	}
    }

    private long lastCheck = 0;

    public void checkForSpread() {
	if (world.getWorldInfo().getGameTime() - lastCheck > 40) {
	    lastCheck = world.getWorldInfo().getGameTime();
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
		currentSpread = lastMax;
	    }
	}
    }
}
