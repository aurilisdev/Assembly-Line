package assemblyline.common.tile;

import java.util.List;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.registers.AssemblyLineTiles;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import voltaic.prefab.tile.GenericTile;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.*;
import voltaic.prefab.utilities.BlockEntityUtils;

public class TileAutocrafter extends GenericTile {

	// public boolean isPowered = false;

	public TileAutocrafter(BlockPos worldPosition, BlockState blockState) {
		super(AssemblyLineTiles.TILE_AUTOCRAFTER.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, false, true).maxJoules(AssemblyLineConstants.AUTOCRAFTER_USAGE * 20).setInputDirections(BlockEntityUtils.MachineDirection.values()));
		addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(9).outputs(1))
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.BOTTOM, 9)
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.TOP, 1, 3, 4, 5, 7)
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.BACK, 6, 7, 8)
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.FRONT, 0, 1, 2)
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.LEFT, 2, 5, 8)
				//
				.setSlotsByDirection(BlockEntityUtils.MachineDirection.RIGHT, 0, 3, 6));
		addComponent(new ComponentContainerProvider("autocrafter", this).createMenu((id, player) -> new ContainerAutocrafter(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
		addComponent(new ComponentForgeEnergy(this));
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
		return i == shaped.getIngredients().size() && (isSimple ? stackedcontents.canCraft(shaped, (IntList) null) : RecipeMatcher.findMatches(inputs, shaped.getIngredients()) != null);
	}

	public void tickServer(ComponentTickable tick) {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		boolean canContinue = electro.getJoulesStored() >= AssemblyLineConstants.AUTOCRAFTER_USAGE;
		if (tick.getTicks() % 20 == 0) {
			if (canContinue) {
				ComponentInventory inventory = getComponent(IComponentType.Inventory);
				for (int i = 0; i < 9; i++) {
					if (inventory.getItem(i).getCount() == 1) {
						canContinue = false;
					}
				}
				if (canContinue) {
					List<RecipeHolder<CraftingRecipe>> recipes = level.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
					ItemStack result = ItemStack.EMPTY;
					canContinue = false;
					for (RecipeHolder<CraftingRecipe> recipe : recipes) {
						result = recipe.value().getResultItem(level.registryAccess());
						if (recipe.value() instanceof ShapedRecipe shapedRecipe) {
							if (shapedMatches(inventory, shapedRecipe)) {
								canContinue = true;
								break;
							}
						} else if (recipe.value() instanceof ShapelessRecipe shapelessRecipe) {
							if (shapelessMatches(inventory, shapelessRecipe)) {
								canContinue = true;
								break;
							}
						}
					}
					if (canContinue) {
						ItemStack currentItemStack = inventory.getItem(9);
						if (currentItemStack.isEmpty() || ItemStack.isSameItem(result, currentItemStack) && currentItemStack.getCount() + result.getCount() <= result.getMaxStackSize()) {
							for (int i = 0; i < 9; i++) {
								inventory.getItem(i).shrink(1);
							}
							if (currentItemStack.isEmpty()) {
								inventory.setItem(9, result.copy());
							} else {
								currentItemStack.grow(result.getCount());
							}
							electro.joules(electro.getJoulesStored() - AssemblyLineConstants.AUTOCRAFTER_USAGE);
						}
					}
				}
			}
		}
	}
}
