package assemblyline.common.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mojang.datafixers.util.Pair;

import assemblyline.DeferredRegisters;
import assemblyline.client.ClientEvents;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.Constants;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.item.ItemUtils;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.InventoryUtils;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

	public boolean refillEmpty = false;
	public boolean fullGrowBonemeal = false;

	public boolean clientRefillEmpty;
	public boolean clientGrowBonemeal;

	private int ticksSinceCheck;
	private int currentWaitTime;

	public double clientProgress;

	private double powerUsageMultiplier = 1;
	public double clientUsageMultiplier;

	protected int currentWidth;
	protected int currentLength;
	public int clientLength;
	public int clientWidth;

	public static List<List<Integer>> COLORS = new ArrayList<>();

	static {
		// argb
		COLORS.add(Arrays.asList(255, 0, 0, 0));
		COLORS.add(Arrays.asList(255, 255, 0, 0));
		COLORS.add(Arrays.asList(255, 120, 0, 255));
		COLORS.add(Arrays.asList(255, 0, 255, 0));
		COLORS.add(Arrays.asList(255, 220, 0, 255));
		COLORS.add(Arrays.asList(255, 255, 120, 0));
		COLORS.add(Arrays.asList(255, 0, 0, 255));
		COLORS.add(Arrays.asList(255, 240, 255, 0));
		COLORS.add(Arrays.asList(255, 0, 240, 255));
	}

	public TileFarmer(BlockPos pos, BlockState state) {
		super(DeferredRegisters.TILE_FARMER.get(), pos, state);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler().customPacketWriter(this::createPacket).guiPacketWriter(this::createPacket).customPacketReader(this::readPacket).guiPacketReader(this::readPacket));
		addComponent(new ComponentTickable().tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.FARMER_USAGE * 20));
		addComponent(new ComponentInventory(this).size(22).inputs(10).outputs(9).upgrades(3).validUpgrades(ContainerFarmer.VALID_UPGRADES).valid(machineValidator()).shouldSendInfo());
		addComponent(new ComponentContainerProvider("container.farmer").createMenu((id, player) -> new ContainerFarmer(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	public void tickServer(ComponentTickable tick) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		currentWaitTime = DEFAULT_WAIT_TICKS;
		currentWidth = 3;
		currentLength = 3;
		powerUsageMultiplier = 1;
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty()) {
				ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
				switch (upgrade.subtype) {
				case advancedspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime = Math.max(currentWaitTime / 4, FASTEST_WAIT_TICKS);
						powerUsageMultiplier *= 1.5;
					}
					break;
				case basicspeed:
					for (int i = 0; i < stack.getCount(); i++) {
						currentWaitTime = (int) Math.max(currentWaitTime / 1.5, FASTEST_WAIT_TICKS);
						powerUsageMultiplier *= 1.5;
					}
					break;
				case range:
					for (int i = 0; i < stack.getCount(); i++) {
						currentLength = Math.min(currentLength + 6, MAX_LENGTH_FARMER);
						currentWidth = Math.min(currentWidth + 6, MAX_WIDTH_FARMER);
						powerUsageMultiplier *= 1.3;
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
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		// faster starting speed, but the fastest speed is one block in area checked per tick
		if (electro.getJoulesStored() >= Constants.MOBGRINDER_USAGE) {
			electro.extractPower(TransferPack.joulesVoltage(Constants.MOBGRINDER_USAGE, electro.getVoltage()), false);
			if (ticksSinceCheck == 0) {
				BlockPos machinePos = getBlockPos();
				BlockPos startPos = new BlockPos(machinePos.getX() - currentWidth / 2, machinePos.getY() + OPERATION_OFFSET, machinePos.getZ() - currentLength / 2);
				genQuadrants();
				BlockPos checkPos = new BlockPos(startPos.getX() + prevXShift, startPos.getY(), startPos.getZ() + prevZShift);
				int quadrant = getQuadrant(prevXShift, prevZShift);
				if (quadrant >= 0) {
					handleHarvest(checkPos, quadrant);
					handlePlanting(checkPos, quadrant);
				}
				refillInputs();
				prevZShift++;
				if (prevZShift >= currentLength) {
					prevZShift = 0;
					prevXShift++;
					if (prevXShift >= currentWidth) {
						prevXShift = 0;
					}
				}
				quadrants.clear();
			}
			ticksSinceCheck++;
			if (ticksSinceCheck >= currentWaitTime) {
				ticksSinceCheck = 0;
			}
		}

	}

	private void handleHarvest(BlockPos checkPos, int quadrant) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		if (inv.areOutputsEmpty()) {
			Level world = getLevel();
			BlockState checkState = world.getBlockState(checkPos);
			Block checkBlock = checkState.getBlock();
			if (checkBlock instanceof CropBlock crop && crop.isMaxAge(checkState)) {
				breakBlock(checkState, world, checkPos, inv, electro, SoundEvents.CROP_BREAK);
			} else if (checkBlock instanceof StemGrownBlock stem) {
				breakBlock(checkState, world, checkPos, inv, electro, SoundEvents.WOOD_BREAK);
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
						breakBlock(currState, world, currPos, inv, electro, SoundEvents.WOOL_BREAK);
					} else {
						breakBlock(currState, world, currPos, inv, electro, SoundEvents.GRASS_BREAK);
					}
				}
			} else if (checkBlock instanceof NetherWartBlock wart && checkState.getValue(NetherWartBlock.AGE).intValue() == NetherWartBlock.MAX_AGE) {
				breakBlock(checkState, world, checkPos, inv, electro, SoundEvents.NETHER_WART_BREAK);
			} else if (ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LOGS).contains(checkBlock)) {
				handleTree(world, checkPos, inv, electro);
			}
		}
	}

	private void handleTree(Level world, BlockPos checkPos, ComponentInventory inv, ComponentElectrodynamic electro) {

		ITag<Block> logs = ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LOGS);
		ITag<Block> leaves = ForgeRegistries.BLOCKS.tags().getTag(BlockTags.LEAVES);

		List<BlockPos> scannedBlocks = new ArrayList<>(64);
		Queue<BlockPos> toScan = new ConcurrentLinkedQueue<>();
		toScan.add(checkPos);

		BlockState currState = world.getBlockState(checkPos);
		breakBlock(currState, world, checkPos, inv, electro, leaves.contains(currState.getBlock()) ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);

		while (toScan.size() > 0) {
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
					breakBlock(currState, world, currPos, inv, electro, isLeaves ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK);
				}
			}
		}
	}

	private void handlePlanting(BlockPos checkPos, int quadrant) {
		Level world = getLevel();
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		List<ItemStack> inputs = inv.getInputContents().get(0);
		ItemStack plantingContents = inputs.get(quadrant);
		ItemStack bonemeal = inputs.get(9);
		BlockState checkState = world.getBlockState(checkPos);
		BlockPos below = checkPos.below();
		BlockState belowState = world.getBlockState(below);
		BlockState farmland = Blocks.FARMLAND.defaultBlockState();
		boolean isAir = checkState.is(Blocks.AIR);
		// Check block type
		if (isAir && plantingContents.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable plantable) {
			Block block = blockItem.getBlock();
			belowState = world.getBlockState(below);
			// first we check if it can be planted
			if (belowState.canSustainPlant(world, below, Direction.UP, plantable)) {
				world.setBlockAndUpdate(checkPos, block.defaultBlockState());
				world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
				plantingContents.shrink(1);
				electro.extractPower(TransferPack.joulesVoltage(Constants.FARMER_USAGE * powerUsageMultiplier, electro.getVoltage()), false);
				// then we check if it can be planted if the block becomes farmland
			} else if (belowState.is(Blocks.DIRT) && farmland.canSustainPlant(world, below, Direction.UP, plantable)) {
				world.setBlockAndUpdate(below, farmland);
				world.playSound(null, below, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				world.setBlockAndUpdate(checkPos, block.defaultBlockState());
				world.playSound(null, checkPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
				plantingContents.shrink(1);
				electro.extractPower(TransferPack.joulesVoltage(Constants.FARMER_USAGE * powerUsageMultiplier, electro.getVoltage()), false);
			}
		}
		// update checkState in case something has been planted
		checkState = world.getBlockState(checkPos);
		if (bonemeal.getItem() instanceof BoneMealItem && bonemeal.getCount() > 0) {
			if (fullGrowBonemeal) {
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
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		List<ItemStack> inputs = inv.getInputContents().get(0);
		for (int i = 0; i < inputs.size(); i++) {
			ItemStack input = inputs.get(i);
			for (ItemStack output : inv.getOutputContents()) {
				if (!output.isEmpty()) {
					if (ItemUtils.testItems(input.getItem(), output.getItem())) {
						int room = input.getMaxStackSize() - input.getCount();
						int accepted = room > output.getCount() ? output.getCount() : room;
						input.grow(accepted);
						output.shrink(accepted);
					} else if (refillEmpty && input.isEmpty() && output.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable) {
						int room = inv.getMaxStackSize();
						int amountAccepted = room > output.getCount() ? output.getCount() : room;
						inv.setItem(i, new ItemStack(output.getItem(), amountAccepted).copy());
						output.shrink(amountAccepted);
					}
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public Pair<List<List<Integer>>, List<AABB>> getLines(TileFarmer farmer) {
		BlockPos machinePos = farmer.getBlockPos();
		int multiplier = farmer.clientWidth / 3;
		int x = machinePos.getX();
		int y = machinePos.getY() + OPERATION_OFFSET;
		int z = machinePos.getZ();
		List<AABB> boundingBoxes = new ArrayList<>();
		int xOffset = farmer.clientWidth / 2;
		int zOffset = farmer.clientLength / 2;
		BlockPos startPos, endPos;
		if (multiplier == 1) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					startPos = new BlockPos(x - xOffset + i, y + 1, z - zOffset + j);
					endPos = new BlockPos(x - xOffset + i + 1, y, z - zOffset + j + 1);
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
		return Pair.of(COLORS, boundingBoxes);
	}

	protected void createPacket(CompoundTag nbt) {
		nbt.putInt("clientLength", currentLength);
		nbt.putInt("clientWidth", currentWidth);
		nbt.putDouble("clientProgress", (double) ticksSinceCheck / (double) currentWaitTime);
		nbt.putDouble("clientMultiplier", powerUsageMultiplier);
		nbt.putBoolean("clientBonemeal", fullGrowBonemeal);
		nbt.putBoolean("clientRefill", refillEmpty);
	}

	protected void readPacket(CompoundTag nbt) {
		clientLength = nbt.getInt("clientLength");
		clientWidth = nbt.getInt("clientWidth");
		clientProgress = nbt.getDouble("clientProgress");
		clientUsageMultiplier = nbt.getDouble("clientMultiplier");
		clientGrowBonemeal = nbt.getBoolean("clientBonemeal");
		clientRefillEmpty = nbt.getBoolean("clientRefill");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putBoolean("bonemeal", fullGrowBonemeal);
		nbt.putBoolean("refill", refillEmpty);
		nbt.putInt("xPos", prevXShift);
		nbt.putInt("zPos", prevZShift);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		fullGrowBonemeal = nbt.getBoolean("bonemeal");
		refillEmpty = nbt.getBoolean("refill");
		prevXShift = nbt.getInt("xPos");
		prevZShift = nbt.getInt("zPos");
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (getLevel().isClientSide) {
			ClientEvents.farmerLines.remove(getBlockPos());
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
		int multiplier = currentLength / 3;
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

	private void breakBlock(BlockState checkState, Level world, BlockPos checkPos, ComponentInventory inv, ComponentElectrodynamic electro, SoundEvent event) {
		electro.extractPower(TransferPack.joulesVoltage(Constants.FARMER_USAGE * powerUsageMultiplier, electro.getVoltage()), false);
		List<ItemStack> drops = Block.getDrops(checkState, (ServerLevel) world, checkPos, null);
		InventoryUtils.addItemsToInventory(inv, drops, inv.getOutputStartIndex(), inv.getOutputContents().size());
		world.setBlockAndUpdate(checkPos, AIR);
		world.playSound(null, checkPos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

}
