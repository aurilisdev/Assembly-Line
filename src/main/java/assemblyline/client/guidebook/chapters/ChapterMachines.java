package assemblyline.client.guidebook.chapters;

import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import voltaic.client.guidebook.ScreenGuidebook;
import voltaic.client.guidebook.utils.components.Chapter;
import voltaic.client.guidebook.utils.components.Module;
import voltaic.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import voltaic.client.guidebook.utils.pagedata.text.TextWrapperObject;

public class ChapterMachines extends Chapter {

    private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F,
	    AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.farmer));

    public ChapterMachines(Module module) {
	super(module);
    }

    @Override
    public ItemWrapperObject getLogo() {
	return LOGO;
    }

    @Override
    public MutableComponent getTitle() {
	return AssemblyTextUtils.guidebook("chapter.machines");
    }

    @Override
    public void addData() {

	// Block Breaker
	pageData.add(new TextWrapperObject(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE
		.getValue(SubtypeAssemblyMachine.blockbreaker).getDescription().copy().withStyle(ChatFormatting.BOLD))
		.setCentered());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F,
		AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockbreaker)));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.blockbreaker"))
		.setSeparateStart().setIndentions(1));

	// Block Placer
	pageData.add(new TextWrapperObject(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE
		.getValue(SubtypeAssemblyMachine.blockplacer).getDescription().copy().withStyle(ChatFormatting.BOLD))
		.setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F,
		AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockplacer)));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.blockplacer"))
		.setSeparateStart().setIndentions(1));

	// Energized Rancher
	pageData.add(
		new TextWrapperObject(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.rancher)
			.getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F,
		AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.rancher)));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.energizedrancher"))
		.setSeparateStart().setIndentions(1));

	// Mob Grinder
	pageData.add(new TextWrapperObject(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE
		.getValue(SubtypeAssemblyMachine.mobgrinder).getDescription().copy().withStyle(ChatFormatting.BOLD))
		.setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F,
		AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.mobgrinder)));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.mobgrinder"))
		.setSeparateStart().setIndentions(1));

	// Farmer
	pageData.add(
		new TextWrapperObject(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.farmer)
			.getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F,
		AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.farmer)));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.farmer1")).setSeparateStart()
		.setIndentions(1));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.farmer2")).setSeparateStart()
		.setIndentions(1));

    }

}
