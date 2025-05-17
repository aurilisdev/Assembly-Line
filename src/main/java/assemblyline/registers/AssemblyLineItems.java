package assemblyline.registers;

import java.util.ArrayList;
import java.util.List;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import voltaic.Voltaic;
import voltaic.api.creativetab.CreativeTabSupplier;
import voltaic.api.registration.BulkRegistryObject;
import voltaic.common.blockitem.BlockItemDescriptable;
import voltaic.common.item.ItemUpgrade;
import voltaic.common.item.subtype.SubtypeItemUpgrade;

public class AssemblyLineItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, AssemblyLine.ID);

	public static final RegistryObject<BlockItemDescriptable> ITEM_CONVEYORBELT = ITEMS.register("conveyorbelt", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(), new Properties(), AssemblyLineCreativeTabs.MAIN));
	public static final RegistryObject<BlockItemDescriptable> ITEM_SORTERBELT = ITEMS.register("sorterbelt", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_SORTERBELT.get(), new Properties(), AssemblyLineCreativeTabs.MAIN));
	public static final RegistryObject<BlockItemDescriptable> ITEM_DETECTOR = ITEMS.register("detector", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_DETECTOR.get(), new Properties(), AssemblyLineCreativeTabs.MAIN));

	public static final BulkRegistryObject<BlockItemDescriptable, SubtypeAssemblyMachine> ITEMS_ASSEMBLYMACHINE = new BulkRegistryObject<>(SubtypeAssemblyMachine.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(subtype), new Properties(), AssemblyLineCreativeTabs.MAIN)));

	public static final RegistryObject<ItemUpgrade> ITEM_SPEEDUPGRADE_BASIC = ITEMS.register("upgradebasicspeed", () -> new ItemUpgrade(new Item.Properties(), SubtypeItemUpgrade.basicspeed, AssemblyLineCreativeTabs.MAIN) {
		@Override
		public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
			if(Voltaic.isElectroLoaded()) {
				return;
			}
			super.addCreativeModeItems(tab, items);
		}
	});

	public static final RegistryObject<ItemUpgrade> ITEM_SPEEDUPGRADE_ADVANCED = ITEMS.register("upgradeadvancedpeed", () -> new ItemUpgrade(new Item.Properties(), SubtypeItemUpgrade.advancedspeed, AssemblyLineCreativeTabs.MAIN) {
		@Override
		public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
			if(Voltaic.isElectroLoaded()) {
				return;
			}
			super.addCreativeModeItems(tab, items);
		}
	});

	public static final RegistryObject<ItemUpgrade> ITEM_UPGRADEITEMINPUT = ITEMS.register("upgradeiteminput", () -> new ItemUpgrade(new Item.Properties(), SubtypeItemUpgrade.iteminput, AssemblyLineCreativeTabs.MAIN) {
		@Override
		public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
			if(Voltaic.isElectroLoaded()) {
				return;
			}
			super.addCreativeModeItems(tab, items);
		}
	});

	public static final RegistryObject<ItemUpgrade> ITEM_UPGRADEITEMOUTPUT = ITEMS.register("upgradeitemoutput", () -> new ItemUpgrade(new Item.Properties(), SubtypeItemUpgrade.itemoutput, AssemblyLineCreativeTabs.MAIN) {
		@Override
		public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
			if(Voltaic.isElectroLoaded()) {
				return;
			}
			super.addCreativeModeItems(tab, items);
		}
	});

	public static final RegistryObject<ItemUpgrade> ITEM_UPGRADERANGE = ITEMS.register("upgraderange", () -> new ItemUpgrade(new Item.Properties(), SubtypeItemUpgrade.range, AssemblyLineCreativeTabs.MAIN) {
		@Override
		public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
			if(Voltaic.isElectroLoaded()) {
				return;
			}
			super.addCreativeModeItems(tab, items);
		}
	});

	@EventBusSubscriber(value = Dist.CLIENT, modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
	private static class AssemblyCreativeRegistry {

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
