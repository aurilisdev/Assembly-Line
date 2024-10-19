package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jetbrains.annotations.NotNull;

import assemblyline.client.render.event.levelstage.HandlerFarmerLines;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.Constants;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyType;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.InventoryUtils;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

public class TileFarmer extends GenericTile {

	private static final BlockState AIR = Blocks.AIR.defaultBlockState();

	public static final int DEFAULT_WAIT_TICKS = 60;
	public static final int FASTEST_WAIT_TICKS = 1;

	public static final int MAX_WIDTH_FARMER = 27;
	public static final int MAX_LENGTH_FARMER = 27;

	public static final int MAX_TREE_SIZE_CHECK = 10;

	public static final int OPERATION_OFFSET = 2;

	private static final int[][] TreeScanningGrid = new int[][] { // Don't need to check bellow any blocks, as trees don't grow like that.
			{ 1, 1, 1 }, { 1, 1, 0 }, { 1, 1, -1 }, { 1, 0, 1 }, { 1, 0, 0 }, { 1, 0, -1 },

			{ 0, 1, 1 }, { 0, 1, 0 }, { 0, 1, -1 }, { 0, 0, 1 }, { 0, 0, 0 }, { 0, 0, -1 },

			{ -1, 1, 1 }, { -1, 1, 0 }, { -1, 1, -1 }, { -1, 0, 1 }, { -1, 0, 0 }, { -1, 0, -1 }, };

	private int prevXShift = 0;
	private int prevZShift = 0;

	private final List<List<Integer>> quadrants = new ArrayList<>();

	public final Property<Boolean> refillEmpty = property(new Property<>(PropertyType.Boolean, "refillempty", false));
	public final Property<Boolean> fullGrowBonemeal = property(new Property<>(PropertyType.Boolean, "fullbonemeal", false));

	public final Property<Integer> ticksSinceCheck = property(new Property<>(PropertyType.Integer, "ticks", 0));
	public final Property<Integer> currentWaitTime = property(new Property<>(PropertyType.Integer, "waitTime", DEFAULT_WAIT_TICKS));

	public final Property<Double> powerUsageMultiplier = property(new Property<>(PropertyType.Double, "powermultiplier", 1.0));

	public final Property<Integer> currentWidth = property(new Property<>(PropertyType.Integer, "currwidth", 3));
	public final Property<Integer> currentLength = property(new Property<>(PropertyType.Integer, "currlength", 3));

