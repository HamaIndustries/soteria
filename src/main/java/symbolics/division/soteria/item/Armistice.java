package symbolics.division.soteria.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.UUID;

public class Armistice extends Item {
    private static final UUID OWNER = UUID.fromString("62d5f675-f2b1-48a3-b5b6-78127cd1ed2c");

    public Armistice() {
        super(new Item.Settings()
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .attributeModifiers(SwordItem.createAttributeModifiers(
                        ToolMaterials.DIAMOND, 696969, 1
                )));
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!entity.getUuid().equals(OWNER) && entity instanceof PlayerEntity e) {
//            e.getInventory().setStack(slot, ItemStack.EMPTY);
//            boolean a = DamageTypeTags.BYPASSES_INVULNERABILITY world.getDamageSources().registry.get
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
