package assemblyline.common.tile.belt.utils;

import java.util.ArrayList;
import java.util.List;

import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.settings.AssemblyLineConstants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import voltaic.Voltaic;
import voltaic.common.tags.VoltaicTags;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.tile.components.type.ComponentForgeEnergy;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.tile.components.type.ComponentPacketHandler;
import voltaic.prefab.tile.components.type.ComponentTickable;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.prefab.utilities.CapabilityUtils;
import voltaic.prefab.utilities.object.CachedTileOutput;
import voltaic.prefab.utilities.object.Location;

public abstract class GenericTileConveyorBelt extends GenericTile {

	public static final int MIN_SPREAD = 0;

	public static final BlockPos[] SPREAD_OFFSETS = {
			//
			new BlockPos(0, 0, 1),
			//
			new BlockPos(0, 0, -1),
			//
			new BlockPos(1, 0, 0),
			//
			new BlockPos(-1, 0, 0),
			//
			new BlockPos(0, -1, 1),
			//
			new BlockPos(0, -1, -1),
			//
			new BlockPos(1, -1, 0),
			//
			new BlockPos(-1, -1, 0),
			//
			new BlockPos(0, 1, 1),
			//
			new BlockPos(0, 1, -1),
			//
			new BlockPos(1, 1, 0),
			//
			new BlockPos(-1, 1, 0),
			//
	};

