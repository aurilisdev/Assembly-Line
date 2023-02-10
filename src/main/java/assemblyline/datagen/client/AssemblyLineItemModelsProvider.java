package assemblyline.datagen.client;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
public class AssemblyLineItemModelsProvider extends ElectrodynamicsItemModelsProvider {

	public AssemblyLineItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, existingFileHelper, References.ID);
	}

	@Override
	protected void registerModels() {

		simpleBlockItem(AssemblyLineBlocks.blockBlockBreaker, existingBlock(blockLoc("blockbreaker")));
		simpleBlockItem(AssemblyLineBlocks.blockMobGrinder, existingBlock(blockLoc("mobgrinder")));

	}

}
