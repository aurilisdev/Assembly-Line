package assemblyline.datagen.server;

import assemblyline.registers.AssemblyLineBlockTypes;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.server.ElectrodynamicsLootTablesProvider;
import net.minecraft.data.DataGenerator;

public class AssemblyLineLootTablesProvider extends ElectrodynamicsLootTablesProvider {

	public AssemblyLineLootTablesProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addTables() {
		
		addSimpleBlock(AssemblyLineBlocks.blockDetector);
		
		addITable(AssemblyLineBlocks.blockCrate, AssemblyLineBlockTypes.TILE_CRATE);
		addITable(AssemblyLineBlocks.blockCrateMedium, AssemblyLineBlockTypes.TILE_CRATE);
		addITable(AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlockTypes.TILE_CRATE);
		
		addIETable(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlockTypes.TILE_AUTOCRAFTER);
		addIETable(AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlockTypes.TILE_BLOCKBREAKER);
		addIETable(AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlockTypes.TILE_BLOCKPLACER);
		addIETable(AssemblyLineBlocks.blockRancher, AssemblyLineBlockTypes.TILE_RANCHER);
		addIETable(AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlockTypes.TILE_MOBGRINDER);
		addIETable(AssemblyLineBlocks.blockFarmer, AssemblyLineBlockTypes.TILE_FARMER);
		
	}
	

}
