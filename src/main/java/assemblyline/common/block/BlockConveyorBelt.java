package assemblyline.common.block;

import java.util.Arrays;
import java.util.List;

import assemblyline.common.tile.TileConveyorBelt;
import assemblyline.common.tile.TileConveyorBelt.ConveyorType;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockConveyorBelt extends GenericEntityBlockWaterloggable {
	private static final VoxelShape shape = Shapes.box(0, 0, 0, 1, 5.0 / 16.0, 1);

	public BlockConveyorBelt() {
		super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
		registerDefaultState(stateDefinition.any().setValue(GenericEntityBlock.FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return shape;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return Arrays.asList(new ItemStack(AssemblyLineBlocks.blockConveyorBelt));
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!world.isClientSide && tile instanceof TileConveyorBelt belt && entityIn instanceof ItemEntity item && entityIn.tickCount > 5) {
			item.setItem(belt.addItemOnBelt(item.getItem()));
		}
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		if (player.level.getBlockEntity(pos) instanceof TileConveyorBelt belt) {
			if (belt.conveyorType.ordinal() + 1 <= ConveyorType.values().length - 1) {
				belt.conveyorType = ConveyorType.values()[belt.conveyorType.ordinal() + 1];
			} else {
				belt.conveyorType = ConveyorType.values()[0];
			}
			belt.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof BlockConveyorBelt)) {
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (!(state.getBlock() == newState.getBlock() && state.getValue(GenericEntityBlock.FACING) != newState.getValue(GenericEntityBlock.FACING)) && tile instanceof GenericTile generic) {
				if (generic.hasComponent(ComponentType.Inventory)) {
					Containers.dropContents(worldIn, pos, generic.<ComponentInventory>getComponent(ComponentType.Inventory));
				}
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(GenericEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(GenericEntityBlock.FACING);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileConveyorBelt(pos, state);
	}
}
