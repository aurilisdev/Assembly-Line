package assemblyline.datagen.client;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AssemblyLineItemModelsProvider extends ElectrodynamicsItemModelsProvider {

	public AssemblyLineItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, existingFileHelper, References.ID);
	}

	@Override
	protected void registerModels() {

		simpleBlockItem(AssemblyLineBlocks.blockBlockBreaker, existingBlock(blockLoc("blockbreaker")));
		simpleBlockItem(AssemblyLineBlocks.blockMobGrinder, existingBlock(blockLoc("mobgrinder")));

	}

}
