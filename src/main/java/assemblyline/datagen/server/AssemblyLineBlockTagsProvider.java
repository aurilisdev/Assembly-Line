package assemblyline.datagen.server;

import java.util.concurrent.CompletableFuture;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AssemblyLineBlockTagsProvider extends BlockTagsProvider {

	public AssemblyLineBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlocks.blockConveyorBelt, AssemblyLineBlocks.blockCrate, AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlocks.blockCrateMedium, AssemblyLineBlocks.blockDetector, AssemblyLineBlocks.blockFarmer, AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlocks.blockRancher, AssemblyLineBlocks.blockSorterBelt);

		tag(BlockTags.NEEDS_STONE_TOOL).add(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlocks.blockConveyorBelt, AssemblyLineBlocks.blockCrate, AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlocks.blockCrateMedium, AssemblyLineBlocks.blockDetector, AssemblyLineBlocks.blockFarmer, AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlocks.blockRancher, AssemblyLineBlocks.blockSorterBelt);

	}

}
