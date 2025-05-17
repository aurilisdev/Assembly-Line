package assemblyline.registers;

import assemblyline.AssemblyLine;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AssemblyLine.ID);

    public static final RegistryObject<SoundEvent> SOUND_BLOCKBREAKER = sound("blockbreaker");

    private static RegistryObject<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(AssemblyLine.rl(name)));
    }

}
