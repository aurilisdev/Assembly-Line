package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockManipulator;
import assemblyline.common.settings.Constants;
import electrodynamics.api.tile.electric.CapabilityElectrodynamic;
import electrodynamics.api.tile.electric.IElectrodynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileConveyorBelt extends TileEntity implements IElectrodynamic {
	public double joules = 0;
	public long lastTime = 0;

	public TileConveyorBelt() {
		super(DeferredRegisters.TILE_CONVEYORBELT.get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		Direction check = getBlockState().get(BlockConveyorBelt.FACING);
		;
		if (capability == CapabilityElectrodynamic.ELECTRODYNAMIC && facing != Direction.UP && facing != check && facing != check.getOpposite()) {
			return (LazyOptional<T>) LazyOptional.of(() -> this);
		}
		return super.getCapability(capability, facing);
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

	public void onEntityCollision(Entity entityIn, boolean running) {
		Direction facing = getBlockState().get(BlockConveyorBelt.FACING);
		if (running) {
			if (entityIn.getPosY() > pos.getY() + 4.0 / 16.0) {
				Direction dir = facing.getOpposite();
				entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
				BlockPos next = pos.offset(dir);
				BlockState side = world.getBlockState(next);
				if (entityIn instanceof ItemEntity) {
					ItemEntity itemEntity = (ItemEntity) entityIn;
					if (!itemEntity.getItem().isEmpty()) {
						if (side.getBlock() instanceof BlockManipulator) {
							if (side.get(BlockConveyorBelt.FACING) == dir.getOpposite()) {
								BlockPos chestPos = next.offset(dir);
								TileEntity chestTile = world.getTileEntity(chestPos);
								if (chestTile instanceof IInventory) {
									itemEntity.setItem(HopperTileEntity.putStackInInventoryAllSlots(null, (IInventory) chestTile, itemEntity.getItem(), dir.getOpposite()));
									if (itemEntity.getItem().isEmpty()) {
										itemEntity.remove();
									}
								}
							}
						}
					}
				}
			}
		}
		if (joules < Constants.CONVEYORBELT_USAGE) {
			if (running) {
				world.setBlockState(pos, DeferredRegisters.blockConveyorbelt.getDefaultState().with(BlockConveyorBelt.FACING, facing), 2 | 16);
			}
		} else {
			if (lastTime != world.getGameTime()) {
				joules -= Constants.CONVEYORBELT_USAGE;
				lastTime = world.getGameTime();
				if (!running) {
					world.setBlockState(pos, DeferredRegisters.blockConveyorbeltRunning.getDefaultState().with(BlockConveyorBelt.FACING, facing), 2 | 16);
				}
			}
		}
	}
}
