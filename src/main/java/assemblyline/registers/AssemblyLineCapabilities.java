package assemblyline.registers;

import assemblyline.AssemblyLine;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.registers.VoltaicCapabilities;

@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public class AssemblyLineCapabilities {

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {

        /* TILES */

        AssemblyLineTiles.BLOCK_ENTITY_TYPES.getEntries().forEach(entry -> {
            event.registerBlockEntity(VoltaicCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getElectrodynamicCapability(context));
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getFluidHandlerCapability(context));
            event.registerBlockEntity(VoltaicCapabilities.CAPABILITY_GASHANDLER_BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getGasHandlerCapability(context));
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getItemHandlerCapability(context));
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getForgeEnergyCapability(context));
        });

    }

}
