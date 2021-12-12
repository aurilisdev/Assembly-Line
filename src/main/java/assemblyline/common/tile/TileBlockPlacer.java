package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TileBlockPlacer extends GenericTile {

    public TileBlockPlacer(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_SORTERBELT.get(), worldPosition, blockState);
	addComponent(new ComponentDirection());
	addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient).tickCommon(this::tickCommon));
	addComponent(new ComponentPacketHandler());
	addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.CONVEYORBELT_USAGE * 20).input(Direction.DOWN));
	addComponent(new ComponentInventory(this).size(1));
	addComponent(new ComponentContainerProvider("container.blockplacer")
		.createMenu((id, player) -> new ContainerBlockPlacer(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
    }

    private void tickServer(ComponentTickable tick) {
    }

    private void tickCommon(ComponentTickable tick) {
    }

    private void tickClient(ComponentTickable tick) {
    }

}
