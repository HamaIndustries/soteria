package symbolics.division.soteria;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoterianSounds {
    public static SoundEvent STAFF_CHARGE_START = of("staff.charge.start");
    public static SoundEvent STAFF_CHARGE_COMBINED = of("staff.charge.combined");
    public static SoundEvent STAFF_CHARGE_END = of("staff.charge.end");
    public static SoundEvent STAFF_SHOOT = of("staff.shoot");

    public static SoundEvent MURMUR = of("murmur");

    private static SoundEvent of(String id) {
        Identifier ident = Soteria.id(id);
        return Registry.register(Registries.SOUND_EVENT, ident, SoundEvent.of(ident));
    }

    public static void init() {
    }
}