	public final SingleProperty<Integer> currentSpread = property(new SingleProperty<>(PropertyTypes.INTEGER, "currentspread", 0)).setNoUpdateServer();
	public final SingleProperty<Boolean> running = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "running", false)).setNoUpdateServer();
	public final SingleProperty<Location> itemLocation = property(new SingleProperty<>(PropertyTypes.LOCATION, "conveyorobject", new Location(0, 0, 0))).setNoUpdateServer();
	public final SingleProperty<Integer> conveyorType = property(new SingleProperty<>(PropertyTypes.INTEGER, "conveyortype", ConveyorType.HORIZONTAL.ordinal())).setNoUpdateServer();
	public final SingleProperty<Boolean> isPusher = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "pusher", false)).setNoUpdateServer();
	public final SingleProperty<Boolean> isPuller = property(new SingleProperty<>(PropertyTypes.BOOLEAN, "puller", false)).setNoUpdateServer();

	public int wait = 0;
	private CachedTileOutput nextCache;
	private CachedTileOutput beforeCache;

	private boolean hasDroppedThisTick = false; // There is a dupe bug I found. Unknown how it happens but this fixes it

	private final ConveyorBeltProperties properties;

	public GenericTileConveyorBelt(TileEntityType<?> type, ConveyorBeltProperties properties) {
		super(type);
		addComponent(new ComponentTickable(this).tickCommon(this::tickCommon));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().forceSize(properties.invSize)));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).maxJoules(AssemblyLineConstants.CONVEYORBELT_USAGE * 100));
		addComponent(new ComponentForgeEnergy(this));
		this.properties = properties;
	}

	public void tickCommon(ComponentTickable tickable) {

		hasDroppedThisTick = false;

		if (nextCache == null) {
			nextCache = new CachedTileOutput(getLevel(), getNextPos());
		}

		if (beforeCache == null) {
			beforeCache = new CachedTileOutput(getLevel(), getBeforePos());
		}

		if (tickable.getTicks() % 5 == 0) {
			nextCache.update(getNextPos());
			beforeCache.update(getBeforePos());
		}

		if (!level.isClientSide) {

			isPusher.setValue(properties.canBePusher && nextCache.valid() && !(nextCache.getSafe() instanceof GenericTileConveyorBelt) && ((TileEntity) nextCache.getSafe()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getDirectionForNext().getOpposite()).orElse(CapabilityUtils.EMPTY_ITEM_HANDLER) != CapabilityUtils.EMPTY_ITEM_HANDLER);
			isPuller.setValue(properties.canBePuller && beforeCache.valid() && !(beforeCache.getSafe() instanceof GenericTileConveyorBelt) && ((TileEntity) beforeCache.getSafe()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getDirectionForLast().getOpposite()).orElse(CapabilityUtils.EMPTY_ITEM_HANDLER) != CapabilityUtils.EMPTY_ITEM_HANDLER);

			int currSpread = currentSpread.getValue();

			int maxSpread = 0;

			TileEntity offsetTile;

			for (BlockPos offset : SPREAD_OFFSETS) {

				offsetTile = level.getBlockEntity(worldPosition.offset(offset));

				if (offsetTile instanceof GenericTileConveyorBelt) {
					GenericTileConveyorBelt belt = (GenericTileConveyorBelt) offsetTile;

					int offsetSpread = belt.currentSpread.getValue();

					if (offsetSpread - 1 > maxSpread) {

						maxSpread = offsetSpread - 1;

					}

				}
			}

			ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

			currentSpread.setValue(maxSpread);

			if (currSpread > currentSpread.getValue()) {

				currentSpread.setValue(MIN_SPREAD);

			}

			if (currentSpread.getValue() == MIN_SPREAD || currentSpread.getValue() == AssemblyLineConstants.CONVEYOR_MAX_SPREAD) {

				if (electro.getJoulesStored() < AssemblyLineConstants.CONVEYORBELT_USAGE) {

					currentSpread.setValue(MIN_SPREAD);

				} else {

					electro.joules(electro.getJoulesStored() - AssemblyLineConstants.CONVEYORBELT_USAGE);

					currentSpread.setValue(AssemblyLineConstants.CONVEYOR_MAX_SPREAD);

				}
			}
			running.setValue(currentSpread.getValue() > MIN_SPREAD);
		}

		if (getItemOnBelt().isEmpty()) {
			itemLocation.setValue(getDefaultItemLocation(false));
			if (!level.isClientSide && running.getValue()) {
				pullItemFromInventory();
			}
			if (getItemOnBelt().isEmpty()) {
				return;
			}
		}

		if (!running.getValue()) {
			return;
		}

		Vector3f move = getDirectionVector();

		if (canMove()) {

			move.mul(1 / 16.0f);

			double speed = properties.conveyorClass.speed;

			double x = move.x() * speed;
			double y = move.y();
			double z = move.z() * speed;

			ConveyorType type = getConveyorType();

			if (type != ConveyorType.HORIZONTAL) {

				y += 1 / 16.0f * (type == ConveyorType.SLOPED_DOWN ? -1 : 1) * speed;

			}

			itemLocation.setValue(itemLocation.getValue().add(x, y, z));

			Vector3f localVector = getLocalItemLocationVector();

			if (x == 0 && localVector.x() - 0.5 * Math.signum(localVector.x()) != 0) {

				localVector.setX(0.5F * Math.signum(localVector.x()));
				Location loc = new Location(worldPosition.getX() + localVector.x(), worldPosition.getY() + localVector.y(), worldPosition.getZ() + localVector.z());
				itemLocation.setValue(loc);

			} else if (z == 0 && localVector.z() - 0.5 * Math.signum(localVector.z()) != 0) {

				localVector.setZ(0.5F * Math.signum(localVector.z()));
				Location loc = new Location(worldPosition.getX() + localVector.x(), worldPosition.getY() + localVector.y(), worldPosition.getZ() + localVector.z());
				itemLocation.setValue(loc);

			}

			return;
		}

		if (!nextCache.valid()) {
			Vector3f local = getLocalItemLocationVector();

			Direction dir = getDirectionForNext();

			float stepX = dir.getStepX();
			float stepZ = dir.getStepZ();

			float absX = Math.abs(local.x());
			float absZ = Math.abs(local.z());

			boolean xIs = stepX != 0 && stepX < 0 ? absX <= 0.2F : absX >= 0.8F;
			boolean zIs = stepZ != 0 && stepZ < 0 ? absZ <= 0.2F : absZ >= 0.8F;

			if (xIs || zIs) {
				dropItem(getItemOnBelt(), move);
			}
			return;
		}

		TileEntity nextBlockEntity = nextCache.getSafe();

		// boolean shouldTransfer = shouldTransfer(nextBlockEntity, itemLocation.getValue().toBlockPos());

		if (nextBlockEntity instanceof GenericTileConveyorBelt) {
			GenericTileConveyorBelt belt = (GenericTileConveyorBelt) nextBlockEntity;

			if (belt.getItemOnBelt().isEmpty()) {
				belt.addItemOnBelt(getItemOnBelt().copy(), itemLocation.getValue());
				setItemOnBelt(ItemStack.EMPTY);
			}

		} else {

			Direction direction = getFacing();

			IItemHandler handler = nextBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).orElse(CapabilityUtils.EMPTY_ITEM_HANDLER);

			ItemStack stackOnBelt = getItemOnBelt().copy();

			if (handler != CapabilityUtils.EMPTY_ITEM_HANDLER && !level.isClientSide) {

				if (wait == 0) {

					int amtTaken = 0;

					ItemStack remainder;

					for (int targetIndex = 0; targetIndex < handler.getSlots(); targetIndex++) {

						remainder = handler.insertItem(targetIndex, stackOnBelt, level.isClientSide);

						int taken = stackOnBelt.getCount() - remainder.getCount();

						if (taken <= 0) {

							continue;

						}

						amtTaken += taken;

						stackOnBelt = stackOnBelt.copy();

						stackOnBelt.shrink(taken);

						if (stackOnBelt.isEmpty()) {
							break;
						}

					}

					stackOnBelt.shrink(amtTaken);

					setItemOnBelt(stackOnBelt);

					if (amtTaken == 0) {

						wait = 20;

					}

				} else {

					wait--;

				}

			}

		}

	}

	// Serverside only
	public void pullItemFromInventory() {

		if (!isPuller.getValue() || !getItemOnBelt().isEmpty()) {
			return;
		}

		TileEntity lastBlockEntity = beforeCache.getSafe();

		IItemHandler handler = lastBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getDirectionForLast().getOpposite()).orElse(CapabilityUtils.EMPTY_ITEM_HANDLER);

		if (handler != CapabilityUtils.EMPTY_ITEM_HANDLER) {

			for (int slot = 0; slot < handler.getSlots(); slot++) {

				ItemStack accepted = addItemOnBelt(handler.extractItem(slot, 64, true), getDefaultItemLocation(true));

				if (!accepted.isEmpty()) {

					handler.extractItem(slot, accepted.getCount(), level.isClientSide);

					break;
				}
			}
		}
	}

	public boolean canMove() {

		if (getConveyorType() == ConveyorType.VERTICAL) {

			return getLocalItemLocationVector().y() < 1;

		}

		Direction next = getDirectionForNext();

		BlockPos nextPos = getNextPos();

		if (next == Direction.SOUTH) {
			return nextPos.getZ() - itemLocation.getValue().z() > 0;
		} else if (next == Direction.WEST) {
			return nextPos.getX() - itemLocation.getValue().x() < -1;
		} else if (next == Direction.NORTH) {
			return nextPos.getZ() - itemLocation.getValue().z() < -1;
		} else if (next == Direction.EAST) {
			return nextPos.getX() - itemLocation.getValue().x() > 0;
		}

		return false;

	}

	// Returns the amount that was taken
	public ItemStack addItemOnBelt(ItemStack add, Location object) {

		ItemStack taken = ItemStack.EMPTY;

		if (add.isEmpty()) {
			return taken;
		}

		taken = ItemStack.EMPTY;

		boolean inserted = false;

		boolean newItem = true;

		ItemStack beltItem = getItemOnBelt();

		if (beltItem.isEmpty()) {

			taken = add.copy();

			setItemOnBelt(add.copy());

			inserted = true;

		} else if (ItemStack.isSame(beltItem, add)) {

			int room = beltItem.getMaxStackSize() - beltItem.getCount();

			int accepted = Math.min(room, add.getCount());

			if (accepted > 0) {

				taken = add.copy();

				beltItem.grow(accepted);

				setItemOnBelt(beltItem.copy());

				taken.setCount(accepted);

				inserted = true;

				newItem = false;
			}

		}

		if (inserted && newItem) {

			if (getConveyorType() == ConveyorType.VERTICAL) {

				Vector3f vec = getDirectionVector();

				object = object.add(vec.x(), vec.y(), vec.z());

			}

			itemLocation.setValue(object);
		}

		return taken;
	}

	public void dropItem(ItemStack stackOnBelt, Vector3f move) {

		if (hasDroppedThisTick) {
			return;
		}

		hasDroppedThisTick = true;

		if (!level.isClientSide) {

			double x = worldPosition.getX() + 0.5 + (move.x() / 2.0f);
			double y = worldPosition.getY() + 0.4 + (getConveyorType() == ConveyorType.SLOPED_DOWN ? -1.0 : 0.0);
			double z = worldPosition.getZ() + 0.5 + (move.z() / 2.0f);

			ItemEntity entity = new ItemEntity(level, x, y, z, stackOnBelt.copy());

			entity.setDeltaMovement(move.x() / 12.0, 1.5 / 16.0, move.z() / 12.0);

			entity.setPickUpDelay(20);

			level.addFreshEntity(entity);

		}

		setItemOnBelt(ItemStack.EMPTY);

	}

	public BlockPos getNextPos() {

		Direction direction = getDirectionForNext();

		switch (ConveyorType.values()[conveyorType.getValue()]) {
		case SLOPED_DOWN:
			return worldPosition.relative(direction).below();
		case SLOPED_UP:
			return worldPosition.relative(direction).above();
		case VERTICAL:
			TileEntity tileentity = level.getBlockEntity(worldPosition.relative(Direction.UP));
			return tileentity instanceof GenericTileConveyorBelt && ((GenericTileConveyorBelt) tileentity).getConveyorType() == ConveyorType.VERTICAL ? worldPosition.relative(Direction.UP) : worldPosition.relative(direction).above();
		default:
			return worldPosition.relative(direction);

		}
	}

	public BlockPos getBeforePos() {
		Direction direction = getDirectionForLast();

		switch (ConveyorType.values()[conveyorType.getValue()]) {
		case SLOPED_DOWN:
			return worldPosition.relative(direction).above();
		case SLOPED_UP:
			return worldPosition.relative(direction).below();
		case VERTICAL:
			TileEntity tileentity = level.getBlockEntity(worldPosition.relative(Direction.DOWN));
			return tileentity instanceof GenericTileConveyorBelt && ((GenericTileConveyorBelt) tileentity).getConveyorType() == ConveyorType.VERTICAL ? worldPosition.relative(Direction.DOWN) : worldPosition.relative(direction).below();
		default:
			return worldPosition.relative(direction);

		}
	}

	public Direction getDirectionForNext() {
		return getFacing().getOpposite();
	}

	public Direction getDirectionForLast() {
		return getFacing();
	}

	public ItemStack getItemOnBelt() {
		return this.<ComponentInventory>getComponent(IComponentType.Inventory).getItem(0);
	}

	public void setItemOnBelt(ItemStack item) {
		this.<ComponentInventory>getComponent(IComponentType.Inventory).setItem(0, item);
	}

	public ConveyorType getConveyorType() {
		return ConveyorType.values()[conveyorType.getValue()];
	}

	public Vector3f getLocalItemLocationVector() {
		return new Vector3f((float) (itemLocation.getValue().x() - (float) worldPosition.getX()), (float) (itemLocation.getValue().y() - (float) worldPosition.getY()), (float) (itemLocation.getValue().z() - (float) worldPosition.getZ()));
	}

	public Vector3f getDirectionVector() {
		Direction direction = getDirectionForNext();
		return new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ());
	}

	public Location getDefaultItemLocation(boolean setToEnd) {
		double x = worldPosition.getX() + 0.5D;
		double y = worldPosition.getY();
		double z = worldPosition.getZ() + 0.5D;

		switch (getConveyorType()) {
		case SLOPED_DOWN:
			y += -4.0D / 16.0D;
			break;
		case SLOPED_UP:
			y += 8.0D / 16.0D;
			break;
		}

		if (setToEnd) {
			Direction directionForNext = getDirectionForNext();

			x -= (directionForNext.getStepX() / 2.0);
			z -= (directionForNext.getStepZ() / 2.0);
		}

		return new Location(x, y, z);
	}

	public void cycleConveyorType() {
		if (conveyorType.getValue() + 1 <= ConveyorType.values().length - 1) {
			conveyorType.setValue(ConveyorType.values()[conveyorType.getValue() + 1].ordinal());
		} else {
			conveyorType.setValue(ConveyorType.values()[0].ordinal());
		}
	}

	@Override
	public void onEntityInside(BlockState state, World level, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity && !isRemoved()) {
			ItemEntity item = (ItemEntity) entity;

			if (entity.tickCount > 5 && !level.isClientSide) {

				ItemStack stack = item.getItem().copy();

				ItemStack inserted = addItemOnBelt(stack, getDefaultItemLocation(false)).copy();

				stack.shrink(inserted.getCount());

				item.setItem(stack);

			}
		} else if (running.getValue() && entity instanceof LivingEntity && ((LivingEntity) entity).blockPosition().equals(getBlockPos())) {
			LivingEntity living = (LivingEntity) entity;
			if (living instanceof PlayerEntity && !level.isClientSide()) {
				return;
			} else if (!(living instanceof PlayerEntity) && level.isClientSide) {
				return;
			}

			double deltaY = living.getY() - living.blockPosition().getY();

			if (deltaY > BlockConveyorBelt.MAX_Y) {
				return;
			}

			List<ItemStack> armorPieces = new ArrayList<>();
			living.getArmorSlots().forEach(piece -> armorPieces.add(piece));

			if (armorPieces.size() > 3 && armorPieces.get(0).getItem().is(VoltaicTags.Items.INSULATES_PLAYER_FEET)) {
				return;
			}

			Vector3f dirVec = getDirectionVector();
			dirVec.mul(1.0F / 16.0F);
			dirVec.mul((float) properties.conveyorClass.speed);
			living.push(dirVec.x(), 0, dirVec.z());
		}
	}

	@Override
	public void onBlockDestroyed() {
		if (!level.isClientSide) {

			ItemStack stack = getItemOnBelt().copy();

			if (stack.isEmpty()) {
				return;
			}

			double d0 = EntityType.ITEM.getWidth();
			double d1 = 1.0 - d0;
			double d2 = d0 / 2.0;
			double d3 = Math.floor(getBlockPos().getX()) + level.random.nextDouble() * d1 + d2;
			double d4 = Math.floor(getBlockPos().getY()) + level.random.nextDouble() * d1;
			double d5 = Math.floor(getBlockPos().getZ()) + level.random.nextDouble() * d1 + d2;

			ItemEntity itementity = new ItemEntity(level, d3, d4, d5, stack);
			itementity.setDeltaMovement(Voltaic.RANDOM.nextGaussian() * (double) 0.05F, Voltaic.RANDOM.nextGaussian() * (double) 0.05F + (double) 0.2F, Voltaic.RANDOM.nextGaussian() * (double) 0.05F);
			level.addFreshEntity(itementity);
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putInt("conveyorwait", wait);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		wait = compound.getInt("conveyorwait");
	}

}
