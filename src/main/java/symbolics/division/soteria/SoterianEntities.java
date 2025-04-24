package symbolics.division.soteria;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import symbolics.division.soteria.entity.PoiseSpark;
import symbolics.division.soteria.entity.PrideSpark;

public class SoterianEntities {

    public static final EntityType<PoiseSpark> POISE_SPARK = Registry.register(
            Registries.ENTITY_TYPE,
            Soteria.id("poise_spark"),
            EntityType.Builder.create(PoiseSpark::new, SpawnGroup.MISC)
                    .dimensions(0.05f, 0.05f)
                    .build("poise_spark")
    );

    public static final EntityType<PrideSpark> PRIDE_SPARK = Registry.register(
            Registries.ENTITY_TYPE,
            Soteria.id("pride_spark"),
            EntityType.Builder.create(PrideSpark::new, SpawnGroup.MISC)
                    .dimensions(0.05f, 0.05f)
                    .build("pride_spark")
    );

    public static void init() {
    }
}
