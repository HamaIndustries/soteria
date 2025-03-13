package symbolics.division.soteria.item;

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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoterianLance extends Item {

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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float MAX_DISTANCE = 100f;
        if (user instanceof PlayerEntity player) {
            Vec3d look = player.getRotationVec(0);
            System.out.println("look: " + look);
            System.out.println("p: " + player.getEyePos());
            HitResult result = ProjectileUtil.raycast(player, player.getEyePos(), player.getEyePos().add(look.multiply(MAX_DISTANCE)), player.getBoundingBox().expand(MAX_DISTANCE), Entity::canBeHitByProjectile, MAX_DISTANCE);
            System.out.println("r: " + result);
            if (result != null) {
                if (result.getType().equals(HitResult.Type.ENTITY) && !world.isClient) {
                    // damage
                }
                Vec3d from = player.getEyePos().add(look);
                Vec3d to = result.getPos();
                Vec3d along = to.subtract(from);
                double len = along.length();
                Vec3d anchor = player.getEyePos().add(look);
                for (float i = 0; i < len; i++) {
                    Vec3d p = anchor.add(along.multiply(i / len));
                    world.addParticle(ParticleTypes.CRIT, p.x, p.y, p.z, 0, 0, 0);
                }
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
}
