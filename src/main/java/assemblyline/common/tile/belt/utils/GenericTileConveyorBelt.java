package assemblyline.common.tile.belt.utils;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.settings.Constants;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class GenericTileConveyorBelt extends GenericTile {

    public static final int MAX_SPREAD = 16;

    public static final int MIN_SPREAD = 0;

    public static final BlockPos[] OFFSETS = {
            //
            new BlockPos(0, 0, 0).relative(Direction.EAST),
            //
            new BlockPos(0, 0, 0).relative(Direction.WEST),
            //
            new BlockPos(0, 0, 0).relative(Direction.NORTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.SOUTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.EAST),
            //
            new BlockPos(0, 0, 0).relative(Direction.WEST),
            //
            new BlockPos(0, 0, 0).relative(Direction.NORTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.SOUTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.EAST),
            //
            new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.WEST),
            //
            new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.NORTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.DOWN).relative(Direction.SOUTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.EAST),
            //
            new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.WEST),
            //
            new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.NORTH),
            //
            new BlockPos(0, 0, 0).relative(Direction.UP).relative(Direction.SOUTH),
            //
    };


    public final Property<Integer> currentSpread = property(new Property<>(PropertyTypes.INTEGER, "currentspread", 0)).setNoUpdateServer();
    public final Property<Boolean> running = property(new Property<>(PropertyTypes.BOOLEAN, "running", false)).setNoUpdateServer();
    public final Property<Boolean> isQueueReady = property(new Property<>(PropertyTypes.BOOLEAN, "isqueueready", false)).setNoUpdateServer();
    public final Property<Boolean> waiting = property(new Property<>(PropertyTypes.BOOLEAN, "waiting", false)).setNoUpdateServer();
    public final Property<Location> itemLocation = property(new Property<>(PropertyTypes.LOCATION, "conveyorobject", new Location(0, 0, 0))).setNoUpdateServer();
    public final Property<Integer> conveyorType = property(new Property<>(PropertyTypes.INTEGER, "conveyortype", ConveyorType.HORIZONTAL.ordinal())).setNoUpdateServer();
    public final Property<Boolean> isPusher = property(new Property<>(PropertyTypes.BOOLEAN, "pusher", false)).setNoUpdateServer();
    public final Property<Boolean> isPuller = property(new Property<>(PropertyTypes.BOOLEAN, "puller", false)).setNoUpdateServer();

    public int wait = 0;
    public final ArrayList<GenericTileConveyorBelt> inQueue = new ArrayList<>();

    private boolean hasDroppedThisTick = false; // There is a dupe bug I found. Unknown how it happens but this fixes it

    private final ConveyorBeltProperties properties;

    public final Property<Boolean> canMove = property(new Property<>(PropertyTypes.BOOLEAN, "canmove", false)).setNoUpdateServer();

    public GenericTileConveyorBelt(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, ConveyorBeltProperties properties) {
        super(type, worldPosition, blockState);
        addComponent(new ComponentTickable(this).tickCommon(this::tickCommon));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().forceSize(properties.invSize)));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT).maxJoules(Constants.CONVEYORBELT_USAGE * 100));
        this.properties = properties;
    }

    public void tickCommon(ComponentTickable tickable) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        ItemStack stackOnBelt = getItemOnBelt();

        if (stackOnBelt.isEmpty()) {
            itemLocation.set(getDefaultItemLocation(false));
        }

        hasDroppedThisTick = false;

        isQueueReady.set(stackOnBelt.isEmpty());

        running.set(currentSpread.get() > MIN_SPREAD && isQueueReady.get());

        BlockEntity nextBlockEntity = level.getBlockEntity(getNextPos());

        //Direction facing = getFacing();

        boolean nextNotNull = nextBlockEntity != null;

        isPusher.set(properties.canBePusher && nextNotNull && !(nextBlockEntity instanceof GenericTileConveyorBelt) && level.getCapability(Capabilities.ItemHandler.BLOCK, nextBlockEntity.getBlockPos(), nextBlockEntity.getBlockState(), nextBlockEntity, getDirectionForNext().getOpposite()) != null);

        if (currentSpread.get() > MIN_SPREAD) {

            Vector3f move = getDirectionVector();

            if (!stackOnBelt.isEmpty()) {

                boolean shouldTransfer = shouldTransfer(nextBlockEntity, itemLocation.get().toBlockPos());

                if (nextBlockEntity instanceof GenericTileConveyorBelt belt) {

                    if (shouldTransfer) {
                        if (!belt.inQueue.contains(this)) {

                            belt.inQueue.add(this);

                        }

                        if (belt.inQueue.get(0) == this && belt.isQueueReady.get()) {

                            waiting.set(false);

                            belt.inQueue.remove(0);

                            belt.addItemOnBelt(stackOnBelt.copy(), itemLocation.get());

                            setItemOnBelt(ItemStack.EMPTY);

                        } else {

                            waiting.set(true);

                        }

                    }

                } else if (nextNotNull) {

                    if (shouldTransfer) {
                        Direction direction = getFacing();

                        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, nextBlockEntity.getBlockPos(), nextBlockEntity.getBlockState(), nextBlockEntity, direction);

                        if (handler != null) {

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

                        } else if (canMove.get()) {

                            Vector3f local = getLocalItemLocationVector();

                            Direction dir = getDirectionForNext();

                            float stepX = dir.getStepX();
                            float stepZ = dir.getStepZ();

                            float absX = Math.abs(local.x());
                            float absZ = Math.abs(local.z());

                            boolean xIs = stepX != 0 && stepX < 0 ? absX <= 0.2F : absX >= 0.8F;
                            boolean zIs = stepZ != 0 && stepZ < 0 ? absZ <= 0.2F : absZ >= 0.8F;

                            if (xIs || zIs) {
                                dropItem(stackOnBelt, move);
                            }

                        }
                    }

                } else if (canMove.get()) {
                    Vector3f local = getLocalItemLocationVector();

                    Direction dir = getDirectionForNext();

                    float stepX = dir.getStepX();
                    float stepZ = dir.getStepZ();

                    float absX = Math.abs(local.x());
                    float absZ = Math.abs(local.z());

                    boolean xIs = stepX != 0 && stepX < 0 ? absX <= 0.2F : absX >= 0.8F;
                    boolean zIs = stepZ != 0 && stepZ < 0 ? absZ <= 0.2F : absZ >= 0.8F;

                    if (xIs || zIs) {
                        dropItem(stackOnBelt, move);
                    }
                }

                if (!shouldTransfer && canMove.get()) {

                    move.mul(1 / 16.0f);

                    double speed = properties.conveyorClass.speed;

                    double x = move.x() * speed;
                    double y = move.y();
                    double z = move.z() * speed;

                    ConveyorType type = getConveyorType();

                    if (type != ConveyorType.HORIZONTAL) {

                        y += 1 / 16.0f * (type == ConveyorType.SLOPED_DOWN ? -1 : 1) * speed;

                    }

                    itemLocation.set(itemLocation.get().add(x, y, z));

                    Vector3f localVector = getLocalItemLocationVector();

                    if (x == 0 && localVector.x() - 0.5 * Math.signum(localVector.x()) != 0) {

                        localVector.x = 0.5F * Math.signum(localVector.x);
                        Location loc = new Location(worldPosition.getX() + localVector.x, worldPosition.getY() + localVector.y, worldPosition.getZ() + localVector.z);
                        itemLocation.set(loc);

                    } else if (z == 0 && localVector.z() - 0.5 * Math.signum(localVector.z()) != 0) {

                        localVector.z = 0.5F * Math.signum(localVector.z);
                        Location loc = new Location(worldPosition.getX() + localVector.x, worldPosition.getY() + localVector.y, worldPosition.getZ() + localVector.z);
                        itemLocation.set(loc);

                    }

                }

            }

            isQueueReady.set(stackOnBelt.isEmpty());

            running.set(currentSpread.get() > MIN_SPREAD /*&& (!waiting.get() || isQueueReady.get())*/);

        }

        if (level.isClientSide()) {
            return;
        }

        BlockEntity lastBlockEntity = level.getBlockEntity(worldPosition.relative(getDirectionForLast()));

        isPuller.set(properties.canBePuller && lastBlockEntity != null && !(lastBlockEntity instanceof GenericTileConveyorBelt) && level.getCapability(Capabilities.ItemHandler.BLOCK, lastBlockEntity.getBlockPos(), lastBlockEntity.getBlockState(), lastBlockEntity, getDirectionForLast().getOpposite()) != null);

        if (isQueueReady.get()) {

            if (!inQueue.isEmpty()) {

                while (true) {

                    GenericTileConveyorBelt queue = inQueue.get(0);

                    if (!queue.isRemoved() && queue.waiting.get()) {

                        break;

                    }

                    inQueue.remove(0);

                    if (inQueue.isEmpty()) {

                        break;

                    }

                }

            } else if (lastBlockEntity != null && isPuller.get()) {

                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, lastBlockEntity.getBlockPos(), lastBlockEntity.getBlockState(), lastBlockEntity, getDirectionForLast().getOpposite());

                if (handler != null) {

                    for (int slot = 0; slot < handler.getSlots(); slot++) {

                        ItemStack accepted = addItemOnBelt(handler.extractItem(slot, 64, true), getDefaultItemLocation(true));

                        if (!accepted.isEmpty()) {

                            handler.extractItem(slot, accepted.getCount(), level.isClientSide);

                            break;
                        }
                    }
                }
            }
        }

        int currSpread = currentSpread.get();

        int maxSpread = 0;

        BlockEntity offsetTile;

        for (BlockPos offset : OFFSETS) {

            offsetTile = level.getBlockEntity(worldPosition.offset(offset));

            if (offsetTile instanceof GenericTileConveyorBelt belt) {

                int offsetSpread = belt.currentSpread.get();

                if (offsetSpread - 1 > maxSpread) {

                    maxSpread = offsetSpread - 1;

                }

            }
        }

        currentSpread.set(maxSpread);

        if (currSpread > currentSpread.get()) {

            currentSpread.set(MIN_SPREAD);

        }

        if (currentSpread.get() == MIN_SPREAD || currentSpread.get() == MAX_SPREAD) {

            if (electro.getJoulesStored() < Constants.CONVEYORBELT_USAGE) {

                currentSpread.set(MIN_SPREAD);

            } else {

                electro.joules(electro.getJoulesStored() - Constants.CONVEYORBELT_USAGE);

                currentSpread.set(MAX_SPREAD);

            }
        }
    }

    public boolean shouldTransfer(BlockEntity next, BlockPos conveyorItemPos) {

        if (!level.isClientSide) {
            canMove.set(true);
        }

        Vector3f local = getLocalItemLocationVector();

        Vector3f direction = getDirectionVector();

        Direction dir = getDirectionForNext();

        float stepX = dir.getStepX();
        float stepZ = dir.getStepZ();

        float absX = Math.abs(local.x());
        float absZ = Math.abs(local.z());

        boolean xIs = stepX != 0 && stepX < 0 ? absX <= 0.2F : absX >= 0.8F;
        boolean zIs = stepZ != 0 && stepZ < 0 ? absZ <= 0.2F : absZ >= 0.8F;

        float coordComponent = local.dot(direction);
        ConveyorType type = ConveyorType.values()[conveyorType.get()];
        if (type != ConveyorType.HORIZONTAL) {
            return type == ConveyorType.SLOPED_DOWN ? itemLocation.get().y() <= worldPosition.getY() - 1 : itemLocation.get().y() >= worldPosition.getY() + 1;
        }

        float value = 1;

        if (next instanceof GenericTileConveyorBelt belt && (belt.inQueue.isEmpty() || belt.inQueue.get(0).getBlockPos().equals(getBlockPos())) && belt.isQueueReady.get()) {

            ConveyorType beltType = belt.getConveyorType();

            value = (beltType == ConveyorType.SLOPED_UP || beltType == ConveyorType.VERTICAL) ? 1 : 1.25f;

        } else if (!(next instanceof GenericTileConveyorBelt) && next != null && level.getCapability(Capabilities.ItemHandler.BLOCK, next.getBlockPos(), next.getBlockState(), next, dir.getOpposite()) instanceof IItemHandler handler && (xIs || zIs)) {
            if (level.isClientSide) {
                return false;
            }
            ItemStack beltItem = getItemOnBelt().copy();
            for (int i = 0; i < handler.getSlots(); i++) {
                beltItem = handler.insertItem(i, getItemOnBelt(), true);
                if (beltItem.isEmpty()) {
                    return true;
                }
            }
            canMove.set(false);
            return false;
        }

        if (direction.x() + direction.y() + direction.z() > 0) {
            return !conveyorItemPos.equals(worldPosition) && coordComponent >= value;
        }
        return !conveyorItemPos.equals(worldPosition) && coordComponent >= value - 1;
    }

    //Returns the amount that was taken
    public ItemStack addItemOnBelt(ItemStack add, Location object) {

        ItemStack taken = ItemStack.EMPTY;

        if (add.isEmpty()) {
            return taken;
        }

        taken = ItemStack.EMPTY;

        boolean inserted = false;

        ItemStack beltItem = getItemOnBelt();

        if (beltItem.isEmpty()) {

            taken = add.copy();

            setItemOnBelt(add.copy());

            inserted = true;

        } else if (ItemStack.isSameItem(beltItem, add)) {

            int room = beltItem.getMaxStackSize() - beltItem.getCount();

            int accepted = Math.min(room, add.getCount());

            if (accepted > 0) {

                taken = add.copy();

                beltItem.grow(accepted);

                setItemOnBelt(beltItem.copy());

                taken.setCount(accepted);

                inserted = true;
            }

        }

        if (inserted) {

            if (getConveyorType() == ConveyorType.VERTICAL) {

                Vector3f vec = getDirectionVector();

                object = object.add(vec.x(), vec.y(), vec.z());

            }

            itemLocation.set(object);
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

        return switch (ConveyorType.values()[conveyorType.get()]) {
            case SLOPED_DOWN -> worldPosition.relative(direction).below();
            case SLOPED_UP -> worldPosition.relative(direction).above();
            case VERTICAL -> level.getBlockEntity(worldPosition.relative(Direction.UP)) instanceof GenericTileConveyorBelt belt && belt.getConveyorType() == ConveyorType.VERTICAL ? worldPosition.relative(Direction.UP) : worldPosition.relative(direction).above();
            default -> worldPosition.relative(direction);

        };
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
        return ConveyorType.values()[conveyorType.get()];
    }

    public Vector3f getLocalItemLocationVector() {
        return new Vector3f((float) (itemLocation.get().x() - worldPosition.getX()), (float) (itemLocation.get().y() - worldPosition.getY()), (float) (itemLocation.get().z() - worldPosition.getZ()));
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
        if (conveyorType.get() + 1 <= ConveyorType.values().length - 1) {
            conveyorType.set(ConveyorType.values()[conveyorType.get() + 1].ordinal());
        } else {
            conveyorType.set(ConveyorType.values()[0].ordinal());
        }
    }

    @Override
    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof ItemEntity item) {

            if (entity.tickCount > 5 && !level.isClientSide) {

                ItemStack stack = item.getItem().copy();

                ItemStack inserted = addItemOnBelt(stack, getDefaultItemLocation(false)).copy();

                stack.shrink(inserted.getCount());

                item.setItem(stack);

            }
        } else if (running.get() && entity instanceof LivingEntity living && living.getOnPos().equals(getBlockPos())) {

            if(living instanceof Player && !level.isClientSide()) {
                return;
            } else if (level.isClientSide) {
                return;
            }

            double deltaY = living.getY() - living.getOnPos().getY();

            if(deltaY > BlockConveyorBelt.MAX_Y) {
                return;
            }


            List<ItemStack> armorPieces = new ArrayList<>();
            living.getArmorSlots().forEach(piece -> armorPieces.add(piece));

            if (armorPieces.size() > 3 && armorPieces.get(0).is(ElectrodynamicsTags.Items.INSULATES_PLAYER_FEET)) {
                return;
            }

            Vector3f dirVec = getDirectionVector();
            dirVec = dirVec.mul(1.0F / 16.0F);
            dirVec = dirVec.mul((float) properties.conveyorClass.speed);
            living.push(new Vec3(dirVec.x, 0, dirVec.z));
        }
    }

    @Override
    public void onBlockDestroyed() {
        if (!level.isClientSide) {
            Containers.dropContents(level, getBlockPos(), (ComponentInventory) getComponent(IComponentType.Inventory));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("conveyorwait", wait);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        wait = compound.getInt("conveyorwait");
    }

}
