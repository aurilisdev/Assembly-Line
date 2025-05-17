package assemblyline.datagen.client;

import assemblyline.AssemblyLine;
import assemblyline.registers.AssemblyLineSounds;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import voltaic.datagen.utils.client.BaseSoundProvider;

public class AssemblyLineSoundProvider extends BaseSoundProvider {
	
    public AssemblyLineSoundProvider(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, helper, AssemblyLine.ID);
    }

    @Override
    public void registerSounds() {
        add(AssemblyLineSounds.SOUND_BLOCKBREAKER);
    }
}
