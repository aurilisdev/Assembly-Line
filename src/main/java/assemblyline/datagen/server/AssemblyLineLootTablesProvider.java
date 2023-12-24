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

	}

}
