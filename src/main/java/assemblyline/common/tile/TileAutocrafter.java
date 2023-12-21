package assemblyline.common.tile;

import java.util.List;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.Constants;
import assemblyline.registers.AssemblyLineBlockTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class TileAutocrafter extends GenericTile {
	
	//public boolean isPowered = false;

	// public boolean isPowered = false;

	public TileAutocrafter(BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineBlockTypes.TILE_AUTOCRAFTER.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, false, true).maxJoules(Constants.AUTOCRAFTER_USAGE * 20).setInputDirections(Direction.values()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(9).outputs(1)).setSlotsByDirection(Direction.DOWN, 9).setSlotsByDirection(Direction.UP, 1, 3, 4, 5, 7).setSlotsByDirection(Direction.SOUTH, 6, 7, 8).setSlotsByDirection(Direction.NORTH, 0, 1, 2).setSlotsByDirection(Direction.WEST, 2, 5, 8).setSlotsByDirection(Direction.EAST, 0, 3, 6));
		addComponent(new ComponentContainerProvider("container.autocrafter", this).createMenu((id, player) -> new ContainerAutocrafter(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	public static boolean shapedMatches(ComponentInventory inv, ShapedRecipe shaped) {
		for (int i = 0; i <= 3 - shaped.getWidth(); ++i) {
			for (int j = 0; j <= 3 - shaped.getHeight(); ++j) {
				if (TileAutocrafter.shapedMatches(inv, shaped, i, j, true) || TileAutocrafter.shapedMatches(inv, shaped, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean shapedMatches(ComponentInventory inventory, ShapedRecipe shaped, int x, int y, boolean flag) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int k = i - x;
				int l = j - y;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < shaped.getWidth() && l < shaped.getHeight()) {
					if (flag) {
						ingredient = shaped.getIngredients().get(shaped.getWidth() - k - 1 + l * shaped.getWidth());
					} else {
						ingredient = shaped.getIngredients().get(k + l * shaped.getWidth());
					}
				}

				if (!ingredient.test(inventory.getItem(i + j * 3))) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean shapelessMatches(ComponentInventory inventory, ShapelessRecipe shaped) {
		StackedContents stackedcontents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		boolean isSimple = shaped.getIngredients().stream().allMatch(Ingredient::isSimple);
		int i = 0;

		for (int j = 0; j < 9; ++j) {
			ItemStack itemstack = inventory.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				if (isSimple) {
					stackedcontents.accountStack(itemstack, 1);
				} else {
					inputs.add(itemstack);
				}
			}
		}
		return i == shaped.getIngredients().size() && (isSimple ? stackedcontents.canCraft(shaped, (IntList) null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, shaped.getIngredients()) != null);
	}

	public void tickServer(ComponentTickable tick) {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		boolean canContinue = electro.getJoulesStored() >= Constants.AUTOCRAFTER_USAGE;
		if (tick.getTicks() % 20 == 0) {
			if (canContinue) {
				ComponentInventory inventory = getComponent(IComponentType.Inventory);
				for (int i = 0; i < 9; i++) {
					if (inventory.getItem(i).getCount() == 1) {
						canContinue = false;
					}
				}
				if (canContinue) {
					List<CraftingRecipe> recipes = level.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
					ItemStack result = ItemStack.EMPTY;
					canContinue = false;
					for (CraftingRecipe recipe : recipes) {
						result = recipe.getResultItem();
						if (recipe instanceof ShapedRecipe shapedRecipe) {
							if (shapedMatches(inventory, shapedRecipe)) {
								canContinue = true;
								break;
							}
						} else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
							if (shapelessMatches(inventory, shapelessRecipe)) {
								canContinue = true;
								break;
							}
						}
					}
					if (canContinue) {
						ItemStack currentItemStack = inventory.getItem(9);
						if (currentItemStack.isEmpty() || ItemStack.isSame(result, currentItemStack) && currentItemStack.getCount() + result.getCount() <= result.getMaxStackSize()) {
							for (int i = 0; i < 9; i++) {
								inventory.getItem(i).shrink(1);
							}
							if (currentItemStack.isEmpty()) {
								inventory.setItem(9, result.copy());
							} else {
								currentItemStack.grow(result.getCount());
							}
							electro.joules(electro.getJoulesStored() - Constants.AUTOCRAFTER_USAGE);
						}
					}
				}
			}
		}
	}
}
