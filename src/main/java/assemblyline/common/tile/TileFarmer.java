package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import assemblyline.client.event.levelstage.HandlerFarmerLines;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.SpecialPlantable;
import net.neoforged.neoforge.common.Tags;
import voltaic.common.item.ItemUpgrade;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentContainerProvider;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentPacketHandler;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.prefab.utilities.ItemUtils;
import voltaic.prefab.utilities.object.TransferPack;
import voltaic.registers.VoltaicCapabilities;

public class TileFarmer extends GenericTile {

    public static final int DEFAULT_WAIT_TICKS = 60;
    public static final int FASTEST_WAIT_TICKS = 1;

    public static final int MAX_WIDTH_FARMER = 27;
    public static final int MAX_LENGTH_FARMER = 27;

    public static final int OPERATION_OFFSET = 2;

    public static final int MIN_CHORUS_PLANT_SIZE = 5;

    private static final int[][] TREE_SCANNING_GRID = new int[][]{ // Don't need to check bellow any blocks, as trees don't grow like that.
            {1, 1, 1}, {1, 1, 0}, {1, 1, -1}, {1, 0, 1}, {1, 0, 0}, {1, 0, -1},

            {0, 1, 1}, {0, 1, 0}, {0, 1, -1}, {0, 0, 1}, {0, 0, 0}, {0, 0, -1},

            {-1, 1, 1}, {-1, 1, 0}, {-1, 1, -1}, {-1, 0, 1}, {-1, 0, 0}, {-1, 0, -1},};

    private static final List<TagKey<Item>> VANILLA_SEED_TAGS = List.of(
            //
            Tags.Items.SEEDS_BEETROOT,
            //
            Tags.Items.SEEDS_MELON,
            //
            Tags.Items.SEEDS_WHEAT,
            //
            Tags.Items.SEEDS_PUMPKIN,
            //
            Tags.Items.CROPS_CACTUS,
            //
            Tags.Items.CROPS_SUGAR_CANE,
            //
            Tags.Items.CROPS_CARROT,
            //
            Tags.Items.CROPS_COCOA_BEAN,
            //
            Tags.Items.CROPS_NETHER_WART,
            //
            Tags.Items.CROPS_POTATO,
            //
            ItemTags.SAPLINGS
            //
    );

    private static final List<TagKey<Item>> VANILLA_TILLABLE_SEED_TAGS = List.of(
//
            Tags.Items.SEEDS_BEETROOT,
            //
            Tags.Items.SEEDS_MELON,
            //
            Tags.Items.SEEDS_WHEAT,
            //
            Tags.Items.SEEDS_PUMPKIN,
            //
            Tags.Items.CROPS_CARROT,
            //
            Tags.Items.CROPS_POTATO
            //

    );

    private static final List<Item> VANILLA_SEED_ITEMS = List.of(
            //
            Items.BAMBOO,
            //
            Items.CHORUS_FLOWER
            //

    );


    private int prevXShift = 0;
    private int prevZShift = 0;

    private final List<List<Integer>> quadrants = new ArrayList<>();

