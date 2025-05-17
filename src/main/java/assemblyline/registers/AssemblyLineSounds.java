package assemblyline.registers;

import assemblyline.AssemblyLine;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyLineSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AssemblyLine.ID);

    public static final RegistryObject<SoundEvent> SOUND_BLOCKBREAKER = sound("blockbreaker");

    private static RegistryObject<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(AssemblyLine.rl(name), 16.0F));
    }

}
