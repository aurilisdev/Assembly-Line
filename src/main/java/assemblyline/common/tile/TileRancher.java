package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerRancher;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IForgeShearable;

public class TileRancher extends GenericTile {

	public static final int MAX_WAIT_TICKS = 200;
	private AABB checkArea;
	private int ticksSinceCheck = 0;
	//This is the percieved radius; the true radius has to be 1 larger to handle clipping issues
	private static final int CHECK_WIDTH = 5;
	private static final int CHECK_HEIGHT = 5;
	private static final int CHECK_LENGTH = 5;
	
	private static ItemStack SHEERS = new ItemStack(Items.SHEARS);
	
	
	public TileRancher(BlockPos worldPos, BlockState blockState) {
		super(DeferredRegisters.TILE_RANCHER.get(), worldPos, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentTickable().tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE)
				.maxJoules(Constants.RANCHER_USAGE * 20));
		addComponent(new ComponentInventory(this).size(9).outputs(9).valid(machineValidator()).shouldSendInfo());
		addComponent(new ComponentContainerProvider("container.rancher")
				.createMenu((id, player) -> new ContainerRancher(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}
	
	private void tickServer(ComponentTickable tickable) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		if (tickable.getTicks() % 20 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		if(inv.areOutputsEmpty() && (electro.getJoulesStored() >= Constants.RANCHER_USAGE)) {
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
					List<ItemStack> collectedItems = new ArrayList<>();
					for(Entity entity : entities) {
						if((electro.getJoulesStored() >= Constants.RANCHER_USAGE) && entity instanceof IForgeShearable sheep && sheep.isShearable(SHEERS, level, entity.blockPosition())) {
							collectedItems.addAll(sheep.onSheared(null, SHEERS, level, entity.blockPosition(), 0));
							electro.extractPower(TransferPack.joulesVoltage(Constants.RANCHER_USAGE, electro.getVoltage()), false);
						}
					}
					if(collectedItems.size() > 0) {
						ComponentInventory.addItemsToInventory(inv, collectedItems, inv.getOutputStartIndex(), inv.getOutputContents().size());
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
