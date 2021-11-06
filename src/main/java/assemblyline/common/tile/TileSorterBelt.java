package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.Constants;
import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileSorterBelt extends GenericTile {
    public int currentSpread = 0;
    public long lastTime = 0;

    public TileSorterBelt() {
	super(DeferredRegisters.TILE_SORTERBELT.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.CONVEYORBELT_USAGE * 20).input(Direction.DOWN));
	addComponent(new ComponentInventory(this).size(18));
	addComponent(new ComponentContainerProvider("container.sorterbelt")
		.createMenu((id, player) -> new ContainerSorterBelt(id, player, getComponent(ComponentType.Inventory), new SimpleContainerData(0))));
    }

    public void onEntityCollision(Entity entityIn, boolean running) {
	ComponentInventory inv = getComponent(ComponentType.Inventory);
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	Direction facing = getBlockState().getValue(BlockGenericMachine.FACING);
	if (running && entityIn.getY() > worldPosition.getY() + 4.0 / 16.0) {
	    Direction dir = facing.getOpposite();
	    if (entityIn instanceof ItemEntity) {
		ItemEntity itemEntity = (ItemEntity) entityIn;
		boolean hasRight = false;
		boolean hasLeft = false;
		for (int i = 0; i < 9; i++) {
		    ItemStack s = inv.getItem(i);
		    if (s.getItem() == itemEntity.getItem().getItem()) {
			hasLeft = true;
			break;
		    }
		}
		for (int i = 9; i < 18; i++) {
		    ItemStack s = inv.getItem(i);
		    if (s.getItem() == itemEntity.getItem().getItem()) {
			hasRight = true;
			break;
		    }
		}
		if (hasLeft) {
		    entityIn.push(dir.getCounterClockWise().getStepX() / 20.0, 0, dir.getCounterClockWise().getStepZ() / 20.0);
		} else if (hasRight) {
		    entityIn.push(dir.getClockWise().getStepX() / 20.0, 0, dir.getClockWise().getStepZ() / 20.0);
		} else {
		    entityIn.push(dir.getStepX() / 20.0, 0, dir.getStepZ() / 20.0);
		}
	    } else {
		entityIn.push(dir.getStepX() / 20.0, 0, dir.getStepZ() / 20.0);
	    }
	}
	checkForSpread();
	if (currentSpread == 0 || currentSpread == 16) {
	    if (electro.getJoulesStored() < Constants.SORTERBELT_USAGE) {
		if (running) {
		    level.setBlock(worldPosition, DeferredRegisters.blockSorterBelt.defaultBlockState().setValue(BlockGenericMachine.FACING, facing),
			    2 | 16);
		    currentSpread = 0;
		}
	    } else {
		if (lastTime != level.getGameTime()) {
		    electro.joules(electro.getJoulesStored() - Constants.SORTERBELT_USAGE);
		    lastTime = level.getGameTime();
		    if (!running) {
			level.setBlock(worldPosition,
				DeferredRegisters.blockSorterBeltRunning.defaultBlockState().setValue(BlockGenericMachine.FACING, facing), 2 | 16);
		    }
		    currentSpread = 16;
		}
	    }
	} else {
	    if (currentSpread > 0 && !running) {
		level.setBlock(worldPosition,
			DeferredRegisters.blockSorterBeltRunning.defaultBlockState().setValue(BlockGenericMachine.FACING, facing), 2 | 16);
	    }
	}
    }

    private long lastCheck = 0;

    public void checkForSpread() {
	if (level.getLevelData().getGameTime() - lastCheck > 40) {
	    lastCheck = level.getLevelData().getGameTime();
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
		currentSpread = lastMax;
	    }
	}
    }
}
