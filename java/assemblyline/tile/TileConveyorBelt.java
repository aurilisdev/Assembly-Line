package assemblyline.tile;

import assemblyline.DeferredRegisters;
import electrodynamics.api.tile.electric.IElectricTile;
import electrodynamics.api.tile.electric.IPowerReceiver;
import electrodynamics.api.utilities.TransferPack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class TileConveyorBelt extends TileEntity implements IPowerReceiver, IElectricTile {
	public static final double MAX_JOULES = 100;
	public double joules = 0;
	public long lastTime = 0;

	public TileConveyorBelt() {
		super(DeferredRegisters.TILE_CONVEYORBELT.get());
	}

	@Override
	public TransferPack receivePower(TransferPack transfer, Direction dir, boolean debug) {
		if (!canConnectElectrically(dir)) {
			return TransferPack.EMPTY;
		}
		double received = Math.min(transfer.getJoules(), MAX_JOULES - joules);
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
