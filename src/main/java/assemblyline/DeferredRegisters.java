package assemblyline;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockCrate;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileDetector;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DeferredRegisters {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);
    public static BlockConveyorBelt blockConveyorBelt = new BlockConveyorBelt();
    public static BlockSorterBelt blockSorterBelt = new BlockSorterBelt(false);
    public static BlockSorterBelt blockSorterBeltRunning = new BlockSorterBelt(true);
    public static BlockDetector blockDetector = new BlockDetector();
    public static BlockCrate blockCrate = new BlockCrate();

    static {
	BLOCKS.register("conveyorbelt", supplier(blockConveyorBelt));
	BLOCKS.register("sorterbelt", supplier(blockSorterBelt));
	BLOCKS.register("sorterbeltrunning", supplier(blockSorterBeltRunning));
	BLOCKS.register("detector", supplier(blockDetector));
	BLOCKS.register("crate", supplier(blockCrate));
	ITEMS.register("conveyorbelt", supplier(new BlockItemDescriptable(blockConveyorBelt, new Properties().group(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbelt", supplier(new BlockItemDescriptable(blockSorterBelt, new Properties().group(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbeltrunning", supplier(new BlockItemDescriptable(blockSorterBeltRunning, new Properties())));
	ITEMS.register("detector", supplier(new BlockItemDescriptable(blockDetector, new Properties().group(References.ASSEMBLYLINETAB))));
	ITEMS.register("crate", supplier(new BlockItemDescriptable(blockCrate, new Properties().group(References.ASSEMBLYLINETAB))));
	BlockItemDescriptable.addDescription(blockConveyorBelt, "|translate|tooltip.conveyorbelt.powerusage");
	BlockItemDescriptable.addDescription(blockSorterBelt, "|translate|tooltip.sorterbelt.powerusage");
	BlockItemDescriptable.addDescription(blockDetector, "|translate|tooltip.detector");
    }
    public static final RegistryObject<TileEntityType<TileConveyorBelt>> TILE_BELT = TILES.register("belt",
	    () -> new TileEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
    public static final RegistryObject<TileEntityType<TileDetector>> TILE_DETECTOR = TILES.register("detector",
	    () -> new TileEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
    public static final RegistryObject<TileEntityType<TileSorterBelt>> TILE_SORTERBELT = TILES.register("sorterbelt",
	    () -> new TileEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt, blockSorterBeltRunning), null));
    public static final RegistryObject<TileEntityType<TileCrate>> TILE_CRATE = TILES.register("crate",
	    () -> new TileEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate), null));
    public static final RegistryObject<ContainerType<ContainerSorterBelt>> CONTAINER_SORTERBELT = CONTAINERS.register("sorterbelt",
	    () -> new ContainerType<>(ContainerSorterBelt::new));

    private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry) {
	return () -> entry;
    }
}
