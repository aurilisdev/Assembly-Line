package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.electric.CapabilityElectrodynamic;
import electrodynamics.api.tile.electric.IElectrodynamic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
		if (capability == CapabilityElectrodynamic.ELECTRODYNAMIC && facing == Direction.DOWN) {
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
		return joules * 200;
	}
}
