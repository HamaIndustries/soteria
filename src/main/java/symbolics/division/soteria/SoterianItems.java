package symbolics.division.soteria;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import symbolics.division.soteria.item.Armistice;
import symbolics.division.soteria.item.PrideOfAnshar;
import symbolics.division.soteria.item.SoterianLance;
import symbolics.division.spirit_vector.SpiritVectorMod;

public class SoterianItems {
    public static final Item SOTERIAN_LANCE = Registry.register(Registries.ITEM, Soteria.id("soterian_lance"), new SoterianLance());
    public static final Item ARMISTICE = Registry.register(Registries.ITEM, Soteria.id("armistice"), new Armistice());
    public static final Item PRIDE_OF_ANSHAR = Registry.register(Registries.ITEM, Soteria.id("pride_of_anshar"), new PrideOfAnshar());

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, SpiritVectorMod.id("item_group")))
                .register(content -> content.add(SOTERIAN_LANCE.getDefaultStack()));
    }
}