	public TileFarmer(BlockPos pos, BlockState state) {
		super(AssemblyLineBlockTypes.TILE_FARMER.get(), pos, state);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.FARMER_USAGE * 20));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(10).outputs(9).upgrades(3)).setSlotsByDirection(Direction.EAST, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).setSlotsByDirection(Direction.WEST, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
				.setSlotsByDirection(Direction.NORTH, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).setSlotsByDirection(Direction.SOUTH, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).validUpgrades(ContainerFarmer.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider("container.farmer", this).createMenu((id, player) -> new ContainerFarmer(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	public void tickServer(ComponentTickable tick) {

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		// faster starting speed, but the fastest speed is one block in area checked per tick
		if (electro.getJoulesStored() < Constants.FARMER_USAGE * powerUsageMultiplier.get()) {
			return;
		}

		electro.joules(electro.getJoulesStored() - Constants.FARMER_USAGE * powerUsageMultiplier.get());

		ticksSinceCheck.set(ticksSinceCheck.get() + 1);

		if (ticksSinceCheck.get() >= currentWaitTime.get()) {
			ticksSinceCheck.set(0);
		}

		if (ticksSinceCheck.get() != 0) {
			return;
		}

		BlockPos machinePos = getBlockPos();
		BlockPos startPos = new BlockPos(machinePos.getX() - currentWidth.get() / 2, machinePos.getY() + OPERATION_OFFSET, machinePos.getZ() - currentLength.get() / 2);
		genQuadrants();
		BlockPos checkPos = new BlockPos(startPos.getX() + prevXShift, startPos.getY(), startPos.getZ() + prevZShift);
		int quadrant = getQuadrant(prevXShift, prevZShift);
		if (quadrant >= 0) {
			handleHarvest(checkPos, quadrant);
			handlePlanting(checkPos, quadrant);
		}
		refillInputs();
		prevZShift++;
		if (prevZShift >= currentLength.get()) {
			prevZShift = 0;
			prevXShift++;
			if (prevXShift >= currentWidth.get()) {
				prevXShift = 0;
			}
		}
		quadrants.clear();

	}

	private void handleHarvest(BlockPos checkPos, int quadrant) {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		if (!inv.areInputsEmpty()) {
			return;
		}
		Level world = getLevel();
		BlockState checkState = world.getBlockState(checkPos);
		Block checkBlock = checkState.getBlock();
		if (checkBlock instanceof CropBlock crop && crop.isMaxAge(checkState)) {
			breakBlock(checkState, world, checkPos, inv, SoundEvents.CROP_BREAK);
		} else if (checkBlock instanceof StemGrownBlock) {
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
		} else if (ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LOGS).contains(checkBlock)) {
			handleTree(world, checkPos, inv);
		}
	}

	private void handleTree(Level world, BlockPos checkPos, ComponentInventory inv) {

		ITag<Block> logs = ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LOGS);
		ITag<Block> leaves = ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LEAVES);

		List<BlockPos> scannedBlocks = new ArrayList<>(64);
		Queue<BlockPos> toScan = new ConcurrentLinkedQueue<>();
		toScan.add(checkPos);

		BlockState currState = world.getBlockState(checkPos);
		breakBlock(currState, world, checkPos, inv, leaves.contains(currState.getBlock()) ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);

		while (!toScan.isEmpty()) {
			BlockPos itemPos = toScan.remove();

			for (int[] offset : TreeScanningGrid) {
				boolean isLeaves;
				BlockPos currPos = itemPos.offset(offset[0], offset[1], offset[2]);

				// ignore already checked blocks
				if (scannedBlocks.contains(currPos)) {
					continue;
				}
				scannedBlocks.add(itemPos);

				currState = world.getBlockState(currPos);
				Block currBlock = currState.getBlock();
				isLeaves = leaves.contains(currBlock);
				if (logs.contains(currBlock) || isLeaves) {
					toScan.add(currPos);
					breakBlock(currState, world, currPos, inv, isLeaves ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);
				}
			}
		}
	}

	private void breakBlock(BlockState checkState, Level world, BlockPos checkPos, ComponentInventory inv, SoundEvent event) {
		List<ItemStack> drops = Block.getDrops(checkState, (ServerLevel) world, checkPos, null);
		InventoryUtils.addItemsToInventory(inv, drops, inv.getOutputStartIndex(), inv.getOutputContents().size());
		world.setBlockAndUpdate(checkPos, AIR);
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
		if (isAir && plantingContents.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable plantable) {
			Block block = blockItem.getBlock();
			// first we check if it can be planted
			if (belowState.canSustainPlant(world, below, Direction.UP, plantable)) {
				world.setBlockAndUpdate(checkPos, block.defaultBlockState());
				world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
				plantingContents.shrink(1);
				electro.extractPower(TransferPack.joulesVoltage(Constants.FARMER_USAGE * powerUsageMultiplier.get(), electro.getVoltage()), false);
				// then we check if it can be planted if the block becomes farmland
			} else if (belowState.is(Blocks.DIRT) && farmland.canSustainPlant(world, below, Direction.UP, plantable)) {
				world.setBlockAndUpdate(below, farmland);
				world.playSound(null, below, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				world.setBlockAndUpdate(checkPos, block.defaultBlockState());
				world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
				plantingContents.shrink(1);
				electro.extractPower(TransferPack.joulesVoltage(Constants.FARMER_USAGE * powerUsageMultiplier.get(), electro.getVoltage()), false);
			}
		}
		// update checkState in case something has been planted
		checkState = world.getBlockState(checkPos);
		if (bonemeal.getItem() instanceof BoneMealItem && bonemeal.getCount() > 0) {
			if (fullGrowBonemeal.get()) {
				while (bonemeal.getCount() > 0 && checkState.getBlock() instanceof BonemealableBlock bone && bone.isValidBonemealTarget(world, checkPos, checkState, false)) {
					bone.performBonemeal((ServerLevel) world, world.getRandom(), checkPos, checkState);
					bonemeal.shrink(1);
					checkState = world.getBlockState(checkPos);
				}
			} else if (checkState.getBlock() instanceof BonemealableBlock bone && bone.isValidBonemealTarget(world, checkPos, checkState, false)) {
				bone.performBonemeal((ServerLevel) world, world.getRandom(), checkPos, checkState);
				bonemeal.shrink(1);
			}
		}
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
					} else if (refillEmpty.get() && input.isEmpty() && output.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable) {
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
		int multiplier = farmer.currentWidth.get() / 3;
		int x = machinePos.getX();
		int y = machinePos.getY() + OPERATION_OFFSET;
		int z = machinePos.getZ();
		List<AABB> boundingBoxes = new ArrayList<>();
		int xOffset = farmer.currentWidth.get() / 2;
		int zOffset = farmer.currentLength.get() / 2;
		BlockPos startPos;
		BlockPos endPos;
		if (multiplier == 0) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					startPos = new BlockPos(x - 1 + i, y + 1, z - 1 + j);
					endPos = new BlockPos(x - 1 + i + 1, y, z - 1 + j + 1);
					boundingBoxes.add(new AABB(startPos, endPos));
				}
			}
		} else {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					startPos = new BlockPos(x - xOffset + i * multiplier, y + 1, z - zOffset + j * multiplier);
					endPos = new BlockPos(x - xOffset + (i + 1) * multiplier, y, z - zOffset + (j + 1) * multiplier);
					boundingBoxes.add(new AABB(startPos, endPos));
				}
			}
		}
		return boundingBoxes;
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("xPos", prevXShift);
		nbt.putInt("zPos", prevZShift);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		prevXShift = nbt.getInt("xPos");
		prevZShift = nbt.getInt("zPos");
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
		int multiplier = currentLength.get() / 3;
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
						upgrade.subtype.applyUpgrade.accept(this, null, stack);
						break;
					default:
						break;
					}
				}
			}

			currentWaitTime.set(waitTime);
			currentWidth.set(width);
			currentLength.set(length);
			powerUsageMultiplier.set(powerMultiplier);
		}

	}

}
