package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import voltaic.api.registration.BulkRegistryObject;
import voltaic.common.blockitem.BlockItemDescriptable;

public class AssemblyLineItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AssemblyLine.ID);

	public static final RegistryObject<BlockItemDescriptable> ITEM_CONVEYORBELT = ITEMS.register("conveyorbelt", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(), new Properties(), () -> AssemblyLineCreativeTabs.MAIN));
	public static final RegistryObject<BlockItemDescriptable> ITEM_SORTERBELT = ITEMS.register("sorterbelt", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_SORTERBELT.get(), new Properties(), () -> AssemblyLineCreativeTabs.MAIN));
	public static final RegistryObject<BlockItemDescriptable> ITEM_DETECTOR = ITEMS.register("detector", () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCK_DETECTOR.get(), new Properties(), () -> AssemblyLineCreativeTabs.MAIN));

	public static final BulkRegistryObject<BlockItemDescriptable, SubtypeAssemblyMachine> ITEMS_ASSEMBLYMACHINE = new BulkRegistryObject<>(SubtypeAssemblyMachine.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(subtype), new Properties(), () -> AssemblyLineCreativeTabs.MAIN)));


}
