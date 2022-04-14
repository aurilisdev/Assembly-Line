package assemblyline;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import assemblyline.common.block.BlockBlockBreaker;
import assemblyline.common.block.BlockBlockPlacer;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockCrate;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockFarmer;
import assemblyline.common.block.BlockMobGrinder;
import assemblyline.common.block.BlockRancher;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.ContainerSorterBelt;
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
import electrodynamics.common.blockitem.BlockItemDescriptable;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class DeferredRegisters {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.ID);
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);
	public static BlockConveyorBelt blockConveyorBelt;
	public static BlockSorterBelt blockSorterBelt;
	public static BlockSorterBelt blockSorterBeltRunning;
	public static BlockDetector blockDetector;
	public static BlockCrate blockCrate;
	public static BlockCrate blockCrateMedium;
	public static BlockCrate blockCrateLarge;
	public static GenericMachineBlock blockAutocrafter;
	public static BlockBlockBreaker blockBlockBreaker;
	public static BlockBlockPlacer blockBlockPlacer;
	public static BlockRancher blockRancher;
	public static BlockMobGrinder blockMobGrinder;
	public static BlockFarmer blockFarmer;

	static {
		BLOCKS.register("conveyorbelt", supplier(() -> blockConveyorBelt = new BlockConveyorBelt()));
		BLOCKS.register("sorterbelt", supplier(() -> blockSorterBelt = new BlockSorterBelt(false)));
		BLOCKS.register("sorterbeltrunning", supplier(() -> blockSorterBeltRunning = new BlockSorterBelt(true)));
		BLOCKS.register("detector", supplier(() -> blockDetector = new BlockDetector()));
		BLOCKS.register("crate", supplier(() -> blockCrate = new BlockCrate(64)));
		BLOCKS.register("cratemedium", supplier(() -> blockCrateMedium = new BlockCrate(128)));
		BLOCKS.register("cratelarge", supplier(() -> blockCrateLarge = new BlockCrate(256)));
		BLOCKS.register("autocrafter", supplier(() -> blockAutocrafter = new GenericMachineBlock(TileAutocrafter::new)));
		BLOCKS.register("blockbreaker", supplier(() -> blockBlockBreaker = new BlockBlockBreaker()));
		BLOCKS.register("blockplacer", supplier(() -> blockBlockPlacer = new BlockBlockPlacer()));
		BLOCKS.register("rancher", supplier(() -> blockRancher = new BlockRancher()));
		BLOCKS.register("mobgrinder", supplier(() -> blockMobGrinder = new BlockMobGrinder()));
		BLOCKS.register("farmer", supplier(() -> blockFarmer = new BlockFarmer()));
		ITEMS.register("conveyorbelt", supplier(() -> new BlockItemDescriptable(() -> blockConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("sorterbelt", supplier(() -> new BlockItemDescriptable(() -> blockSorterBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("sorterbeltrunning", supplier(() -> new BlockItemDescriptable(() -> blockSorterBeltRunning, new Properties())));
		ITEMS.register("detector", supplier(() -> new BlockItemDescriptable(() -> blockDetector, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("crate", supplier(() -> new BlockItemDescriptable(() -> blockCrate, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("cratemedium", supplier(() -> new BlockItemDescriptable(() -> blockCrateMedium, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("cratelarge", supplier(() -> new BlockItemDescriptable(() -> blockCrateLarge, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("autocrafter", supplier(() -> new BlockItemDescriptable(() -> blockAutocrafter, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("blockbreaker", supplier(() -> new BlockItemDescriptable(() -> blockBlockBreaker, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("blockplacer", supplier(() -> new BlockItemDescriptable(() -> blockBlockPlacer, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("rancher", supplier(() -> new BlockItemDescriptable(() -> blockRancher, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("mobgrinder", supplier(() -> new BlockItemDescriptable(() -> blockMobGrinder, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("farmer", supplier(() -> new BlockItemDescriptable(() -> blockFarmer, new Properties().tab(References.ASSEMBLYLINETAB))));

		// MACHINES
		BlockItemDescriptable.addDescription(() -> blockConveyorBelt, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockSorterBelt, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockAutocrafter, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockBlockPlacer, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockBlockBreaker, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockRancher, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockMobGrinder, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockFarmer, "|translate|tooltip.voltage.120");

		// Misc
		BlockItemDescriptable.addDescription(() -> blockDetector, "|translate|tooltip.detector");
		BlockItemDescriptable.addDescription(() -> blockCrate, "|translate|tooltip.crate");
		BlockItemDescriptable.addDescription(() -> blockCrateMedium, "|translate|tooltip.cratemedium");
		BlockItemDescriptable.addDescription(() -> blockCrateLarge, "|translate|tooltip.cratelarge");
	}
	public static final RegistryObject<BlockEntityType<TileConveyorBelt>> TILE_BELT = TILES.register("belt", () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
	public static final RegistryObject<BlockEntityType<TileDetector>> TILE_DETECTOR = TILES.register("detector", () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
	public static final RegistryObject<BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = TILES.register("sorterbelt", () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt, blockSorterBeltRunning), null));
	public static final RegistryObject<BlockEntityType<TileCrate>> TILE_CRATE = TILES.register("crate", () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate, blockCrateMedium, blockCrateLarge), null));
	public static final RegistryObject<BlockEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = TILES.register("autocrafter", () -> new BlockEntityType<>(TileAutocrafter::new, Sets.newHashSet(blockAutocrafter), null));
	public static final RegistryObject<BlockEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = TILES.register("blockbreaker", () -> new BlockEntityType<>(TileBlockBreaker::new, Sets.newHashSet(blockBlockBreaker), null));
	public static final RegistryObject<BlockEntityType<TileBlockPlacer>> TILE_BLOCKPLACER = TILES.register("blockplacer", () -> new BlockEntityType<>(TileBlockPlacer::new, Sets.newHashSet(blockBlockPlacer), null));
	public static final RegistryObject<BlockEntityType<TileRancher>> TILE_RANCHER = TILES.register("rancher", () -> new BlockEntityType<>(TileRancher::new, Sets.newHashSet(blockRancher), null));
	public static final RegistryObject<BlockEntityType<TileMobGrinder>> TILE_MOBGRINDER = TILES.register("mobgrinder", () -> new BlockEntityType<>(TileMobGrinder::new, Sets.newHashSet(blockMobGrinder), null));
	public static final RegistryObject<BlockEntityType<TileFarmer>> TILE_FARMER = TILES.register("farmer", () -> new BlockEntityType<>(TileFarmer::new, Sets.newHashSet(blockFarmer), null));

	public static final RegistryObject<MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = CONTAINERS.register("sorterbelt", () -> new MenuType<>(ContainerSorterBelt::new));
	public static final RegistryObject<MenuType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = CONTAINERS.register("autocrafter", () -> new MenuType<>(ContainerAutocrafter::new));
	public static final RegistryObject<MenuType<ContainerBlockPlacer>> CONTAINER_BLOCKPLACER = CONTAINERS.register("blockplacer", () -> new MenuType<>(ContainerBlockPlacer::new));
	public static final RegistryObject<MenuType<ContainerBlockBreaker>> CONTAINER_BLOCKBREAKER = CONTAINERS.register("blockbreaker", () -> new MenuType<>(ContainerBlockBreaker::new));
	public static final RegistryObject<MenuType<ContainerFrontHarvester>> CONTAINER_HARVESTER = CONTAINERS.register("harvester", () -> new MenuType<>(ContainerFrontHarvester::new));
	public static final RegistryObject<MenuType<ContainerFarmer>> CONTAINER_FARMER = CONTAINERS.register("farmer", () -> new MenuType<>(ContainerFarmer::new));

	private static <T extends ForgeRegistryEntry<T>> Supplier<? extends T> supplier(Supplier<? extends T> entry) {
		return entry;
	}

}
