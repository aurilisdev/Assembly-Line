package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.ITickableTileBase;
import electrodynamics.api.tile.electric.IElectricTile;
import electrodynamics.api.tile.electric.IPowerReceiver;
import electrodynamics.api.utilities.TransferPack;
import electrodynamics.common.tile.generic.GenericTileBase;
import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileManipulator extends GenericTileBase implements ITickableTileBase, IPowerReceiver, IElectricTile {
	public double joules = 0;

	private long ticks = 0;

	public TileManipulator() {
		super(DeferredRegisters.TILE_MANIPULATOR.get());
	}

	@Override
	public void tickServer() {
		ticks++;
		boolean running = ((BlockManipulator) getBlockState().getBlock()).running;
		boolean input = ((BlockManipulator) getBlockState().getBlock()).input;
		if (ticks % 20 == 0) {
			if (joules < Constants.MANIPULATOR_USAGE) {
				if (running) {
					Block next = input ? DeferredRegisters.blockManipulatorInput : DeferredRegisters.blockManipulatorOutput;
					world.setBlockState(pos, next.getDefaultState().with(BlockConveyorBelt.FACING, getFacing()));
				}
			} else {
				joules -= Constants.MANIPULATOR_USAGE;
				if (!running) {
					Block next = input ? DeferredRegisters.blockManipulatorInputRunning : DeferredRegisters.blockManipulatorOutputRunning;
					world.setBlockState(pos, next.getDefaultState().with(BlockConveyorBelt.FACING, getFacing()));
				}
			}
			Direction dir = getFacing().getOpposite();
			TileEntity facing = world.getTileEntity(pos.offset(dir));
			TileEntity opposite = world.getTileEntity(pos.offset(dir.getOpposite()));
			if (opposite instanceof TileConveyorBelt && opposite.getBlockState().get(BlockConveyorBelt.FACING).getOpposite() != dir) {
				if (((BlockManipulator) getBlockState().getBlock()).input) {
					world.setBlockState(pos, DeferredRegisters.blockManipulatorOutput.getDefaultState().with(BlockManipulator.FACING, getFacing()));
				}
				if (facing instanceof IInventory) {
					IInventory inv = (IInventory) facing;
					if (inv instanceof ISidedInventory) {
						ISidedInventory sided = (ISidedInventory) inv;
						for (int slot : sided.getSlotsForFace(dir.getOpposite())) {
							ItemStack stack = inv.getStackInSlot(slot);
							if (!stack.isEmpty()) {
								if (sided.canExtractItem(slot, stack, dir.getOpposite())) {
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
				if (!((BlockManipulator) getBlockState().getBlock()).input) {
					world.setBlockState(pos, DeferredRegisters.blockManipulatorInput.getDefaultState().with(BlockManipulator.FACING, getFacing()));
				}
			}
		}
	}

	@Override
	public TransferPack receivePower(TransferPack transfer, Direction dir, boolean debug) {
		if (!canConnectElectrically(dir)) {
			return TransferPack.EMPTY;
		}
		double received = Math.min(transfer.getJoules(), Constants.MANIPULATOR_USAGE * 200 - joules);
		if (!debug) {
			joules += received;
		}
		return TransferPack.joulesVoltage(received, transfer.getVoltage());
	}

	@Override
	public boolean canConnectElectrically(Direction dir) {
		return dir == Direction.DOWN;
	}

}
