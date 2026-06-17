package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.common.tile.belt.TileConveyorBelt;
import assemblyline.common.tile.belt.TileSorterBelt;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import voltaic.api.registration.BulkRegistryObject;
import voltaic.common.block.BlockMachine;
import voltaic.common.block.voxelshapes.VoxelShapeProvider;

public class AssemblyLineBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
	    AssemblyLine.ID);

    public static final RegistryObject<BlockConveyorBelt> BLOCK_CONVEYORBELT = BLOCKS.register("conveyorbelt",
	    () -> new BlockConveyorBelt(AssemblyLineVoxelShapes.CONVEYORBELT, TileConveyorBelt::new));
    public static final RegistryObject<BlockConveyorBelt> BLOCK_SORTERBELT = BLOCKS.register("sorterbelt",
	    () -> new BlockConveyorBelt(VoxelShapeProvider.DEFAULT, TileSorterBelt::new));
    public static final RegistryObject<BlockDetector> BLOCK_DETECTOR = BLOCKS.register("detector", BlockDetector::new);
    public static final BulkRegistryObject<BlockMachine, SubtypeAssemblyMachine> BLOCKS_ASSEMBLYMACHINES = new BulkRegistryObject<>(
	    SubtypeAssemblyMachine.values(),
	    subtype -> BLOCKS.register(subtype.tag(), () -> new BlockMachine(subtype)));

}
