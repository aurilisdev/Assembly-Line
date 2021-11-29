package assemblyline.common.tile;

import assemblyline.DeferredRegisters;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;

public class TileAutocrafter extends GenericTile {
    public boolean isPowered = false;

    public TileAutocrafter(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_AUTOCRAFTER.get(), worldPosition, blockState);
	addComponent(new ComponentDirection());
	addComponent(new ComponentElectrodynamic(this).maxJoules(Constants.CONVEYORBELT_USAGE * 20).input(Direction.DOWN));
	addComponent(new ComponentInventory(this).size(10));
	addComponent(new ComponentContainerProvider("container.autocrafter")
		.createMenu((id, player) -> new ContainerAutocrafter(id, player, getComponent(ComponentType.Inventory), new SimpleContainerData(0))));

    }
}
