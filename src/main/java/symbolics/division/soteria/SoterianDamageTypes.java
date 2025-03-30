package symbolics.division.soteria;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class SoterianDamageTypes {
    public static final RegistryKey<DamageType> MEMORY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Soteria.id("memory"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static void init() {
    }
}
