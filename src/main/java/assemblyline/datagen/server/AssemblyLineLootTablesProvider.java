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

		addMachineTable(AssemblyLineBlocks.blockCrate, AssemblyLineBlockTypes.TILE_CRATE, true, false, false, false, false);
		addMachineTable(AssemblyLineBlocks.blockCrateMedium, AssemblyLineBlockTypes.TILE_CRATE, true, false, false, false, false);
		addMachineTable(AssemblyLineBlocks.blockCrateLarge, AssemblyLineBlockTypes.TILE_CRATE, true, false, false, false, false);

		addMachineTable(AssemblyLineBlocks.blockAutocrafter, AssemblyLineBlockTypes.TILE_AUTOCRAFTER, true, false, false, true, false);
		addMachineTable(AssemblyLineBlocks.blockBlockBreaker, AssemblyLineBlockTypes.TILE_BLOCKBREAKER, true, false, false, true, false);
		addMachineTable(AssemblyLineBlocks.blockBlockPlacer, AssemblyLineBlockTypes.TILE_BLOCKPLACER, true, false, false, true, false);
		addMachineTable(AssemblyLineBlocks.blockRancher, AssemblyLineBlockTypes.TILE_RANCHER, true, false, false, true, false);
		addMachineTable(AssemblyLineBlocks.blockMobGrinder, AssemblyLineBlockTypes.TILE_MOBGRINDER, true, false, false, true, false);
		addMachineTable(AssemblyLineBlocks.blockFarmer, AssemblyLineBlockTypes.TILE_FARMER, true, false, false, true, false);

	}

}
