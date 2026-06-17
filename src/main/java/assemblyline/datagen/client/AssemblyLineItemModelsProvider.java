package assemblyline.datagen.client;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import voltaic.datagen.utils.client.BaseItemModelsProvider;

public class AssemblyLineItemModelsProvider extends BaseItemModelsProvider {

    public AssemblyLineItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
	super(output, existingFileHelper, AssemblyLine.ID);
    }

    @Override
    protected void registerModels() {

	simpleBlockItem(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker),
		existingBlock(blockLoc("blockbreaker")));
	simpleBlockItem(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder),
		existingBlock(blockLoc("mobgrinder")));

	// layeredBuilder(name(AssemblyLineItems.ITEM_SPEEDUPGRADE_BASIC),
	// Parent.GENERATED, itemLoc("upgrade/" +
	// SubtypeItemUpgrade.basicspeed.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
	// layeredBuilder(name(AssemblyLineItems.ITEM_SPEEDUPGRADE_ADVANCED),
	// Parent.GENERATED, itemLoc("upgrade/" +
	// SubtypeItemUpgrade.advancedspeed.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
	// layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADEITEMINPUT),
	// Parent.GENERATED, itemLoc("upgrade/" +
	// SubtypeItemUpgrade.iteminput.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
	// layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADEITEMOUTPUT),
	// Parent.GENERATED, itemLoc("upgrade/" +
	// SubtypeItemUpgrade.itemoutput.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
	// layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADERANGE), Parent.GENERATED,
	// itemLoc("upgrade/" +
	// SubtypeItemUpgrade.range.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();

    }

}
