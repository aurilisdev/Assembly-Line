package assemblyline.datagen.server;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AssemblyLineBlockTagsProvider extends BlockTagsProvider {

	public AssemblyLineBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlocks.blockConveyorBelt, AssemblyLineBlocks.blockCrate, AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlocks.blockCrateMedium,
				AssemblyLineBlocks.blockDetector, AssemblyLineBlocks.blockFarmer, AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlocks.blockRancher, AssemblyLineBlocks.blockSorterBelt);
		
		tag(BlockTags.NEEDS_STONE_TOOL).add(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlocks.blockConveyorBelt, AssemblyLineBlocks.blockCrate, AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlocks.blockCrateMedium,
				AssemblyLineBlocks.blockDetector, AssemblyLineBlocks.blockFarmer, AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlocks.blockRancher, AssemblyLineBlocks.blockSorterBelt);
		

	}

}
