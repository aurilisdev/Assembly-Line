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

import assemblyline.References;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
	static {
		ITEMS.register("conveyorbelt", () -> new BlockItemDescriptable(() -> blockConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("sorterbelt", () -> new BlockItemDescriptable(() -> blockSorterBelt, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("detector", () -> new BlockItemDescriptable(() -> blockDetector, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("crate", () -> new BlockItemDescriptable(() -> blockCrate, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("cratemedium", () -> new BlockItemDescriptable(() -> blockCrateMedium, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("cratelarge", () -> new BlockItemDescriptable(() -> blockCrateLarge, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("autocrafter", () -> new BlockItemDescriptable(() -> blockAutocrafter, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("blockbreaker", () -> new BlockItemDescriptable(() -> blockBlockBreaker, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("blockplacer", () -> new BlockItemDescriptable(() -> blockBlockPlacer, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("rancher", () -> new BlockItemDescriptable(() -> blockRancher, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("mobgrinder", () -> new BlockItemDescriptable(() -> blockMobGrinder, new Properties().tab(References.ASSEMBLYLINETAB)));
		ITEMS.register("farmer", () -> new BlockItemDescriptable(() -> blockFarmer, new Properties().tab(References.ASSEMBLYLINETAB)));

	}
}
