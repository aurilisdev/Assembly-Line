package assemblyline.registers;

import static assemblyline.registers.AssemblyLineBlocks.blockConveyorBelt;
import static assemblyline.registers.AssemblyLineBlocks.blockCrate;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateLarge;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateMedium;
import static assemblyline.registers.AssemblyLineBlocks.blockDetector;
import static assemblyline.registers.AssemblyLineBlocks.blockSorterBelt;

import com.google.common.collect.Sets;

import assemblyline.References;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileDetector;
import assemblyline.common.tile.TileSorterBelt;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineBlockTypes {
	public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.ID);
	public static final RegistryObject<TileEntityType<TileConveyorBelt>> TILE_BELT = BLOCK_ENTITY_TYPES.register("belt", () -> new TileEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
	public static final RegistryObject<TileEntityType<TileDetector>> TILE_DETECTOR = BLOCK_ENTITY_TYPES.register("detector", () -> new TileEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
	public static final RegistryObject<TileEntityType<TileSorterBelt>> TILE_SORTERBELT = BLOCK_ENTITY_TYPES.register("sorterbelt", () -> new TileEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt), null));
	public static final RegistryObject<TileEntityType<TileCrate>> TILE_CRATE = BLOCK_ENTITY_TYPES.register("crate", () -> new TileEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate, blockCrateMedium, blockCrateLarge), null));

}
