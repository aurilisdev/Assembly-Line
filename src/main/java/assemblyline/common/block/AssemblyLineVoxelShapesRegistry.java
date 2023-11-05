package assemblyline.common.block;

import java.util.stream.Stream;

import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.common.block.voxelshapes.VoxelShapes;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AssemblyLineVoxelShapesRegistry {

	/**
	 * By convention this will be in alphabetical order
	 */

	public static void init() {

		/* AUTOCRAFTER */

		VoxelShape autocrafter = Stream.of(
				//
				Block.box(1, 7, 1, 15, 12, 15),
				//
				Block.box(0, 12, 0, 16, 16, 16),
				//
				Block.box(0, 0, 15, 16, 1, 16),
				//
				Block.box(0, 0, 1, 1, 1, 15),
				//
				Block.box(4, 0, 4, 12, 1, 12),
				//
				Block.box(4, 4, 15.05, 12, 12, 16.05),
				//
				Block.box(-0.05, 4, 4, 0.95, 12, 12),
				//
				Block.box(15.05, 4, 4, 16.05, 12, 12),
				//
				Block.box(4, 4, -0.05, 12, 12, 0.95),
				//
				Block.box(15, 0, 1, 16, 1, 15),
				//
				Block.box(0, 0, 0, 16, 1, 1),
				//
				Block.box(0, 1, 0, 16, 7, 16)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockAutocrafter, autocrafter, Direction.EAST);

		/* BLOCK BREAKER */

		VoxelShape blockbreaker = Stream.of(
				//
				Block.box(0, 0, 11, 16, 16, 15),
				//
				Block.box(0, 0, 15, 16, 1, 16),
				//
				Block.box(0, 15, 15, 16, 16, 16),
				//
				Block.box(0, 1, 15, 16, 15, 16),
				//
				Block.box(1, 1, 6, 15, 15, 11),
				//
				Block.box(0, 0, 4, 16, 16, 7),
				//
				Block.box(0, 0, 2, 16, 2, 4),
				//
				Block.box(0, 14, 2, 16, 16, 4),
				//
				Block.box(0, 2, 2, 16, 14, 4)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockBlockBreaker, blockbreaker, Direction.SOUTH);

		/* BLOCK PLACER */

		VoxelShape blockplacer = Stream.of(
				//
				Block.box(5, 1, 1, 11, 15, 15),
				//
				Block.box(2, 0, 0, 5, 16, 16),
				//
				Block.box(0, 2, 2, 2, 14, 14),
				//
				Block.box(0, 0, 0, 2, 16, 2),
				//
				Block.box(0, 0, 14, 2, 16, 16),
				//
				Block.box(0, 14, 2, 2, 16, 14),
				//
				Block.box(0, 0, 2, 2, 2, 14),
				//
				Block.box(15, 4, 4, 16, 12, 12),
				//
				Block.box(11, 0, 0, 15, 16, 16),
				//
				Block.box(15, 0, 0, 16, 16, 1),
				//
				Block.box(15, 15, 1, 16, 16, 15),
				//
				Block.box(15, 0, 15, 16, 16, 16),
				//
				Block.box(15, 0, 1, 16, 1, 15)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockBlockPlacer, blockplacer, Direction.EAST);

		/* ENERGIZED RANCHER */

		VoxelShape energizedrancher = Stream.of(
				//
				Block.box(0, 0, 11, 16, 16, 15),
				//
				Block.box(0, 0, 15, 16, 1, 16),
				//
				Block.box(0, 15, 15, 16, 16, 16),
				//
				Block.box(0, 1, 15, 16, 15, 16),
				//
				Block.box(1, 1, 6, 15, 15, 11),
				//
				Block.box(0, 0, 4, 16, 16, 7),
				//
				Block.box(0, 0, 2, 16, 2, 4),
				//
				Block.box(0, 14, 2, 16, 16, 4),
				//
				Block.box(0, 2, 2, 16, 14, 4)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockRancher, energizedrancher, Direction.SOUTH);

		/* FARMER */

		VoxelShape farmer = Stream.of(
				//
				Block.box(0, 1, 0, 16, 5, 16),
				//
				Block.box(0, 0, 0, 16, 1, 1),
				//
				Block.box(0, 0, 15, 16, 1, 16),
				//
				Block.box(0, 0, 1, 1, 1, 15),
				//
				Block.box(4, 0, 4, 12, 1, 12),
				//
				Block.box(15, 0, 1, 16, 1, 15),
				//
				Block.box(1, 5, 1, 15, 10, 15),
				//
				Block.box(0, 9, 0, 16, 12, 16),
				//
				Block.box(0, 12, 0, 16, 14, 2),
				//
				Block.box(0, 12, 14, 16, 14, 16),
				//
				Block.box(14, 12, 2, 16, 14, 14),
				//
				Block.box(0, 12, 2, 2, 14, 14),
				//
				Block.box(2, 12, 2, 14, 15, 14),
				//
				Block.box(0, 15, 0, 16, 16, 3),
				//
				Block.box(0, 15, 13, 16, 16, 16),
				//
				Block.box(0, 15, 3, 3, 16, 13),
				//
				Block.box(13, 15, 3, 16, 16, 13),
				//
				Block.box(14, 14, 14, 15, 15, 15),
				//
				Block.box(1, 14, 14, 2, 15, 15),
				//
				Block.box(1, 14, 1, 2, 15, 2),
				//
				Block.box(14, 14, 1, 15, 15, 2)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockFarmer, farmer, Direction.EAST);

		/* MOB GRINDER */

		VoxelShape mobgrinder = Stream.of(
				//
				Block.box(0, 0, 11, 16, 16, 15),
				//
				Block.box(0, 0, 15, 16, 1, 16),
				//
				Block.box(0, 15, 15, 16, 16, 16),
				//
				Block.box(0, 1, 15, 16, 15, 16),
				//
				Block.box(1, 1, 6, 15, 15, 11),
				//
				Block.box(0, 0, 4, 16, 16, 7),
				//
				Block.box(0, 0, 2, 16, 2, 4),
				//
				Block.box(0, 14, 2, 16, 16, 4),
				//
				Block.box(0, 2, 2, 16, 14, 4)
		//
		).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

		VoxelShapes.registerShape(AssemblyLineBlocks.blockMobGrinder, mobgrinder, Direction.SOUTH);

	}

}