    public final SingleProperty<Boolean> refillEmpty = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "refillempty", false));
    public final SingleProperty<Boolean> fullGrowBonemeal = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "fullbonemeal", false));

    public final SingleProperty<Integer> ticksSinceCheck = property(new SingleProperty<>(PropertyTypes.INTEGER, "ticks", 0));
    public final SingleProperty<Integer> currentWaitTime = property(new SingleProperty<>(PropertyTypes.INTEGER, "waitTime", DEFAULT_WAIT_TICKS));

    public final SingleProperty<Double> powerUsageMultiplier = property(new SingleProperty<>(PropertyTypes.DOUBLE, "powermultiplier", 1.0));

    public final SingleProperty<Integer> currentWidth = property(new SingleProperty<>(PropertyTypes.INTEGER, "currwidth", 3));
    public final SingleProperty<Integer> currentLength = property(new SingleProperty<>(PropertyTypes.INTEGER, "currlength", 3));

    public TileFarmer(BlockPos pos, BlockState state) {
        super(AssemblyLineTiles.TILE_FARMER.get(), pos, state);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(VoltaicCapabilities.DEFAULT_VOLTAGE).maxJoules(AssemblyLineConstants.FARMER_USAGE * 20));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(10).outputs(9).upgrades(3))
                //
                .setSlotsByDirection(BlockEntityUtils.MachineDirection.RIGHT, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
                //
                .setSlotsByDirection(BlockEntityUtils.MachineDirection.LEFT, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
                //
                .setSlotsByDirection(BlockEntityUtils.MachineDirection.FRONT, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
                //
                .setSlotsByDirection(BlockEntityUtils.MachineDirection.BACK, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).validUpgrades(ContainerFarmer.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentContainerProvider("farmer", this).createMenu((id, player) -> new ContainerFarmer(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentForgeEnergy(this));
    }

    public void tickServer(ComponentTickable tick) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        // faster starting speed, but the fastest speed is one block in area checked per tick
        if (electro.getJoulesStored() < AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue()) {
            return;
        }

        electro.joules(electro.getJoulesStored() - AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue());

        ticksSinceCheck.setValue(ticksSinceCheck.getValue() + 1);

        if (ticksSinceCheck.getValue() >= currentWaitTime.getValue()) {
            ticksSinceCheck.setValue(0);
        }

        if (ticksSinceCheck.getValue() != 0) {
            return;
        }

        BlockPos machinePos = getBlockPos();
        BlockPos startPos = new BlockPos(machinePos.getX() - currentWidth.getValue() / 2, machinePos.getY() + OPERATION_OFFSET, machinePos.getZ() - currentLength.getValue() / 2);
        genQuadrants();
        BlockPos checkPos = new BlockPos(startPos.getX() + prevXShift, startPos.getY(), startPos.getZ() + prevZShift);
        int quadrant = getQuadrant(prevXShift, prevZShift);
        if (quadrant >= 0) {
            handleHarvest(checkPos, quadrant);
            handlePlanting(checkPos, quadrant);
        }
        refillInputs();
        prevZShift++;
        if (prevZShift >= currentLength.getValue()) {
            prevZShift = 0;
            prevXShift++;
            if (prevXShift >= currentWidth.getValue()) {
                prevXShift = 0;
            }
        }
        quadrants.clear();

    }

    private void handleHarvest(BlockPos checkPos, int quadrant) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        if (!inv.areOutputsEmpty()) {
            return;
        }
        Level world = getLevel();
        BlockState checkState = world.getBlockState(checkPos);
        Block checkBlock = checkState.getBlock();
        if (checkBlock instanceof CropBlock crop && crop.isMaxAge(checkState)) {
            breakBlock(checkState, world, checkPos, inv, SoundEvents.CROP_BREAK);
        } else if (checkState.is(Blocks.PUMPKIN) || checkState.is(Blocks.MELON)) {
            breakBlock(checkState, world, checkPos, inv, SoundEvents.WOOD_BREAK);
        } else if (checkBlock instanceof CactusBlock || checkBlock instanceof SugarCaneBlock) {
            BlockPos above = checkPos.above();
            List<BlockPos> positions = new ArrayList<>();
            while (world.getBlockState(above).is(checkBlock)) {
                positions.add(above);
                above = above.above();
            }
            BlockPos currPos;
            BlockState currState;
            for (int i = positions.size() - 1; i >= 0; i--) {
                currPos = positions.get(i);
                currState = world.getBlockState(currPos);
                if (checkBlock instanceof CactusBlock) {
                    breakBlock(currState, world, currPos, inv, SoundEvents.WOOL_BREAK);
                } else {
                    breakBlock(currState, world, currPos, inv, SoundEvents.GRASS_BREAK);
                }
            }
        } else if (checkBlock instanceof NetherWartBlock && checkState.getValue(NetherWartBlock.AGE).intValue() == NetherWartBlock.MAX_AGE) {
            breakBlock(checkState, world, checkPos, inv, SoundEvents.NETHER_WART_BREAK);
        } else if (checkState.is(BlockTags.LOGS)) {
            handleTree(world, checkPos, inv);
        } else if (checkState.is(Blocks.BAMBOO_SAPLING)) {
            BlockPos above = checkPos.above();
            List<BlockPos> positions = new ArrayList<>();
            while (world.getBlockState(above).getBlock() instanceof BambooStalkBlock) {
                positions.add(above);
                above = above.above();
            }
            BlockPos currPos;
            BlockState currState;
            for (int i = positions.size() - 1; i >= 0; i--) {
                currPos = positions.get(i);
                currState = world.getBlockState(currPos);
                breakBlock(currState, world, currPos, inv, SoundEvents.GRASS_BREAK);
            }
        } else if (checkState.getBlock() instanceof ChorusPlantBlock) {
            handleChorusTree(world, checkPos, inv);
        }
    }

    private static void handleChorusTree(Level world, BlockPos checkPos, ComponentInventory inv) {

        List<BlockPos> scannedBlocks = new ArrayList<>(64);
        Queue<BlockPos> toScan = new ConcurrentLinkedQueue<>();
        HashSet<BlockPos> chorusBlockSet = new HashSet<>(64);
        HashSet<BlockPos> chorusFlowerSet = new HashSet<>(64);
        toScan.add(checkPos);

        chorusBlockSet.add(checkPos);

        BlockState currState = world.getBlockState(checkPos);
        Block currBlock = currState.getBlock();

        while (!toScan.isEmpty()) {
            BlockPos itemPos = toScan.remove();

            for (int[] offset : TREE_SCANNING_GRID) {
                BlockPos currPos = itemPos.offset(offset[0], offset[1], offset[2]);

                // ignore already checked blocks
                if (scannedBlocks.contains(currPos)) {
                    continue;
                }
                scannedBlocks.add(itemPos);

                currState = world.getBlockState(currPos);
                currBlock = currState.getBlock();
                if(currBlock instanceof ChorusPlantBlock) {
                    toScan.add(currPos);
                    chorusBlockSet.add(currPos);
                } else if (currBlock instanceof ChorusFlowerBlock) {
                    toScan.add(currPos);
                    chorusFlowerSet.add(currPos);
                }
            }
        }

        if((chorusFlowerSet.size() + chorusBlockSet.size()) <= MIN_CHORUS_PLANT_SIZE) {
            return;
        }

        List<BlockPos> validBlocks = new ArrayList<>(chorusBlockSet);

        validBlocks.sort((pos1, pos2) -> pos2.getY() - pos1.getY());

        for(BlockPos pos : chorusFlowerSet) {
            breakBlock(world.getBlockState(pos), world, pos, inv, SoundEvents.WOOD_BREAK);
        }

        for(BlockPos pos : validBlocks) {
            breakBlock(world.getBlockState(pos), world, pos, inv, SoundEvents.WOOD_BREAK);
        }



    }

    private static void handleTree(Level world, BlockPos checkPos, ComponentInventory inv) {

        List<BlockPos> scannedBlocks = new ArrayList<>(64);
        Queue<BlockPos> toScan = new ConcurrentLinkedQueue<>();
        toScan.add(checkPos);

        BlockState currState = world.getBlockState(checkPos);
        breakBlock(currState, world, checkPos, inv, currState.is(BlockTags.LEAVES) ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);

        while (!toScan.isEmpty()) {
            BlockPos itemPos = toScan.remove();

            for (int[] offset : TREE_SCANNING_GRID) {
                boolean isLeaves;
                BlockPos currPos = itemPos.offset(offset[0], offset[1], offset[2]);

                // ignore already checked blocks
                if (scannedBlocks.contains(currPos)) {
                    continue;
                }
                scannedBlocks.add(itemPos);

                currState = world.getBlockState(currPos);
                isLeaves = currState.is(BlockTags.LEAVES);
                if (currState.is(BlockTags.LOGS) || isLeaves) {
                    toScan.add(currPos);
                    breakBlock(currState, world, currPos, inv, isLeaves ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);
                }
            }
        }
    }

    private static void breakBlock(BlockState checkState, Level world, BlockPos checkPos, ComponentInventory inv, SoundEvent event) {
        List<ItemStack> drops = Block.getDrops(checkState, (ServerLevel) world, checkPos, null);
        if(checkState.is(Blocks.CHORUS_FLOWER)) {
            drops.add(new ItemStack(Blocks.CHORUS_FLOWER));
        }

        int max = inv.getOutputStartIndex() + inv.getOutputContents().size();

        for(ItemStack item : drops) {

            for (int i = inv.getOutputStartIndex(); i < max; i++) {

                ItemStack contained = inv.getItem(i);

                int room = contained.getMaxStackSize() - contained.getCount();

                int amtAccepted = Math.min(room, item.getCount());

                if(amtAccepted == 0) {
                    continue;
                }

                if (contained.isEmpty()) {

                    inv.setItem(i, new ItemStack(item.getItem(), amtAccepted));

                    item.shrink(amtAccepted);

                } else if (ItemUtils.testItems(item.getItem(), contained.getItem())) {

                    contained.grow(amtAccepted);

                    item.shrink(amtAccepted);

                    inv.setChanged();

                }

                if(item.isEmpty()) {
                    break;
                }

            }

        }

        //world.setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
        world.destroyBlock(checkPos, false);
        world.playSound(null, checkPos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void handlePlanting(BlockPos checkPos, int quadrant) {

        Level world = getLevel();

        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        List<ItemStack> inputs = inv.getInputContents();
        ItemStack plantingContents = inputs.get(quadrant);
        ItemStack bonemeal = inputs.get(9);
        BlockState checkState = world.getBlockState(checkPos);
        BlockPos below = checkPos.below();
        BlockState belowState = world.getBlockState(below);
        BlockState farmland = Blocks.FARMLAND.defaultBlockState();
        boolean isAir = checkState.isAir();
        // Check block type
        if (isAir && plantingContents.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (blockItem instanceof SpecialPlantable plantable) {
                if (plantable.canPlacePlantAtPosition(plantingContents, level, checkPos, Direction.DOWN)) {
                    plantable.spawnPlantAtPosition(plantingContents, level, checkPos, Direction.DOWN);
                    world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                    plantingContents.shrink(1);
                    electro.extractPower(TransferPack.joulesVoltage(AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue(), electro.getVoltage()), false);
                    // then we check if it can be planted if the block becomes farmland
                } else if (belowState.is(BlockTags.DIRT)) {
                    world.setBlockAndUpdate(below, farmland);
                    world.playSound(null, below, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    electro.extractPower(TransferPack.joulesVoltage(AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue(), electro.getVoltage()), false);
                }
            } else if (checkVanilla(plantingContents, blockItem)) {

                if (block.defaultBlockState().canSurvive(world, checkPos)) {

                    world.setBlockAndUpdate(checkPos, block.defaultBlockState());
                    world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                    plantingContents.shrink(1);
                    electro.extractPower(TransferPack.joulesVoltage(AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue(), electro.getVoltage()), false);

                } else if (belowState.is(BlockTags.DIRT) && isVanillaTillable(plantingContents)) {
                    world.setBlockAndUpdate(below, farmland);
                    world.playSound(null, below, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.setBlockAndUpdate(checkPos, block.defaultBlockState());
                    world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                    plantingContents.shrink(1);
                    electro.extractPower(TransferPack.joulesVoltage(AssemblyLineConstants.FARMER_USAGE * powerUsageMultiplier.getValue(), electro.getVoltage()), false);
                }
            }


        }
        // update checkState in case something has been planted
        checkState = world.getBlockState(checkPos);
        if (bonemeal.getItem() instanceof BoneMealItem && bonemeal.getCount() > 0) {
            if (fullGrowBonemeal.getValue()) {
                while (bonemeal.getCount() > 0 && checkState.getBlock() instanceof BonemealableBlock bone && bone.isValidBonemealTarget(world, checkPos, checkState)) {
                    bone.performBonemeal((ServerLevel) world, world.getRandom(), checkPos, checkState);
                    bonemeal.shrink(1);
                    checkState = world.getBlockState(checkPos);
                }
            } else if (checkState.getBlock() instanceof BonemealableBlock bone && bone.isValidBonemealTarget(world, checkPos, checkState)) {
                bone.performBonemeal((ServerLevel) world, world.getRandom(), checkPos, checkState);
                bonemeal.shrink(1);
            }
        }
    }

    private static boolean checkVanilla(ItemStack plantingContents, BlockItem blockItem) {
        for (TagKey<Item> tag : VANILLA_SEED_TAGS) {
            if (plantingContents.is(tag)) {
                return true;
            }
        }
        for (Item item : VANILLA_SEED_ITEMS) {
            if (plantingContents.is(item)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isVanillaTillable(ItemStack plantingContents) {
        for (TagKey<Item> tag : VANILLA_TILLABLE_SEED_TAGS) {
            if (plantingContents.is(tag)) {
                return true;
            }
        }
        return false;
    }

    private void refillInputs() {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        List<ItemStack> inputs = inv.getInputContents();
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack input = inputs.get(i);
            for (ItemStack output : inv.getOutputContents()) {
                if (!output.isEmpty()) {
                    if (ItemUtils.testItems(input.getItem(), output.getItem())) {
                        int room = input.getMaxStackSize() - input.getCount();
                        int accepted = room > output.getCount() ? output.getCount() : room;
                        input.grow(accepted);
                        output.shrink(accepted);
                    } else if (refillEmpty.getValue() && input.isEmpty() && output.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SpecialPlantable) {
                        int room = inv.getMaxStackSize();
                        int amountAccepted = room > output.getCount() ? output.getCount() : room;
                        inv.setItem(i, new ItemStack(output.getItem(), amountAccepted).copy());
                        output.shrink(amountAccepted);
                    }
                }
            }
        }
    }

    public List<AABB> getLines(TileFarmer farmer) {
        BlockPos machinePos = farmer.getBlockPos();
        int multiplier = farmer.currentWidth.getValue() / 3;
        int x = machinePos.getX();
        int y = machinePos.getY() + OPERATION_OFFSET;
        int z = machinePos.getZ();
        List<AABB> boundingBoxes = new ArrayList<>();
        int xOffset = farmer.currentWidth.getValue() / 2;
        int zOffset = farmer.currentLength.getValue() / 2;
        BlockPos startPos;
        BlockPos endPos;
        if (multiplier == 1) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    boundingBoxes.add(new AABB(x + i, y, z + j, x + i + 1, y + 1, z + j + 1));
                }
            }
        } else {
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    startPos = new BlockPos(x + i * multiplier - xOffset, y, z + j * multiplier - zOffset);
                    endPos = new BlockPos(x + (i + 1) * multiplier - xOffset - 1, y, z + (j + 1) * multiplier - 1 - zOffset);
                    boundingBoxes.add(AABB.encapsulatingFullBlocks(startPos, endPos));
                }
            }
        }
        return boundingBoxes;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("xPos", prevXShift);
        compound.putInt("zPos", prevZShift);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        prevXShift = compound.getInt("xPos");
        prevZShift = compound.getInt("zPos");
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (getLevel().isClientSide) {
            HandlerFarmerLines.remove(getBlockPos());
        }
    }

    private int getQuadrant(int xShift, int zShift) {
        for (int i = 0; i < quadrants.size(); i++) {
            List<Integer> quadrant = quadrants.get(i);
            if (matchesQuadrant(quadrant, xShift, zShift)) {
                return i;
            }
        }
        return -1;
    }

    private void genQuadrants() {
        quadrants.clear();
        int multiplier = currentLength.getValue() / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                List<Integer> quadrant = new ArrayList<>();
                // special case for one otherwise series not possible
                if (multiplier == 1) {
                    quadrant.add(i);
                    quadrant.add(i);
                    quadrant.add(j);
                    quadrant.add(j);
                } else {
                    quadrant.add(i * multiplier);
                    quadrant.add((i + 1) * multiplier - 1);
                    quadrant.add(j * multiplier);
                    quadrant.add((j + 1) * multiplier - 1);
                }
                quadrants.add(quadrant);
            }
        }
    }

    private static boolean matchesQuadrant(List<Integer> quadrant, int xShift, int zShift) {
        if (quadrant.get(0) <= xShift && quadrant.get(1) >= xShift) {
            return quadrant.get(2) <= zShift && quadrant.get(3) >= zShift;
        }
        return false;
    }

    @Override
    public void onInventoryChange(ComponentInventory inv, int slot) {
        super.onInventoryChange(inv, slot);

        if (slot == -1 || slot >= inv.getUpgradeSlotStartIndex()) {
            int waitTime = DEFAULT_WAIT_TICKS;
            int width = 3;
            int length = 3;
            double powerMultiplier = 1.0;

            for (ItemStack stack : inv.getUpgradeContents()) {
                if (!stack.isEmpty()) {
                    ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                    switch (upgrade.subtype) {
                        case advancedspeed:
                            for (int i = 0; i < stack.getCount(); i++) {
                                waitTime = Math.max(waitTime / 4, FASTEST_WAIT_TICKS);
                                powerMultiplier *= 1.5;
                            }
                            break;
                        case basicspeed:
                            for (int i = 0; i < stack.getCount(); i++) {
                                waitTime = (int) Math.max(waitTime / 1.5, FASTEST_WAIT_TICKS);
                                powerMultiplier *= 1.5;
                            }
                            break;
                        case range:
                            for (int i = 0; i < stack.getCount(); i++) {
                                length = Math.min(length + 6, MAX_LENGTH_FARMER);
                                width = Math.min(width + 6, MAX_WIDTH_FARMER);
                                powerMultiplier *= 1.3;
                            }
                            break;
                        case itemoutput:
                            upgrade.subtype.applyUpgrade.accept(this, stack, 0);
                            break;
                        default:
                            break;
                    }
                }
            }

            currentWaitTime.setValue(waitTime);
            currentWidth.setValue(width);
            currentLength.setValue(length);
            powerUsageMultiplier.setValue(powerMultiplier);
        }

    }

}
