package assemblyline.registers;

import static assemblyline.registers.AssemblyLineBlocks.blockAutocrafter;
import static assemblyline.registers.AssemblyLineBlocks.blockBlockBreaker;
import static assemblyline.registers.AssemblyLineBlocks.blockBlockPlacer;
import static assemblyline.registers.AssemblyLineBlocks.blockConveyorBelt;
import static assemblyline.registers.AssemblyLineBlocks.blockCrate;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateLarge;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateMedium;
import static assemblyline.registers.AssemblyLineBlocks.blockDetector;
import static assemblyline.registers.AssemblyLineBlocks.blockFarmer;
import static assemblyline.registers.AssemblyLineBlocks.blockMobGrinder;
import static assemblyline.registers.AssemblyLineBlocks.blockRancher;
import static assemblyline.registers.AssemblyLineBlocks.blockSorterBelt;

import com.google.common.collect.Sets;

import assemblyline.References;
import assemblyline.common.tile.TileAutocrafter;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileDetector;
import assemblyline.common.tile.TileFarmer;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.common.tile.TileRancher;
import assemblyline.common.tile.TileSorterBelt;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyLineBlockTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, References.ID);
	public static final RegistryObject<BlockEntityType<TileConveyorBelt>> TILE_BELT = BLOCK_ENTITY_TYPES.register("belt", () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
	public static final RegistryObject<BlockEntityType<TileDetector>> TILE_DETECTOR = BLOCK_ENTITY_TYPES.register("detector", () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
	public static final RegistryObject<BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = BLOCK_ENTITY_TYPES.register("sorterbelt", () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt), null));
	public static final RegistryObject<BlockEntityType<TileCrate>> TILE_CRATE = BLOCK_ENTITY_TYPES.register("crate", () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate, blockCrateMedium, blockCrateLarge), null));
	public static final RegistryObject<BlockEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = BLOCK_ENTITY_TYPES.register("autocrafter", () -> new BlockEntityType<>(TileAutocrafter::new, Sets.newHashSet(blockAutocrafter), null));
	public static final RegistryObject<BlockEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = BLOCK_ENTITY_TYPES.register("blockbreaker", () -> new BlockEntityType<>(TileBlockBreaker::new, Sets.newHashSet(blockBlockBreaker), null));
	public static final RegistryObject<BlockEntityType<TileBlockPlacer>> TILE_BLOCKPLACER = BLOCK_ENTITY_TYPES.register("blockplacer", () -> new BlockEntityType<>(TileBlockPlacer::new, Sets.newHashSet(blockBlockPlacer), null));
	public static final RegistryObject<BlockEntityType<TileRancher>> TILE_RANCHER = BLOCK_ENTITY_TYPES.register("rancher", () -> new BlockEntityType<>(TileRancher::new, Sets.newHashSet(blockRancher), null));
	public static final RegistryObject<BlockEntityType<TileMobGrinder>> TILE_MOBGRINDER = BLOCK_ENTITY_TYPES.register("mobgrinder", () -> new BlockEntityType<>(TileMobGrinder::new, Sets.newHashSet(blockMobGrinder), null));
	public static final RegistryObject<BlockEntityType<TileFarmer>> TILE_FARMER = BLOCK_ENTITY_TYPES.register("farmer", () -> new BlockEntityType<>(TileFarmer::new, Sets.newHashSet(blockFarmer), null));
}
