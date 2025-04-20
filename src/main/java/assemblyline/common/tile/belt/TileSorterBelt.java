package assemblyline.common.tile.belt;

import org.joml.Vector3f;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.tile.belt.utils.ConveyorBeltProperties;
import assemblyline.common.tile.belt.utils.ConveyorClass;
import assemblyline.common.tile.belt.utils.ConveyorType;
import assemblyline.common.tile.belt.utils.GenericTileConveyorBelt;
import assemblyline.registers.AssemblyLineTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentContainerProvider;
import voltaic.prefab.tile.components.type.ComponentInventory;

public class TileSorterBelt extends GenericTileConveyorBelt {

    public TileSorterBelt(BlockPos worldPosition, BlockState blockState) {
        super(AssemblyLineTiles.TILE_SORTERBELT.get(), worldPosition, blockState, ConveyorBeltProperties.builder(ConveyorClass.REGULAR).setNoPuller().setInvSize(19));
        addComponent(new ComponentContainerProvider("sorterbelt", this).createMenu((id, player) -> new ContainerSorterBelt(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    @Override
    public Direction getDirectionForNext() {

        Direction superDirection = super.getDirectionForNext();

        float stepX = superDirection.getStepX();
        float stepZ = superDirection.getStepZ();

        Vector3f localVector = getLocalItemLocationVector();

        float absX = Math.abs(localVector.x());
        float absZ = Math.abs(localVector.z());

        boolean xIs = stepX != 0 && stepX < 0 ? absX <= 0.5F : absX >= 0.5F;
        boolean zIs = stepZ != 0 && stepZ < 0 ? absZ <= 0.5F : absZ >= 0.5F;

        if (xIs || zIs) {

            ComponentInventory inv = getComponent(IComponentType.Inventory);

            ItemStack onBelt = getItemOnBelt();

            boolean isLeft = false;
            boolean isRight = false;

            for (int i = 1; i < 10; i++) {
                ItemStack comparator = inv.getItem(i);
                if (ItemStack.isSameItemSameComponents(comparator, onBelt)) {
                    isLeft = true;
                    break;
                }
            }
            for (int i = 10; i < 19; i++) {
                ItemStack comparator = inv.getItem(i);
                if (ItemStack.isSameItemSameComponents(comparator, onBelt)) {
                    isRight = true;
                    break;
                }
            }

            if (isLeft) {

                return superDirection.getCounterClockWise();

            } else if (isRight) {

                return superDirection.getClockWise();

            } else {

                return superDirection;

            }
        }

        return superDirection;

    }

    @Override
    public ConveyorType getConveyorType() {
        return ConveyorType.HORIZONTAL;
    }

    @Override
    public void cycleConveyorType() {

    }

}
