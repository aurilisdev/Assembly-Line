package assemblyline.common.tile;

import java.util.ArrayList;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.settings.Constants;
import electrodynamics.common.tile.generic.GenericTile;
import electrodynamics.common.tile.generic.component.ComponentType;
import electrodynamics.common.tile.generic.component.type.ComponentDirection;
import electrodynamics.common.tile.generic.component.type.ComponentElectrodynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileConveyorBelt extends GenericTile {
    public int currentSpread = 0;
    public long lastTime = 0;

    public TileConveyorBelt() {
	super(DeferredRegisters.TILE_CONVEYORBELT.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentElectrodynamic(this).setMaxJoules(Constants.CONVEYORBELT_USAGE * 20)
		.addInputDirection(Direction.DOWN).addRelativeInputDirection(Direction.EAST)
		.addRelativeInputDirection(Direction.WEST));
    }

    public void onEntityCollision(Entity entityIn, boolean running) {
	BlockState state = world.getBlockState(pos);
	Direction facing = state.get(BlockConveyorBelt.FACING);
	boolean slanted = state.getBlock() == DeferredRegisters.blockSlantedConveyorbelt
		|| state.getBlock() == DeferredRegisters.blockSlantedConveyorbeltRunning;
	if (running && entityIn.getPosY() > pos.getY() + 4.0 / 16.0) {
	    Direction dir = facing.getOpposite();
	    if (slanted) {
		entityIn.setPosition(entityIn.getPosX() + dir.getXOffset(), entityIn.getPosY() + 1.2,
			entityIn.getPosZ() + dir.getZOffset());
	    } else {
		entityIn.addVelocity(dir.getXOffset() / 20.0, 0.0, dir.getZOffset() / 20.0);
	    }
	    BlockPos next = pos.offset(dir);
	    BlockState side = world.getBlockState(next);
	    if (entityIn instanceof ItemEntity) {
		ItemEntity itemEntity = (ItemEntity) entityIn;
		if (!itemEntity.getItem().isEmpty() && side.getBlock() instanceof BlockManipulator
			&& side.get(BlockConveyorBelt.FACING) == dir.getOpposite()) {
		    BlockPos chestPos = next.offset(dir);
		    TileEntity chestTile = world.getTileEntity(chestPos);
		    if (chestTile instanceof IInventory) {
			itemEntity.setItem(HopperTileEntity.putStackInInventoryAllSlots(null, (IInventory) chestTile,
				itemEntity.getItem(), dir.getOpposite()));
			if (itemEntity.getItem().isEmpty()) {
			    itemEntity.remove();
			}
		    }
		}
	    }
	}
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	checkForSpread();
	if (currentSpread == 0 || currentSpread == 16) {
	    if (electro.getJoulesStored() < Constants.CONVEYORBELT_USAGE) {
		if (running) {
		    world.setBlockState(pos,
			    (slanted ? DeferredRegisters.blockSlantedConveyorbelt : DeferredRegisters.blockConveyorbelt)
				    .getDefaultState().with(BlockConveyorBelt.FACING, facing),
			    2 | 16);
		    currentSpread = 0;
		}
	    } else {
		if (lastTime != world.getGameTime()) {
		    electro.setJoules(electro.getJoulesStored() - Constants.CONVEYORBELT_USAGE);
		    lastTime = world.getGameTime();
		    if (!running) {
			world.setBlockState(pos,
				(slanted ? DeferredRegisters.blockSlantedConveyorbeltRunning
					: DeferredRegisters.blockConveyorbeltRunning).getDefaultState()
						.with(BlockConveyorBelt.FACING, facing),
				2 | 16);
			currentSpread = 16;
		    }
		    currentSpread = 16;
		}
	    }
	} else {
	    if (currentSpread > 0 && !running) {
		world.setBlockState(pos,
			(slanted ? DeferredRegisters.blockSlantedConveyorbeltRunning
				: DeferredRegisters.blockConveyorbeltRunning).getDefaultState()
					.with(BlockConveyorBelt.FACING, facing),
			2 | 16);
	    }
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

    private long lastCheck = 0;

    public void checkForSpread() {
	if (world.getWorldInfo().getGameTime() - lastCheck > 100) {
	    lastCheck = world.getWorldInfo().getGameTime();
	    int lastMax = currentSpread;
	    int max = 0;
	    for (BlockPos po : offsets) {
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
