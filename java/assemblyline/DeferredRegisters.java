package assemblyline;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import assemblyline.block.BlockConveyorBelt;
import assemblyline.block.BlockDetector;
import assemblyline.block.BlockManipulator;
import assemblyline.tile.TileConveyorBelt;
import assemblyline.tile.TileDetector;
import assemblyline.tile.TileManipulator;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DeferredRegisters {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.ID);
	public static BlockConveyorBelt blockConveyorbelt;
	public static BlockConveyorBelt blockConveyorbeltRunning;
	public static BlockManipulator blockManipulator;
	public static BlockDetector blockDetector;

	static {
		BLOCKS.register("conveyorbelt", supplier(blockConveyorbelt = new BlockConveyorBelt(false)));
		BLOCKS.register("conveyorbeltrunning", supplier(blockConveyorbeltRunning = new BlockConveyorBelt(true)));
		BLOCKS.register("manipulator", supplier(blockManipulator = new BlockManipulator()));
		BLOCKS.register("detector", supplier(blockDetector = new BlockDetector()));
		ITEMS.register("conveyorbelt", supplier(new BlockItemDescriptable(blockConveyorbelt, new Item.Properties().group(References.CORETAB))));
		ITEMS.register("conveyorbeltrunning", supplier(new BlockItemDescriptable(blockConveyorbeltRunning, new Item.Properties())));
		ITEMS.register("manipulator", supplier(new BlockItemDescriptable(blockManipulator, new Item.Properties().group(References.CORETAB))));
		ITEMS.register("detector", supplier(new BlockItemDescriptable(blockDetector, new Item.Properties().group(References.CORETAB))));
		BlockItemDescriptable.addDescription(blockConveyorbelt, "|translate|tooltip.conveyorbelt.powerusage");
		BlockItemDescriptable.addDescription(blockConveyorbeltRunning, "|translate|tooltip.conveyorbelt.powerusage");
		BlockItemDescriptable.addDescription(blockDetector, "|translate|tooltip.detector");
	}

	public static final RegistryObject<TileEntityType<TileConveyorBelt>> TILE_CONVEYORBELT = TILES.register("conveyorbelt",
			() -> new TileEntityType<>(TileConveyorBelt::new, Sets.newHashSet(blockConveyorbelt, blockConveyorbeltRunning), null));
	public static final RegistryObject<TileEntityType<TileManipulator>> TILE_MANIPULATOR = TILES.register("manipulator", () -> new TileEntityType<>(TileManipulator::new, Sets.newHashSet(blockManipulator), null));
	public static final RegistryObject<TileEntityType<TileDetector>> TILE_DETECTOR = TILES.register("detector", () -> new TileEntityType<>(TileDetector::new, Sets.newHashSet(blockDetector), null));

	private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry) {
		return () -> entry;
	}
}
