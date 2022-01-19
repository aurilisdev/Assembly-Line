package assemblyline.common.tile.generic;

import assemblyline.client.ClientEvents;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class TileOutlineArea extends GenericTile {

	public static final int CHECK_HEIGHT = 5;
	protected static final int DEFAULT_CHECK_WIDTH = 1;
	protected static final int DEFAULT_CHECK_LENGTH = 1;
	protected static final int DEFAULT_CHECK_HEIGHT = 5;
	protected static final int MAX_CHECK_WIDTH = 25;
	protected static final int MAX_CHECK_LENGTH = 25;
	protected int currentWidth;
	protected int currentLength;
	protected int currentHeight;
	public int clientLength;
	public int clientWidth;
	public int clientHeight;
	protected AABB checkArea;
	
	protected TileOutlineArea(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	public abstract AABB getAABB(int width, int length, int height, boolean isFlipped, boolean isClient, TileOutlineArea tile);
	
	protected void createPacket(CompoundTag nbt) {
		nbt.putInt("clientLength", currentLength);
		nbt.putInt("clientWidth", currentWidth);
		nbt.putInt("clientHeight", currentHeight);
	}

	protected void readPacket(CompoundTag nbt) {
		clientLength = nbt.getInt("clientLength");
		clientWidth = nbt.getInt("clientWidth");
		clientHeight = nbt.getInt("clientHeight");
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		if(getLevel().isClientSide) {
			ClientEvents.outlines.remove(getBlockPos());
		}
	}

}
