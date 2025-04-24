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
import symbolics.division.soteria.SoterianEntities;
import symbolics.division.soteria.entity.PrideSpark;

import java.util.UUID;

public class PrideOfAnshar extends Item {
    public PrideOfAnshar() {
        super(new Item.Settings()
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .attributeModifiers(SwordItem.createAttributeModifiers(
                        ToolMaterials.NETHERITE, 3, -2.4f
                )));
    }

    private static final UUID FLORA = UUID.fromString("62d5f675-f2b1-48a3-b5b6-78127cd1ed2c");
    private static final UUID FAUNA = UUID.fromString("b27495da-4f16-4fc1-9c01-d71cde1b6b77");
    private static final UUID MINERAL = UUID.fromString("f757c36c-51c9-4a77-8d46-90dd6fc77057");

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if ((entity.getUuid().equals(FLORA) || entity.getUuid().equals(FAUNA) || entity.getUuid().equals(MINERAL)) && !user.getEntityWorld().isClient) {
            World world = user.getEntityWorld();
            PrideSpark spark = SoterianEntities.PRIDE_SPARK.create(world);
            world.spawnEntity(spark);
            spark.setTarget(entity);
            spark.refreshPositionAfterTeleport(entity.getEyePos());
        }
        return ActionResult.SUCCESS;
    }
}
