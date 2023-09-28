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

import java.util.ArrayList;
import java.util.List;

import assemblyline.References;
import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.common.blockitem.types.BlockItemDescriptable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);

	static {
		ITEMS.register("conveyorbelt", () -> new BlockItemDescriptable(() -> blockConveyorBelt, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("sorterbelt", () -> new BlockItemDescriptable(() -> blockSorterBelt, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("detector", () -> new BlockItemDescriptable(() -> blockDetector, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("crate", () -> new BlockItemDescriptable(() -> blockCrate, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("cratemedium", () -> new BlockItemDescriptable(() -> blockCrateMedium, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("cratelarge", () -> new BlockItemDescriptable(() -> blockCrateLarge, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("autocrafter", () -> new BlockItemDescriptable(() -> blockAutocrafter, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("blockbreaker", () -> new BlockItemDescriptable(() -> blockBlockBreaker, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("blockplacer", () -> new BlockItemDescriptable(() -> blockBlockPlacer, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("rancher", () -> new BlockItemDescriptable(() -> blockRancher, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("mobgrinder", () -> new BlockItemDescriptable(() -> blockMobGrinder, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));
		ITEMS.register("farmer", () -> new BlockItemDescriptable(() -> blockFarmer, new Properties(), () -> AssemblyLineCreativeTabs.MAIN.get()));

	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	private static class ElectroCreativeRegistry {

		@SubscribeEvent
		public static void registerItems(BuildCreativeModeTabContentsEvent event) {

			ITEMS.getEntries().forEach(reg -> {

				CreativeTabSupplier supplier = (CreativeTabSupplier) reg.get();

				if (supplier.hasCreativeTab() && supplier.isAllowedInCreativeTab(event.getTab())) {
					List<ItemStack> toAdd = new ArrayList<>();
					supplier.addCreativeModeItems(event.getTab(), toAdd);
					event.acceptAll(toAdd);
				}

			});

		}

	}

}
