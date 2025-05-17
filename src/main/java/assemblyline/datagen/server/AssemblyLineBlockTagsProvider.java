package assemblyline.datagen.server;

import assemblyline.AssemblyLine;
import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import voltaic.common.block.BlockMachine;

public class AssemblyLineBlockTagsProvider extends BlockTagsProvider {

	public AssemblyLineBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, AssemblyLine.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                //
                AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(),
                //
                AssemblyLineBlocks.BLOCK_SORTERBELT.get(),
                //
                AssemblyLineBlocks.BLOCK_DETECTOR.get()
                //
        ).add(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getAllValuesArray(new BlockMachine[0]));

        tag(BlockTags.NEEDS_STONE_TOOL).add(
                //
                AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(),
                //
                AssemblyLineBlocks.BLOCK_SORTERBELT.get(),
                //
                AssemblyLineBlocks.BLOCK_DETECTOR.get()
                //
        ).add(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getAllValuesArray(new BlockMachine[0]));
	}

}
