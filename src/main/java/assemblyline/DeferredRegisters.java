package assemblyline;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import assemblyline.common.block.BlockBlockBreaker;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockCrate;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.tile.TileAutocrafter;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileDetector;
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
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class DeferredRegisters {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);
    public static BlockConveyorBelt blockConveyorBelt = new BlockConveyorBelt();
    public static BlockSorterBelt blockSorterBelt = new BlockSorterBelt(false);
    public static BlockSorterBelt blockSorterBeltRunning = new BlockSorterBelt(true);
    public static BlockDetector blockDetector = new BlockDetector();
    public static BlockCrate blockCrate = new BlockCrate(64);
    public static BlockCrate blockCrateMedium = new BlockCrate(128);
    public static BlockCrate blockCrateLarge = new BlockCrate(256);
    public static GenericMachineBlock blockAutocrafter;
    public static BlockBlockBreaker blockBlockBreaker;

    static {
	BLOCKS.register("conveyorbelt", supplier(blockConveyorBelt));
	BLOCKS.register("sorterbelt", supplier(blockSorterBelt));
	BLOCKS.register("sorterbeltrunning", supplier(blockSorterBeltRunning));
	BLOCKS.register("detector", supplier(blockDetector));
	BLOCKS.register("crate", supplier(blockCrate));
	BLOCKS.register("cratemedium", supplier(blockCrateMedium));
	BLOCKS.register("cratelarge", supplier(blockCrateLarge));
	BLOCKS.register("autocrafter", supplier(blockAutocrafter = new GenericMachineBlock(TileAutocrafter::new)));
	BLOCKS.register("blockbreaker", supplier(blockBlockBreaker = new BlockBlockBreaker()));
	ITEMS.register("conveyorbelt", supplier(new BlockItemDescriptable(blockConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbelt", supplier(new BlockItemDescriptable(blockSorterBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbeltrunning", supplier(new BlockItemDescriptable(blockSorterBeltRunning, new Properties())));
	ITEMS.register("detector", supplier(new BlockItemDescriptable(blockDetector, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("crate", supplier(new BlockItemDescriptable(blockCrate, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("cratemedium", supplier(new BlockItemDescriptable(blockCrateMedium, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("cratelarge", supplier(new BlockItemDescriptable(blockCrateLarge, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("autocrafter", supplier(new BlockItemDescriptable(blockAutocrafter, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("blockbreaker", supplier(new BlockItemDescriptable(blockBlockBreaker, new Properties().tab(References.ASSEMBLYLINETAB))));
	BlockItemDescriptable.addDescription(blockConveyorBelt, "|translate|tooltip.conveyorbelt.powerusage");
	BlockItemDescriptable.addDescription(blockSorterBelt, "|translate|tooltip.sorterbelt.powerusage");
	BlockItemDescriptable.addDescription(blockDetector, "|translate|tooltip.detector");
    }
    public static final RegistryObject<BlockEntityType<TileConveyorBelt>> TILE_BELT = TILES.register("belt",
	    () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
    public static final RegistryObject<BlockEntityType<TileDetector>> TILE_DETECTOR = TILES.register("detector",
	    () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
    public static final RegistryObject<BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = TILES.register("sorterbelt",
	    () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt, blockSorterBeltRunning), null));
    public static final RegistryObject<BlockEntityType<TileCrate>> TILE_CRATE = TILES.register("crate",
	    () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate, blockCrateMedium, blockCrateLarge), null));
    public static final RegistryObject<BlockEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = TILES.register("autocrafter",
	    () -> new BlockEntityType<>(TileAutocrafter::new, Sets.newHashSet(blockAutocrafter), null));
    public static final RegistryObject<BlockEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = TILES.register("blockbreaker",
	    () -> new BlockEntityType<>(TileBlockBreaker::new, Sets.newHashSet(blockBlockBreaker), null));
    public static final RegistryObject<MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = CONTAINERS.register("sorterbelt",
	    () -> new MenuType<>(ContainerSorterBelt::new));
    public static final RegistryObject<MenuType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = CONTAINERS.register("autocrafter",
	    () -> new MenuType<>(ContainerAutocrafter::new));

    private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry) {
	return () -> entry;
    }
}
