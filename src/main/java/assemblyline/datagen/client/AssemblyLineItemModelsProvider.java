package assemblyline.datagen.client;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.registers.AssemblyLineBlocks;
import assemblyline.registers.AssemblyLineItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.common.data.ExistingFileHelper;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.datagen.utils.client.BaseItemModelsProvider;

public class AssemblyLineItemModelsProvider extends BaseItemModelsProvider {

	public AssemblyLineItemModelsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, existingFileHelper, AssemblyLine.ID);
	}

	@Override
	protected void registerModels() {

		simpleBlockItem(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker), existingBlock(blockLoc("blockbreaker")));
		simpleBlockItem(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder), existingBlock(blockLoc("mobgrinder")));

		layeredBuilder(name(AssemblyLineItems.ITEM_SPEEDUPGRADE_BASIC), Parent.GENERATED, itemLoc("upgrade/" + SubtypeItemUpgrade.basicspeed.tag())).transforms().transform(Perspective.GUI).scale(0.8F).end();
		layeredBuilder(name(AssemblyLineItems.ITEM_SPEEDUPGRADE_ADVANCED), Parent.GENERATED, itemLoc("upgrade/" + SubtypeItemUpgrade.advancedspeed.tag())).transforms().transform(Perspective.GUI).scale(0.8F).end();
		layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADEITEMINPUT), Parent.GENERATED, itemLoc("upgrade/" + SubtypeItemUpgrade.iteminput.tag())).transforms().transform(Perspective.GUI).scale(0.8F).end();
		layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADEITEMOUTPUT), Parent.GENERATED, itemLoc("upgrade/" + SubtypeItemUpgrade.itemoutput.tag())).transforms().transform(Perspective.GUI).scale(0.8F).end();
		layeredBuilder(name(AssemblyLineItems.ITEM_UPGRADERANGE), Parent.GENERATED, itemLoc("upgrade/" + SubtypeItemUpgrade.range.tag())).transforms().transform(Perspective.GUI).scale(0.8F).end();

	}

}
