package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.common.tile.belt.TileConveyorBelt;
import assemblyline.common.tile.belt.TileSorterBelt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import voltaic.api.registration.BulkDeferredHolder;
import voltaic.common.block.BlockMachine;
import voltaic.common.block.voxelshapes.VoxelShapeProvider;

public class AssemblyLineBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, AssemblyLine.ID);

    public static final DeferredHolder<Block, BlockConveyorBelt> BLOCK_CONVEYORBELT = BLOCKS.register("conveyorbelt", () -> new BlockConveyorBelt(AssemblyLineVoxelShapes.CONVEYORBELT, TileConveyorBelt::new));
    public static final DeferredHolder<Block, BlockConveyorBelt> BLOCK_SORTERBELT = BLOCKS.register("sorterbelt", () -> new BlockConveyorBelt(VoxelShapeProvider.DEFAULT, TileSorterBelt::new));
    public static final DeferredHolder<Block, BlockDetector> BLOCK_DETECTOR = BLOCKS.register("detector", BlockDetector::new);
    public static final BulkDeferredHolder<Block, BlockMachine, SubtypeAssemblyMachine> BLOCKS_ASSEMBLYMACHINES = new BulkDeferredHolder<>(SubtypeAssemblyMachine.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockMachine(subtype)));

}
