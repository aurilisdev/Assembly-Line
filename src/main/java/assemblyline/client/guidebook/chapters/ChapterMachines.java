package assemblyline.client.guidebook.chapters;

import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterMachines extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, AssemblyLineBlocks.blockFarmer.asItem());

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
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockBlockBreaker.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, AssemblyLineBlocks.blockBlockBreaker.asItem()));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.blockbreaker")).setSeparateStart().setIndentions(1));

		// Block Placer
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockBlockPlacer.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, AssemblyLineBlocks.blockBlockPlacer.asItem()));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.blockplacer")).setSeparateStart().setIndentions(1));

		// Energized Rancher
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockRancher.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, AssemblyLineBlocks.blockRancher.asItem()));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.energizedrancher")).setSeparateStart().setIndentions(1));

		// Mob Grinder
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockMobGrinder.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, AssemblyLineBlocks.blockMobGrinder.asItem()));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.mobgrinder")).setSeparateStart().setIndentions(1));

		// Farmer
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockFarmer.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, AssemblyLineBlocks.blockFarmer.asItem()));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.farmer1")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.machines.farmer2")).setSeparateStart().setIndentions(1));

	}

}
