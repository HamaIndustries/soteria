package symbolics.division.soteria;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import symbolics.division.soteria.item.SoterianLance;

public class SoterianItems {
    public static void init() {
    }

    public static final Item SOTERIAN_LANCE = Registry.register(Registries.ITEM, Soteria.id("soterian_lance"), new SoterianLance());
}
