package assemblyline.datagen.server;

import assemblyline.registers.AssemblyLineTiles;
import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.data.DataGenerator;
import voltaic.datagen.utils.server.loottable.BaseLootTablesProvider;

public class AssemblyLineLootTablesProvider extends BaseLootTablesProvider {

	public AssemblyLineLootTablesProvider(DataGenerator generator) {
		super(generator, AssemblyLine.ID);
	}

	@Override
	protected void addTables() {

		addSimpleBlock(AssemblyLineBlocks.BLOCK_DETECTOR);

        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.crate), AssemblyLineTiles.TILE_CRATE, true, false, false, false, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratemedium), AssemblyLineTiles.TILE_CRATE, true, false, false, false, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratelarge), AssemblyLineTiles.TILE_CRATE, true, false, false, false, false);

        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.autocrafter), AssemblyLineTiles.TILE_AUTOCRAFTER, true, false, false, true, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker), AssemblyLineTiles.TILE_BLOCKBREAKER, true, false, false, true, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockplacer), AssemblyLineTiles.TILE_BLOCKPLACER, true, false, false, true, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.rancher), AssemblyLineTiles.TILE_RANCHER, true, false, false, true, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder), AssemblyLineTiles.TILE_MOBGRINDER, true, false, false, true, false);
        addMachineTable(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.farmer), AssemblyLineTiles.TILE_FARMER, true, false, false, true, false);

	}

}
