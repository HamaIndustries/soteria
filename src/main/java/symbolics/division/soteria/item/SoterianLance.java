package symbolics.division.soteria.item;

import com.mojang.datafixers.util.Function4;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.soteria.SoterianEntities;
import symbolics.division.soteria.entity.PoiseSpark;
import symbolics.division.spirit_vector.logic.ISpiritVectorUser;
import symbolics.division.spirit_vector.logic.vector.SpiritVector;

public class SoterianLance extends Item {
    // evil but ok because only processed on client
    private int charge = 0;

    public static Function4<Entity, LivingEntity, Float, Vec3d, Boolean> hitCallback = (target, player, damage, pos) -> false;

    public SoterianLance() {
        super(new Item.Settings()
                .rarity(Rarity.EPIC)
                .maxDamage(250)
                .attributeModifiers(TridentItem.createAttributeModifiers())
                .component(DataComponentTypes.TOOL, TridentItem.createToolComponent()));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof ISpiritVectorUser svUser) {
            SpiritVector sv = svUser.spiritVector();
            if (sv == null || sv.getMomentum() < 1) return;
            int POISE_PER_TICK = 1;
            sv.modifyMomentum(-POISE_PER_TICK);
            charge += POISE_PER_TICK;
        }
    }

    public static void makeSpark(World world, Entity source, Vec3d target) {
        if (!world.isClient) {
            PoiseSpark spark = SoterianEntities.POISE_SPARK.create(world);
            world.spawnEntity(spark);
            spark.refreshPositionAfterTeleport(source.getEyePos().add(source.getRotationVec(0).rotateY(-0.1f)));
            spark.setTarget(target);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float MAX_DISTANCE = 200f;
        if (user instanceof PlayerEntity player && charge > 0) {
            Vec3d look = player.getRotationVec(0);
            Vec3d end = player.getEyePos().add(look.multiply(MAX_DISTANCE));
            EntityHitResult result = ProjectileUtil.raycast(player, player.getEyePos(), end, player.getBoundingBox().expand(MAX_DISTANCE * 2), Entity::canBeHitByProjectile, MAX_DISTANCE);

            Vec3d from = player.getEyePos().add(look);
            Vec3d to = result != null ? result.getPos() : end;
            Vec3d along = to.subtract(from);
            double len = along.length();
            Vec3d anchor = player.getEyePos().add(look);
            for (float i = 0; i < len; i++) {
                Vec3d p = anchor.add(along.multiply(i / len));
                world.addParticle(ParticleTypes.CRIT, p.x, p.y, p.z, 0, 0, 0);
            }

            float DAMAGE_POISE_RATIO = 1f / 5; // 100 poise (full) = 20 damage
            if (result != null) {
                if (result.getType().equals(HitResult.Type.ENTITY) && world.isClient) {
                    hitCallback.apply(result.getEntity(), user, charge * DAMAGE_POISE_RATIO, result.getPos());
                    charge = 0;
                } else {
                    hitCallback.apply(null, user, charge * DAMAGE_POISE_RATIO, result.getPos());
                }
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        if (world.isClient) {
            charge = 0;
        }
        return TypedActionResult.consume(itemStack);
    }
}
