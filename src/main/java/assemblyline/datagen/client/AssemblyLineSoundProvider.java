package assemblyline.datagen.client;

import assemblyline.AssemblyLine;
import assemblyline.registers.AssemblyLineSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voltaic.datagen.utils.client.BaseSoundProvider;

public class AssemblyLineSoundProvider extends BaseSoundProvider {
    public AssemblyLineSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, AssemblyLine.ID);
    }

    @Override
    public void registerSounds() {
        add(AssemblyLineSounds.SOUND_BLOCKBREAKER);
    }
}
