package assemblyline.registers;

import static assemblyline.registers.AssemblyLineBlocks.blockConveyorBelt;
import static assemblyline.registers.AssemblyLineBlocks.blockCrate;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateLarge;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateMedium;
import static assemblyline.registers.AssemblyLineBlocks.blockDetector;
import static assemblyline.registers.AssemblyLineBlocks.blockSorterBelt;
import static electrodynamics.registers.UnifiedElectrodynamicsRegister.supplier;

import assemblyline.References;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
	static {
		ITEMS.register("conveyorbelt", supplier(() -> new BlockItemDescriptable(() -> blockConveyorBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("sorterbelt", supplier(() -> new BlockItemDescriptable(() -> blockSorterBelt, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("detector", supplier(() -> new BlockItemDescriptable(() -> blockDetector, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("crate", supplier(() -> new BlockItemDescriptable(() -> blockCrate, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("cratemedium", supplier(() -> new BlockItemDescriptable(() -> blockCrateMedium, new Properties().tab(References.ASSEMBLYLINETAB))));
		ITEMS.register("cratelarge", supplier(() -> new BlockItemDescriptable(() -> blockCrateLarge, new Properties().tab(References.ASSEMBLYLINETAB))));

	}
}
