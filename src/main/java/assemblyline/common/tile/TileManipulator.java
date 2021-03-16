package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentElectrodynamic;
import electrodynamics.api.tile.components.type.ComponentTickable;
import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileManipulator extends GenericTileTicking {
    public TileManipulator() {
	super(DeferredRegisters.TILE_MANIPULATOR.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentTickable().addTickServer(this::tickServer));
	addComponent(new ComponentElectrodynamic(this).setMaxJoules(Constants.MANIPULATOR_USAGE * 20).addInputDirection(Direction.DOWN));
    }

    public void tickServer(ComponentTickable component) {
	boolean running = ((BlockManipulator) getBlockState().getBlock()).running;
	boolean input = ((BlockManipulator) getBlockState().getBlock()).input;
	ComponentDirection direction = getComponent(ComponentType.Direction);
	if (component.getTicks() % 20 == 0) {
	    if (running) {
		Direction dir = direction.getDirection().getOpposite();
		TileEntity facing = world.getTileEntity(pos.offset(dir));
		TileEntity opposite = world.getTileEntity(pos.offset(dir.getOpposite()));
		if ((opposite instanceof TileConveyorBelt || opposite instanceof TileSorterBelt)
			&& opposite.getBlockState().get(BlockConveyorBelt.FACING).getOpposite() != dir) {
		    if (input) {
			world.setBlockState(pos, DeferredRegisters.blockManipulatorOutputRunning.getDefaultState().with(BlockConveyorBelt.FACING,
				direction.getDirection()));
			input = false;
		    }
		    if (facing instanceof IInventory) {
			IInventory inv = (IInventory) facing;
			if (inv instanceof ISidedInventory) {
			    ISidedInventory sided = (ISidedInventory) inv;
			    for (int slot : sided.getSlotsForFace(dir.getOpposite())) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (!stack.isEmpty() && sided.canExtractItem(slot, stack, dir.getOpposite())) {
				    BlockPos offset = pos.offset(dir.getOpposite());
				    ItemEntity entity = new ItemEntity(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
				    entity.setMotion(0, 0, 0);
				    entity.setItem(stack);
				    world.addEntity(entity);
				    inv.setInventorySlotContents(slot, ItemStack.EMPTY);
				    break;
				}
			    }
			} else {
			    for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (!stack.isEmpty()) {
				    BlockPos offset = pos.offset(dir.getOpposite());
				    ItemEntity entity = new ItemEntity(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
				    entity.setMotion(0, 0, 0);
				    entity.setItem(stack);
				    world.addEntity(entity);
				    inv.setInventorySlotContents(slot, ItemStack.EMPTY);
				    break;
				}
			    }
			}
		    }
		} else {
		    if (input) {
			input = true;
			world.setBlockState(pos, DeferredRegisters.blockManipulatorInputRunning.getDefaultState().with(BlockConveyorBelt.FACING,
				direction.getDirection()));
		    }
		}
	    }
	    ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	    if (electro.getJoulesStored() < Constants.MANIPULATOR_USAGE) {
		if (running) {
		    Block next = input ? DeferredRegisters.blockManipulatorInput : DeferredRegisters.blockManipulatorOutput;
		    world.setBlockState(pos, next.getDefaultState().with(BlockConveyorBelt.FACING, direction.getDirection()), 2 | 16);
		}
	    } else {
		electro.setJoules(electro.getJoulesStored() - Constants.MANIPULATOR_USAGE);
		if (!running) {
		    Block next = input ? DeferredRegisters.blockManipulatorInputRunning : DeferredRegisters.blockManipulatorOutputRunning;
		    world.setBlockState(pos, next.getDefaultState().with(BlockConveyorBelt.FACING, direction.getDirection()), 2 | 16);
		}
	    }
	}
    }
}
