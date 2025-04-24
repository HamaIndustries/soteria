package symbolics.division.soteria.entity;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import symbolics.division.soteria.SoterianAttachments;
import symbolics.division.soteria.SoterianSounds;
import symbolics.division.soteria.mixin.FireworkRocketEntityAccessor;

import java.util.List;

public class PrideSpark extends Entity {
    private static final int TICKS_TO_FIRE = 20 * 10;
    public LivingEntity target;
    private int ticksLeft = TICKS_TO_FIRE;
    private boolean fired = false;

    public PrideSpark(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setTarget(LivingEntity target) {
        this.setAttached(SoterianAttachments.PRIDE_TARGET, target.getId());
        this.target = target;
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null && this.hasAttached(SoterianAttachments.PRIDE_TARGET)) {
            int id = this.getAttached(SoterianAttachments.PRIDE_TARGET);
            Entity e = this.getWorld().getEntityById(id);
            if (e instanceof LivingEntity living) {
                this.target = living;
            } else {
                this.setRemoved(RemovalReason.DISCARDED);
            }
        }

        if (target == null || target.isRemoved() || target.isDead() || target.getHealth() <= 0) {
            this.setRemoved(RemovalReason.DISCARDED);
        } else if (this.getWorld() != null && !this.getWorld().isClient) {
            this.setPosition(this.target.getEyePos());
            if (this.age % 4 == 0) {
                this.playSound(SoterianSounds.ROAR, 0.1f, 0.5f);
            }
            ticksLeft--;

            if (ticksLeft <= 0 & !fired) {
                fired = true;

                var explosion = new FireworksComponent(0, List.of(new FireworkExplosionComponent(
                        FireworkExplosionComponent.Type.LARGE_BALL,
                        IntList.of(0xffffff),
                        IntList.of(0xffffff),
                        false,
                        false
                )));

                ItemStack stack = Items.FIREWORK_ROCKET.getDefaultStack();
                stack.set(DataComponentTypes.FIREWORKS, explosion);
                FireworkRocketEntity rocket = new FireworkRocketEntity(
                        this.getWorld(), this, this.getX(), this.getY(), this.getZ(), stack
                );
                this.getWorld().spawnEntity(rocket);
                ((FireworkRocketEntityAccessor) rocket).invokeExplodeAndRemove();

                if (!this.getWorld().isClient) {
                    target.damage(this.getWorld().getDamageSources().magic(), 696969);
                }

                this.setRemoved(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
