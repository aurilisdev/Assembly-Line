package assemblyline.common.event;

import java.util.ArrayList;
import java.util.List;

import assemblyline.References;
import assemblyline.common.tile.TileMobGrinder;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.capability.types.locationstorage.CapabilityLocationStorage;
import electrodynamics.api.capability.types.locationstorage.ILocationStorage;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.object.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = References.ID, bus = Bus.FORGE)
public class EventHandler {

	@SubscribeEvent
	public static void attachLocStorageCapabiliity(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(new ResourceLocation(References.ID, ElectrodynamicsCapabilities.LOCATION_KEY), new CapabilityLocationStorage(1));
	}

	@SubscribeEvent
	public static void captureDroppedItems(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		LazyOptional<ILocationStorage> lazyOptional = entity.getCapability(ElectrodynamicsCapabilities.LOCATION_STORAGE_CAPABILITY);
		if (lazyOptional.isPresent()) {
			Level level = entity.getLevel();
			ILocationStorage storage = lazyOptional.resolve().get();
			Location location = storage.getLocation(0);
			BlockEntity machine = level.getBlockEntity(new BlockPos(location.intX(), location.intY(), location.intZ()));
			if (machine != null && machine instanceof TileMobGrinder grinder) {
				List<ItemStack> droppedItems = new ArrayList<>();
				event.getDrops().forEach(h -> droppedItems.add(h.getItem()));
				ComponentInventory inv = grinder.getComponent(ComponentType.Inventory);
				ComponentInventory.addItemsToInventory(inv, droppedItems, inv.getOutputStartIndex(), inv.getOutputContents().size());
				event.setCanceled(true);
			}
		}

	}

}
