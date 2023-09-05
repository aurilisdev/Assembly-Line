package assemblyline.common.block;

import assemblyline.common.tile.TileCrate;
import electrodynamics.prefab.block.GenericEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCrate extends GenericEntityBlock {

	public final int size;

	public BlockCrate(int size) {
		super(Properties.copy(Blocks.IRON_BLOCK).strength(3.5F).sound(SoundType.METAL).requiresCorrectToolForDrops());
		this.size = size;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileCrate(size, pos, state);
	}
}
