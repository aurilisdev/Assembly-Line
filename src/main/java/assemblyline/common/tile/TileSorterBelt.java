package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.electric.CapabilityElectrodynamic;
import electrodynamics.api.tile.electric.IElectrodynamic;
import electrodynamics.common.tile.generic.GenericTileInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileSorterBelt extends GenericTileInventory implements IElectrodynamic {
    public double joules = 0;
    public int currentSpread = 0;
    public long lastTime = 0;

    public TileSorterBelt() {
	super(DeferredRegisters.TILE_SORTERBELT.get());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
	if (capability == CapabilityElectrodynamic.ELECTRODYNAMIC && facing == Direction.DOWN) {
	    return (LazyOptional<T>) LazyOptional.of(() -> this);
	}
	return super.getCapability(capability, facing);
    }

    @Override
    public ITextComponent getDisplayName() {
	return new TranslationTextComponent("container.sorterbelt");
    }

    @Override
    public void setJoulesStored(double joules) {
	this.joules = joules;
    }

    @Override
    public double getJoulesStored() {
	return joules;
    }

    @Override
    public double getMaxJoulesStored() {
	return Constants.CONVEYORBELT_USAGE * 200;
    }

    @Override
    public int getSizeInventory() {
	return 18;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
	return SLOTS_EMPTY;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
	return new ContainerSorterBelt(id, player, this, new IntArray(0));
    }

    public void onEntityCollision(Entity entityIn, boolean running) {
	Direction facing = getBlockState().get(BlockConveyorBelt.FACING);
	if (running) {
	    if (entityIn.getPosY() > pos.getY() + 4.0 / 16.0) {
		Direction dir = facing.getOpposite();
		BlockPos next = pos.offset(dir);
		BlockState side = world.getBlockState(next);
		if (entityIn instanceof ItemEntity) {
		    ItemEntity itemEntity = (ItemEntity) entityIn;
		    boolean hasRight = false;
		    boolean hasLeft = false;
		    for (int i = 0; i < 9; i++) {
			ItemStack s = getStackInSlot(i);
			if (s.getItem() == itemEntity.getItem().getItem()) {
			    hasLeft = true;
			    break;
			}
		    }
		    for (int i = 9; i < 18; i++) {
			ItemStack s = getStackInSlot(i);
			if (s.getItem() == itemEntity.getItem().getItem()) {
			    hasRight = true;
			    break;
			}
		    }
		    if (hasLeft) {
			entityIn.addVelocity(dir.rotateYCCW().getXOffset() / 20.0, 0,
				dir.rotateYCCW().getZOffset() / 20.0);
		    } else if (hasRight) {
			entityIn.addVelocity(dir.rotateY().getXOffset() / 20.0, 0, dir.rotateY().getZOffset() / 20.0);
		    } else {
			entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
		    }
		    if (!itemEntity.getItem().isEmpty()) {
			if (side.getBlock() instanceof BlockManipulator) {
			    if (side.get(BlockConveyorBelt.FACING) == dir.getOpposite()) {
				BlockPos chestPos = next.offset(dir);
				TileEntity chestTile = world.getTileEntity(chestPos);
				if (chestTile instanceof IInventory) {
				    itemEntity.setItem(HopperTileEntity.putStackInInventoryAllSlots(null,
					    (IInventory) chestTile, itemEntity.getItem(), dir.getOpposite()));
				    if (itemEntity.getItem().isEmpty()) {
					itemEntity.remove();
				    }
				}
			    }
			}
		    }
		} else {
		    entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
		}
	    }
	}
	checkForSpread();
	if (currentSpread == 0 || currentSpread == 16) {
	    if (joules < Constants.CONVEYORBELT_USAGE) {
		if (running) {
		    world.setBlockState(pos,
			    DeferredRegisters.blockSorterBelt.getDefaultState().with(BlockConveyorBelt.FACING, facing),
			    2 | 16);
		    currentSpread = 0;
		}
	    } else {
		if (lastTime != world.getGameTime()) {
		    joules -= Constants.CONVEYORBELT_USAGE;
		    lastTime = world.getGameTime();
		    if (!running) {
			world.setBlockState(pos, DeferredRegisters.blockSorterBeltRunning.getDefaultState()
				.with(BlockConveyorBelt.FACING, facing), 2 | 16);
			currentSpread = 16;
		    }
		    currentSpread = 16;
		}
	    }
	} else {
	    if (currentSpread > 0 && !running) {
		world.setBlockState(pos, DeferredRegisters.blockSorterBeltRunning.getDefaultState()
			.with(BlockConveyorBelt.FACING, facing), 2 | 16);
	    }
	}
    }

    private long lastCheck = 0;

    public void checkForSpread() {
	if (world.getWorldInfo().getGameTime() - lastCheck > 100) {
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
		currentSpread = 0;
	    }
	}
    }
}
