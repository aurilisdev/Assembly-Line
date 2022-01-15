package assemblyline.common.tile;

import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerMobGrinder;
import assemblyline.common.settings.Constants;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileMobGrinder extends GenericTile {

	public static final int MAX_WAIT_TICKS = 200;
	private AABB checkArea;
	private int ticksSinceCheck = 0;
	//This is the percieved radius; the true radius has to be 1 larger to handle clipping issues
	private static final int CHECK_WIDTH = 5;
	private static final int CHECK_HEIGHT = 5;
	private static final int CHECK_LENGTH = 5;
	
	public TileMobGrinder(BlockPos worldPos, BlockState blockState) {
		super(DeferredRegisters.TILE_MOBGRINDER.get(), worldPos, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentTickable().tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE)
				.maxJoules(Constants.MOBGRINDER_USAGE * 20));
		addComponent(new ComponentInventory(this).size(9).outputs(9).valid(machineValidator()).shouldSendInfo());
		addComponent(new ComponentContainerProvider("container.mobgrinder")
				.createMenu((id, player) -> new ContainerMobGrinder(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}
	
	private void tickServer(ComponentTickable tickable) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		if (tickable.getTicks() % 20 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		if(inv.areOutputsEmpty() && (electro.getJoulesStored() >= Constants.MOBGRINDER_USAGE)) {
			if(ticksSinceCheck == 0) {
				ComponentDirection dir = getComponent(ComponentType.Direction);
				BlockPos machinePos = getBlockPos();
				int deltaX;
				int deltaZ;
				if(checkArea == null) {
					BlockPos blockInFront = machinePos.relative(dir.getDirection());
					BlockPos startPos;
					BlockPos endPos;
					deltaX = blockInFront.getX() - machinePos.getX();
					deltaZ = blockInFront.getZ() - machinePos.getZ();
					int yShift = CHECK_HEIGHT - 1;
					if(deltaX == 0) {
						int xShift = deltaZ * (CHECK_WIDTH + 1) / 2;
						int zShift = deltaZ * CHECK_LENGTH;
						startPos = new BlockPos(blockInFront.getX() + xShift, blockInFront.getY() + yShift, blockInFront.getZ() + zShift);
						endPos = new BlockPos(blockInFront.getX() - xShift, blockInFront.getY(), blockInFront.getZ() - deltaZ);
						checkArea = new AABB(startPos, endPos);
					} else if(deltaZ == 0) {
						int xShift = deltaX * CHECK_WIDTH;
						int zShift = deltaX * (CHECK_LENGTH + 2) / 2;
						startPos = new BlockPos(blockInFront.getX() + xShift, blockInFront.getY() + yShift, blockInFront.getZ() + zShift);
						endPos = new BlockPos(blockInFront.getX() - deltaX, blockInFront.getY(), blockInFront.getZ() - zShift);
						checkArea = new AABB(startPos, endPos);
					} 
				}
				if(checkArea != null){
					List<Entity> entities = level.getEntities(null, checkArea);
					for(Entity entity : entities) {
						if((electro.getJoulesStored() >= Constants.RANCHER_USAGE) && !(entity instanceof Player)) {
							electro.extractPower(TransferPack.joulesVoltage(Constants.MOBGRINDER_USAGE, electro.getVoltage()), false);
							entity.getCapability(ElectrodynamicsCapabilities.LOCATION_STORAGE_CAPABILITY).ifPresent(h -> h.setLocation(0, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()));
							entity.kill();
						}
					}
				}
			}
			ticksSinceCheck++;
			if(ticksSinceCheck >= MAX_WAIT_TICKS) {
				ticksSinceCheck = 0;
			}
		}
	}

}
