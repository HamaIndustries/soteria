package symbolics.division.soteria.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import symbolics.division.soteria.SoterianDamageTypes;

import java.util.UUID;

public class Armistice extends Item {
    private static final UUID OWNER = UUID.fromString("62d5f675-f2b1-48a3-b5b6-78127cd1ed2c");

    public Armistice() {
        super(new Item.Settings()
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .attributeModifiers(SwordItem.createAttributeModifiers(
                        ToolMaterials.NETHERITE, 3, -2.4f
                )));
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!entity.getUuid().equals(OWNER) && entity instanceof PlayerEntity e) {
            e.getInventory().setStack(slot, ItemStack.EMPTY);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        entity.damage(SoterianDamageTypes.of(user.getEntityWorld(), SoterianDamageTypes.MEMORY), 696969);
        return ActionResult.SUCCESS;
    }
}
