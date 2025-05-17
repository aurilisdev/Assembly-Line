package assemblyline.common.tile.util;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import voltaic.prefab.properties.types.PropertyTypes;
import voltaic.prefab.properties.variant.SingleProperty;
import voltaic.prefab.tile.GenericTile;

public abstract class TileOutlineArea extends GenericTile {

	public static final int CHECK_HEIGHT = 5;
	protected static final int DEFAULT_CHECK_WIDTH = 1;
	protected static final int DEFAULT_CHECK_LENGTH = 1;
	protected static final int DEFAULT_CHECK_HEIGHT = 5;
	protected static final int MAX_CHECK_WIDTH = 25;
	protected static final int MAX_CHECK_LENGTH = 25;
	public SingleProperty<Integer> width = property(new SingleProperty<>(PropertyTypes.INTEGER, "width", DEFAULT_CHECK_WIDTH));
	public SingleProperty<Integer> length = property(new SingleProperty<>(PropertyTypes.INTEGER, "length", DEFAULT_CHECK_LENGTH));
	public SingleProperty<Integer> height = property(new SingleProperty<>(PropertyTypes.INTEGER, "height", DEFAULT_CHECK_HEIGHT));
	protected AABB checkArea;

	protected TileOutlineArea(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AABB getAABB(int width, int length, int height, boolean isFlipped) {

		Direction facing = getFacing();

		if (isFlipped) {
			facing = facing.getOpposite();
		}

		Direction counterClockwise = facing.getCounterClockWise();
		Direction clockwise = facing.getClockWise();

		BlockPos pos = getBlockPos().relative(facing);

		BlockPos start = pos.relative(facing, length - 1).relative(counterClockwise, width / 2).relative(Direction.UP, height - 1);

		BlockPos end = pos.relative(clockwise, width / 2);

		return encapsulatingFullBlocks(start, end);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (getLevel().isClientSide) {
			HandlerHarvesterLines.removeLines(getBlockPos());
		}
	}

	public static AABB encapsulatingFullBlocks(BlockPos startPos, BlockPos endPos) {
		return new AABB(
				//
				(double) Math.min(startPos.getX(), endPos.getX()),
				//
				(double) Math.min(startPos.getY(), endPos.getY()),
				//
				(double) Math.min(startPos.getZ(), endPos.getZ()),
				//
				(double) (Math.max(startPos.getX(), endPos.getX()) + 1),
				//
				(double) (Math.max(startPos.getY(), endPos.getY()) + 1),
				//
				(double) (Math.max(startPos.getZ(), endPos.getZ()) + 1)
		//
		);
	}

}
