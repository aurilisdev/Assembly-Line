package assemblyline.common.event;

import java.util.ArrayList;
import java.util.List;

import assemblyline.AssemblyLine;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.prefab.utils.AssemblyCapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import voltaic.api.misc.CapabilityLocationStorage;
import voltaic.api.misc.ILocationStorage;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentInventory;
import voltaic.prefab.utilities.ItemUtils;
import voltaic.prefab.utilities.object.Location;
import voltaic.registers.VoltaicCapabilities;

@EventBusSubscriber(modid = AssemblyLine.ID, bus = Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void attachLocStorageCapabiliity(AttachCapabilitiesEvent<Entity> event) {
	Entity entity = event.getObject();
	if (entity.getCapability(VoltaicCapabilities.CAPABILITY_LOCATIONSTORAGE_ITEM)
		.orElse(AssemblyCapabilityUtils.EMPTY_LOCATION) == AssemblyCapabilityUtils.EMPTY_LOCATION) {
	    event.addCapability(AssemblyLine.rl("mobgrinderdrops"), new CapabilityLocationStorage(1));
	}
    }

    @SubscribeEvent
    public static void captureDroppedItems(LivingDropsEvent event) {
	Entity entity = event.getEntity();
	ILocationStorage storage = entity.getCapability(VoltaicCapabilities.CAPABILITY_LOCATIONSTORAGE_ITEM)
		.orElse(AssemblyCapabilityUtils.EMPTY_LOCATION);
	if (storage == AssemblyCapabilityUtils.EMPTY_LOCATION) {
	    return;
	}
	Level level = entity.level();
	Location location = storage.getLocation(0);
	BlockEntity machine = level.getBlockEntity(new BlockPos(location.intX(), location.intY(), location.intZ()));
	if (machine instanceof TileMobGrinder grinder) {
	    List<ItemStack> droppedItems = new ArrayList<>();
	    event.getDrops().forEach(h -> droppedItems.add(h.getItem()));
	    ComponentInventory inv = grinder.getComponent(IComponentType.Inventory);
	    int max = inv.getOutputStartIndex() + inv.getOutputContents().size();

	    for (ItemStack item : droppedItems) {

		for (int i = inv.getOutputStartIndex(); i < max; i++) {

		    ItemStack contained = inv.getItem(i);

		    int room = contained.getMaxStackSize() - contained.getCount();

		    int amtAccepted = Math.min(room, item.getCount());

		    if (amtAccepted == 0) {
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

		    if (item.isEmpty()) {
			break;
		    }

		}
	    }
	    event.setCanceled(true);
	}

    }

}
