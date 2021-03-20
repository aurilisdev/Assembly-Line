package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.GenericTileTicking;
import electrodynamics.api.tile.components.ComponentType;
import electrodynamics.api.tile.components.type.ComponentDirection;
import electrodynamics.api.tile.components.type.ComponentElectrodynamic;
import electrodynamics.api.tile.components.type.ComponentTickable;
import electrodynamics.common.block.BlockGenericMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileManipulator extends GenericTileTicking {
    public TileManipulator() {
	super(DeferredRegisters.TILE_MANIPULATOR.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentTickable().tickServer(this::tickServer));
	addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.MANIPULATOR_USAGE * 20).input(Direction.DOWN));
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
			&& opposite.getBlockState().get(BlockGenericMachine.FACING).getOpposite() != dir) {
		    if (input) {
			world.setBlockState(pos, DeferredRegisters.blockManipulatorOutputRunning.getDefaultState().with(BlockGenericMachine.FACING,
				direction.getDirection()));
			input = false;
		    }
		    if (facing != null) {
			LazyOptional<IItemHandler> cap = facing.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite());
			if (cap.isPresent()) {
			    IItemHandler handler = cap.resolve().get();
			    for (int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack returned = handler.extractItem(slot, handler.getStackInSlot(slot).getCount(), false);
				if (!returned.isEmpty()) {
				    BlockPos offset = pos.offset(dir.getOpposite());
				    ItemEntity entity = new ItemEntity(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
				    entity.setMotion(0, 0, 0);
				    entity.setItem(returned);
				    world.addEntity(entity);
				    break;
				}
			    }
			}
		    }
		}
	    } else {
		if (input) {
		    input = true;
		    world.setBlockState(pos, DeferredRegisters.blockManipulatorInputRunning.getDefaultState().with(BlockGenericMachine.FACING,
			    direction.getDirection()));
		}
	    }
	}

	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	if (electro.getJoulesStored() < Constants.MANIPULATOR_USAGE) {
	    if (running) {
		Block next = input ? DeferredRegisters.blockManipulatorInput : DeferredRegisters.blockManipulatorOutput;
		world.setBlockState(pos, next.getDefaultState().with(BlockGenericMachine.FACING, direction.getDirection()), 2 | 16);
	    }
	} else {
	    electro.joules(electro.getJoulesStored() - Constants.MANIPULATOR_USAGE);
	    if (!running) {
		Block next = input ? DeferredRegisters.blockManipulatorInputRunning : DeferredRegisters.blockManipulatorOutputRunning;
		world.setBlockState(pos, next.getDefaultState().with(BlockGenericMachine.FACING, direction.getDirection()), 2 | 16);
	    }
	}
    }
}
