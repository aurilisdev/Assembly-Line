package assemblyline.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.DeferredRegisters;
import assemblyline.tile.TileConveyorBelt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockConveyorBelt extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape shape = VoxelShapes.create(0, 0, 0, 1, 5.0 / 16.0, 1);
	public final boolean running;

	public BlockConveyorBelt(boolean running) {
		super(Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).notSolid());
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
		this.running = running;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(DeferredRegisters.blockConveyorbelt));
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote) {
			if (tile instanceof TileConveyorBelt) {
				TileConveyorBelt belt = (TileConveyorBelt) tile;
				if (belt.joules < 0.5) {
					if (running) {
						world.setBlockState(pos, DeferredRegisters.blockConveyorbelt.getDefaultState().with(BlockConveyorBelt.FACING, state.get(BlockConveyorBelt.FACING)));
					}
				} else {
					if (belt.lastTime != world.getGameTime()) {
						belt.joules -= 0.5;
						belt.lastTime = world.getGameTime();
						if (!running) {
							world.setBlockState(pos, DeferredRegisters.blockConveyorbeltRunning.getDefaultState().with(BlockConveyorBelt.FACING, state.get(BlockConveyorBelt.FACING)));
						}
					}
				}
			}
			if (((BlockConveyorBelt) world.getBlockState(pos).getBlock()).running) {
				if (entityIn.getPosY() > pos.getY() + 4.0 / 16.0) {
					Direction dir = state.get(FACING).getOpposite();
					entityIn.addVelocity(dir.getXOffset() / 20.0, 0, dir.getZOffset() / 20.0);
					BlockPos next = pos.offset(dir);
					BlockState side = world.getBlockState(next);
					if (entityIn instanceof ItemEntity) {
						ItemEntity itemEntity = (ItemEntity) entityIn;
						if (!itemEntity.getItem().isEmpty()) {
							if (side.getBlock() instanceof BlockManipulator) {
								if (side.get(BlockManipulator.FACING) == dir.getOpposite()) { // TODO: This could be optimized by moving it to the manipulator tile so it
																								// doesnt do it for every item each individually but for all at the same time
									BlockPos chestPos = next.offset(dir);
									TileEntity chestTile = world.getTileEntity(chestPos);
									if (chestTile instanceof IInventory) {
										itemEntity.setItem(HopperTileEntity.putStackInInventoryAllSlots(null, (IInventory) chestTile, itemEntity.getItem(), dir.getOpposite()));
										if (itemEntity.getItem().isEmpty()) {
											itemEntity.remove();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		worldIn.markBlockRangeForRenderUpdate(pos, state, newState);
		if (!(newState.getBlock() instanceof BlockConveyorBelt)) {
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileConveyorBelt();
	}
}
