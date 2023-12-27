package assemblyline.datagen.client;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.client.ElectrodynamicsBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AssemblyLineBlockStateProvider extends ElectrodynamicsBlockStateProvider {

	public AssemblyLineBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, exFileHelper, References.ID);
	}

	@Override
	protected void registerStatesAndModels() {

		simpleColumnBlock(AssemblyLineBlocks.blockCrate, blockLoc("crate"), blockLoc("cratetop"), true);
		simpleColumnBlock(AssemblyLineBlocks.blockCrateMedium, blockLoc("cratemedium"), blockLoc("cratetop"), true);
		simpleColumnBlock(AssemblyLineBlocks.blockCrateLarge, blockLoc("cratelarge"), blockLoc("cratetop"), true);

		horrRotatedBlock(AssemblyLineBlocks.blockConveyorBelt, existingBlock(blockLoc("conveyorbelt")), true);
		horrRotatedBlock(AssemblyLineBlocks.blockDetector, existingBlock(AssemblyLineBlocks.blockDetector), true);
		horrRotatedLitBlock(AssemblyLineBlocks.blockSorterBelt, existingBlock(AssemblyLineBlocks.blockSorterBelt), existingBlock(blockLoc("sorterbeltrunning")), true);

	}

}
