package assemblyline.common.event;

import java.util.ArrayList;
import java.util.List;

import assemblyline.AssemblyLine;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.registers.AssemblyLineAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.utilities.BlockEntityUtils;
import voltaic.prefab.utilities.ItemUtils;

@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.GAME)
public class EventHandler {

    @SubscribeEvent
    public static void captureDroppedItems(LivingDropsEvent event) {

        Entity entity = event.getEntity();

        BlockPos pos = entity.getData(AssemblyLineAttachmentTypes.GRINDER_KILLED_MOB);

        if (pos.equals(BlockEntityUtils.OUT_OF_REACH)) {
            return;
        }

        if (entity.level().getBlockEntity(pos) instanceof TileMobGrinder grinder) {

            List<ItemStack> droppedItems = new ArrayList<>();

            event.getDrops().forEach(h -> droppedItems.add(h.getItem()));

            if(droppedItems.isEmpty()) {
                return;
            }

            ComponentInventory inv = grinder.getComponent(IComponentType.Inventory);

            int max = inv.getOutputStartIndex() + inv.getOutputContents().size();

            for(ItemStack item : droppedItems) {

                for (int i = inv.getOutputStartIndex(); i < max; i++) {

                    ItemStack contained = inv.getItem(i);

                    int room = contained.getMaxStackSize() - contained.getCount();

                    int amtAccepted = Math.min(room, item.getCount());

                    if(amtAccepted == 0) {
                        continue;
                    }

                    if (contained.isEmpty()) {

                        inv.setItem(i, new ItemStack(item.getItem(), amtAccepted));

                        item.shrink(amtAccepted);

                    } else if (ItemUtils.testItems(item.getItem(), contained.getItem())) {

                        contained.grow(amtAccepted);

                        item.shrink(amtAccepted);

                        inv.setChanged();

                    }

                    if(item.isEmpty()) {
                        break;
                    }

                }

            }

            event.setCanceled(true);
        }

    }

}
