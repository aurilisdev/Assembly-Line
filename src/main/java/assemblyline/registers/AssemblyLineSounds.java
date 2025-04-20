package assemblyline.registers;

import assemblyline.AssemblyLine;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AssemblyLineSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, AssemblyLine.ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_BLOCKBREAKER = sound("blockbreaker");

    private static DeferredHolder<SoundEvent, SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(AssemblyLine.rl(name), 16.0F));
    }

}
