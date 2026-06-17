package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import assemblyline.client.event.levelstage.HandlerFarmerLines;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.common.tile.TileFarmer;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import voltaic.api.electricity.formatting.ChatFormatter;
import voltaic.api.electricity.formatting.DisplayUnits;
import voltaic.prefab.screen.GenericScreen;
import voltaic.prefab.screen.component.button.ScreenComponentButton;
import voltaic.prefab.screen.component.types.ScreenComponentCountdown;
import voltaic.prefab.screen.component.types.ScreenComponentSlot;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.utilities.math.Color;

public class ScreenFarmer extends GenericScreen<ContainerFarmer> {

    public static final Color[] COLORS = {
//
	    new Color(50, 50, 50, 255),
	    //
	    new Color(255, 0, 0, 255),
	    //
	    new Color(120, 0, 255, 255),
	    //
	    new Color(0, 240, 0, 255),
	    //
	    new Color(220, 0, 255, 255),
	    //
	    new Color(255, 120, 0, 255),
	    //
	    new Color(0, 0, 255, 255),
	    //
	    new Color(240, 255, 0, 255),
	    //
	    new Color(0, 240, 255, 255)
//
    };

    public ScreenComponentButton<?> renderArea;
    public ScreenComponentButton<?> fullBonemeal;
    public ScreenComponentButton<?> refillEmpty;

    public ScreenFarmer(ContainerFarmer container, Inventory inv, Component titleIn) {
	super(container, inv, titleIn);

	imageHeight += 58;
	inventoryLabelY += 58;

	addComponent(new ScreenComponentCountdown(() -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer != null) {
		return 1 - (float) farmer.ticksSinceCheck.getValue()
			/ Math.max(farmer.currentWaitTime.getValue(), 1.0F);
	    }
	    return 0.0;
	}, 10, 50 + 58));

	addComponent(new ScreenComponentElectricInfo(this::getElectricInformation,
		-AbstractScreenComponentInfo.SIZE + 1, 2));

	addComponent(fullBonemeal = new ScreenComponentButton<>(10, 20, 60, 20).setLabel(() -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer == null) {
		return Component.empty();
	    }
	    return farmer.fullGrowBonemeal.getValue() ? AssemblyTextUtils.gui("regbonemeal")
		    : AssemblyTextUtils.gui("fullbonemeal");
	}).setOnPress(button -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer == null) {
		return;
	    }
	    farmer.fullGrowBonemeal.setValue(!farmer.fullGrowBonemeal.getValue());
	}));

	addComponent(refillEmpty = new ScreenComponentButton<>(10, 50, 60, 20).setLabel(() -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer == null) {
		return Component.empty();
	    }
	    return farmer.refillEmpty.getValue() ? AssemblyTextUtils.gui("ignoreempty")
		    : AssemblyTextUtils.gui("refillempty");
	}).setOnPress(button -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer == null) {
		return;
	    }
	    farmer.refillEmpty.setValue(!farmer.refillEmpty.getValue());
	}));

	addComponent(renderArea = new ScreenComponentButton<>(10, 80, 60, 20).setLabel(() -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer == null) {
		return Component.empty();
	    }
	    return HandlerFarmerLines.isBeingRendered(farmer.getBlockPos()) ? AssemblyTextUtils.gui("hidearea")
		    : AssemblyTextUtils.gui("renderarea");
	}).setOnPress(button -> {
	    TileFarmer farmer = menu.getSafeHost();
	    if (farmer != null) {
		BlockPos pos = farmer.getBlockPos();
		if (HandlerFarmerLines.isBeingRendered(pos)) {
		    HandlerFarmerLines.remove(pos);
		} else {
		    updateBox(farmer);
		}
	    }
	}));

	new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75,
		82 + 58, 8, 72 + 58, (slot, index) -> {
		    if (index < 9) {
			return COLORS[index];
		    }
		    return Color.WHITE;
		});

    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
	ArrayList<FormattedCharSequence> list = new ArrayList<>();
	TileFarmer farmer = menu.getSafeHost();
	if (farmer != null) {
	    ComponentElectrodynamic electro = farmer.getComponent(IComponentType.Electrodynamic);
	    list.add(AssemblyTextUtils
		    .gui("machine.usage",
			    ChatFormatter
				    .getChatDisplayShort(AssemblyLineConfig.INSTANCE.FARMER_USAGE.getAsDouble()
					    * farmer.powerUsageMultiplier.getValue() * 20, DisplayUnits.WATT)
				    .withStyle(ChatFormatting.GRAY))
		    .withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	    list.add(AssemblyTextUtils
		    .gui("machine.voltage",
			    ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE)
				    .withStyle(ChatFormatting.GRAY))
		    .withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	}
	return list;
    }

    @Override
    protected void containerTick() {
	super.containerTick();
	TileFarmer farmer = menu.getSafeHost();
	if (farmer != null && HandlerFarmerLines.isBeingRendered(farmer.getBlockPos())) {
	    HandlerFarmerLines.remove(farmer.getBlockPos());
	    updateBox(farmer);
	}
    }

    @Override
    protected ScreenComponentSlot createScreenSlot(Slot slot) {
	ScreenComponentSlot component = super.createScreenSlot(slot);
	int index = slot.index;
	if (index < 9) {
	    component.setColor(COLORS[index]);
	}
	return component;
    }

    private static void updateBox(TileFarmer farmer) {
	HandlerFarmerLines.addRenderData(farmer.getBlockPos(), Pair.of(COLORS, farmer.getLines(farmer)));
    }

}
