package assemblyline.registers;

import java.util.ArrayList;
import java.util.List;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import voltaic.api.creativetab.CreativeTabSupplier;
import voltaic.api.registration.BulkDeferredHolder;
import voltaic.common.blockitem.BlockItemDescriptable;

public class AssemblyLineItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, AssemblyLine.ID);

    public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_CONVEYORBELT = ITEMS.register("conveyorbelt",
	    () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(), new Properties(),
		    AssemblyLineCreativeTabs.MAIN));
    public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_SORTERBELT = ITEMS.register("sorterbelt",
	    () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_SORTERBELT.get(), new Properties(),
		    AssemblyLineCreativeTabs.MAIN));
    public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_DETECTOR = ITEMS.register("detector",
	    () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_DETECTOR.get(), new Properties(),
		    AssemblyLineCreativeTabs.MAIN));

    public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeAssemblyMachine> ITEMS_ASSEMBLYMACHINE = new BulkDeferredHolder<>(
	    SubtypeAssemblyMachine.values(),
	    subtype -> ITEMS.register(subtype.tag(),
		    () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(subtype),
			    new Properties(), AssemblyLineCreativeTabs.MAIN)));

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
