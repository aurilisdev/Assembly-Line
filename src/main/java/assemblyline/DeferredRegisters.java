package assemblyline;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import assemblyline.common.block.BlockBetterConveyorBelt;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockCrate;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockElevatorBelt;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.tile.TileBetterConveyorBelt;
import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileDetector;
import assemblyline.common.tile.TileElevatorBelt;
import assemblyline.common.tile.TileSorterBelt;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DeferredRegisters {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);
    public static BlockConveyorBelt blockConveyorBelt = new BlockConveyorBelt();
    public static BlockBetterConveyorBelt blockBetterConveyorBelt = new BlockBetterConveyorBelt();
    public static BlockSorterBelt blockSorterBelt = new BlockSorterBelt(false);
    public static BlockSorterBelt blockSorterBeltRunning = new BlockSorterBelt(true);
    public static BlockElevatorBelt blockElevatorBelt = new BlockElevatorBelt();
    public static BlockDetector blockDetector = new BlockDetector();
    public static BlockCrate blockCrate = new BlockCrate(64);
    public static BlockCrate blockCrateMedium = new BlockCrate(128);
    public static BlockCrate blockCrateLarge = new BlockCrate(256);

    static {
	BLOCKS.register("conveyorbelt", supplier(blockConveyorBelt));
	BLOCKS.register("betterconveyorbelt", supplier(blockBetterConveyorBelt));
	BLOCKS.register("sorterbelt", supplier(blockSorterBelt));
	BLOCKS.register("sorterbeltrunning", supplier(blockSorterBeltRunning));
	BLOCKS.register("elevatorbelt", supplier(blockElevatorBelt));
	BLOCKS.register("detector", supplier(blockDetector));
	BLOCKS.register("crate", supplier(blockCrate));
	BLOCKS.register("cratemedium", supplier(blockCrateMedium));
	BLOCKS.register("cratelarge", supplier(blockCrateLarge));
	ITEMS.register("conveyorbelt", supplier(new BlockItemDescriptable(blockConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("betterconveyorbelt",
		supplier(new BlockItemDescriptable(blockBetterConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbelt", supplier(new BlockItemDescriptable(blockSorterBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("sorterbeltrunning", supplier(new BlockItemDescriptable(blockSorterBeltRunning, new Properties())));
	ITEMS.register("elevatorbelt", supplier(new BlockItemDescriptable(blockElevatorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("detector", supplier(new BlockItemDescriptable(blockDetector, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("crate", supplier(new BlockItemDescriptable(blockCrate, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("cratemedium", supplier(new BlockItemDescriptable(blockCrateMedium, new Properties().tab(References.ASSEMBLYLINETAB))));
	ITEMS.register("cratelarge", supplier(new BlockItemDescriptable(blockCrateLarge, new Properties().tab(References.ASSEMBLYLINETAB))));
	BlockItemDescriptable.addDescription(blockConveyorBelt, "|translate|tooltip.conveyorbelt.powerusage");
	BlockItemDescriptable.addDescription(blockSorterBelt, "|translate|tooltip.sorterbelt.powerusage");
	BlockItemDescriptable.addDescription(blockDetector, "|translate|tooltip.detector");
    }
    public static final RegistryObject<BlockEntityType<TileConveyorBelt>> TILE_BELT = TILES.register("belt",
	    () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorBelt), null));
    public static final RegistryObject<BlockEntityType<TileBetterConveyorBelt>> TILE_BETTERBELT = TILES.register("betterbelt",
	    () -> new BlockEntityType<>(TileBetterConveyorBelt::new, Sets.newHashSet(blockBetterConveyorBelt), null));
    public static final RegistryObject<BlockEntityType<TileElevatorBelt>> TILE_ELEVATORBELT = TILES.register("elevatorbelt",
	    () -> new BlockEntityType<>(TileElevatorBelt::new, Sets.newHashSet(blockElevatorBelt), null));
    public static final RegistryObject<BlockEntityType<TileDetector>> TILE_DETECTOR = TILES.register("detector",
	    () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));
    public static final RegistryObject<BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = TILES.register("sorterbelt",
	    () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(blockSorterBelt, blockSorterBeltRunning), null));
    public static final RegistryObject<BlockEntityType<TileCrate>> TILE_CRATE = TILES.register("crate",
	    () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(blockCrate, blockCrateMedium, blockCrateLarge), null));
    public static final RegistryObject<MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = CONTAINERS.register("sorterbelt",
	    () -> new MenuType<>(ContainerSorterBelt::new));

    private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry) {
	return () -> entry;
    }
}
