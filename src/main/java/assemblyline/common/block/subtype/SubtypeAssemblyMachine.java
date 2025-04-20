package assemblyline.common.block.subtype;

import assemblyline.common.block.AssemblyLineVoxelShapes;
import assemblyline.common.tile.TileAutocrafter;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileFarmer;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.common.tile.TileRancher;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import voltaic.api.ISubtype;
import voltaic.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import voltaic.api.tile.IMachine;
import voltaic.api.tile.MachineProperties;
import voltaic.common.block.voxelshapes.VoxelShapeProvider;

public enum SubtypeAssemblyMachine implements ISubtype, IMachine {

    crate(true, TileCrate::new),
    cratemedium(true, TileCrate::new),
    cratelarge(true, TileCrate::new),
    autocrafter(true, TileAutocrafter::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.AUTOCRAFTER)),
    blockbreaker(true, TileBlockBreaker::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.BLOCKBREAKER)),
    blockplacer(true, TileBlockPlacer::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.BLOCKPLACER)),
    rancher(true, TileRancher::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.ENERGIZEDRANCHER)),
    mobgrinder(true, TileMobGrinder::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.MOBGRINDER)),
    farmer(true, TileFarmer::new, MachineProperties.builder().setShapeProvider(AssemblyLineVoxelShapes.FARMER));

    private final BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier;
    private final boolean showInItemGroup;
    private final MachineProperties properties;

    private SubtypeAssemblyMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        this(showInItemGroup, blockEntitySupplier, MachineProperties.DEFAULT);
    }

    private SubtypeAssemblyMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier, MachineProperties properties) {
        this.showInItemGroup = showInItemGroup;
        this.blockEntitySupplier = blockEntitySupplier;
        this.properties = properties;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntity> getBlockEntitySupplier() {
        return this.blockEntitySupplier;
    }

    @Override
    public int getLitBrightness() {
        return this.properties.litBrightness;
    }

    @Override
    public RenderShape getRenderShape() {
        return this.properties.renderShape;
    }

    @Override
    public boolean isMultiblock() {
        return this.properties.isMultiblock;
    }

    @Override
    public boolean propegatesLightDown() {
        return this.properties.propegatesLightDown;
    }

    @Override
    public String tag() {
        return this.name();
    }

    @Override
    public String forgeTag() {
        return this.tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    @Override
    public boolean isPlayerStorable() {
        return false;
    }

    @Override
    public IMultiblockParentBlock.SubnodeWrapper getSubnodes() {
        return this.properties.wrapper;
    }

    @Override
    public VoxelShapeProvider getVoxelShapeProvider() {
        return this.properties.provider;
    }

    @Override
    public boolean usesLit() {
        return properties.usesLit;
    }

    public boolean showInItemGroup() {
        return this.showInItemGroup;
    }
}
