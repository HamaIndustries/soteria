package symbolics.division.soteria.item;

import com.mojang.datafixers.util.Function4;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.soteria.SoterianItems;
import symbolics.division.soteria.SoterianSounds;
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
        if (SpiritVector.hasEquipped(user) && /*remainingUseTicks % 2 == 0 &&*/ user instanceof PlayerEntity player) {
            int TICKS_TO_MAX = 100;
            int useticks = 72000 - remainingUseTicks;
            float pitch = Math.clamp((float) useticks / TICKS_TO_MAX + 0.5f, 0.5f, 1f);
            world.playSoundFromEntity(player, SoterianSounds.STAFF_CHARGE_COMBINED, SoundCategory.PLAYERS, 0.2f, pitch);
        }
        if (user instanceof ISpiritVectorUser svUser) {
            SpiritVector sv = svUser.spiritVector();
            if (sv == null || sv.getMomentum() < 1) return;
            int POISE_PER_TICK = 1;
            sv.modifyMomentum(-POISE_PER_TICK);
            if (charge < SpiritVector.MAX_MOMENTUM) {
                charge += POISE_PER_TICK;
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float MAX_DISTANCE = 200f;
        if (user instanceof PlayerEntity player && charge > 0) {
            world.playSoundFromEntity(user, SoterianSounds.STAFF_SHOOT, SoundCategory.PLAYERS, 0.9f, 1f);

            player.getItemCooldownManager().set(SoterianItems.SOTERIAN_LANCE, 20);

            if (!world.isClient) return;
            Vec3d look = player.getRotationVecClient();
            Vec3d end = player.getEyePos().add(look.multiply(MAX_DISTANCE));
            EntityHitResult result = PoiseSpark.fire(player, player.getEyePos(), end);

            float DAMAGE_POISE_RATIO = 1f / 5; // 100 poise (full) = 20 damage
            if (result != null) {
                if (result.getType().equals(HitResult.Type.ENTITY)) {
                    hitCallback.apply(result.getEntity(), user, charge * DAMAGE_POISE_RATIO, result.getPos());
                    charge = 0;
                } else {
                    hitCallback.apply(null, user, charge * DAMAGE_POISE_RATIO, result.getPos());
                }
            } else {
                hitCallback.apply(null, user, charge * DAMAGE_POISE_RATIO, user.getRotationVecClient().multiply(MAX_DISTANCE).add(user.getPos()));
                HitResult rocketJump = player.raycast(player.getBlockInteractionRange(), 0, false);
                if (rocketJump instanceof BlockHitResult hit) {
                    Vec3d dir = player.getEyePos().subtract(hit.getPos());
                    double strength = (float) charge / SpiritVector.MAX_MOMENTUM * Math.max(0, 4 - dir.length());
                    player.addVelocity(dir.normalize().multiply(strength));
                }
            }


        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return TypedActionResult.pass(itemStack);
        user.setCurrentHand(hand);
        if (world.isClient) {
            charge = 0;
        }
        return TypedActionResult.consume(itemStack);
    }
}
