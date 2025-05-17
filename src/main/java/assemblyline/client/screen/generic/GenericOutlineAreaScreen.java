package assemblyline.client.screen.generic;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.common.tile.util.TileOutlineArea;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import voltaic.prefab.inventory.container.types.GenericContainerBlockEntity;
import voltaic.prefab.screen.GenericScreen;

public abstract class GenericOutlineAreaScreen<T extends GenericContainerBlockEntity<? extends TileOutlineArea>> extends GenericScreen<T> {

    public GenericOutlineAreaScreen(T container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
	public void tick() {
        super.tick();
        TileOutlineArea harvester = menu.getSafeHost();
        if (harvester != null && HandlerHarvesterLines.containsLines(harvester.getBlockPos())) {
            HandlerHarvesterLines.removeLines(harvester.getBlockPos());
            updateBox(harvester);
        }
    }

    //convenience method for toggle button
    public void toggleRendering() {
        TileOutlineArea harvester = menu.getSafeHost();
        if (harvester != null) {
            BlockPos pos = harvester.getBlockPos();
            if (HandlerHarvesterLines.containsLines(pos)) {
                HandlerHarvesterLines.removeLines(pos);
            } else {
                updateBox(harvester);
            }
        }
    }

    public void updateBox(TileOutlineArea area) {
        HandlerHarvesterLines.addLines(area.getBlockPos(), area.getAABB(area.width.getValue(), area.length.getValue(), area.height.getValue(), isFlipped()));
    }

    public abstract boolean isFlipped();

}
