package symbolics.division.soteria.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.soteria.SLogic;
import symbolics.division.soteria.SoterianEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static symbolics.division.soteria.SoterianAttachments.POISE_TARGET;

public class PoiseSpark extends ProjectileEntity {

    public static final int MAX_TICKS = 25;
    public static final float MAX_DISTANCE = 200f;

    private static final List<Predicate<Entity>> attackPredicates = new ArrayList<>();

    public static void registerPredicate(Predicate<Entity> predicate) {
        attackPredicates.add(predicate);
    }

    static {
        registerPredicate(Entity::canBeHitByProjectile);
    }

    @Nullable
    public static EntityHitResult fire(Entity source, Vec3d start, Vec3d end) {
        return ProjectileUtil.raycast(
                source,
                start,
                end,
                new Box(start, end),
                e -> SLogic.any(attackPredicates, e),
                MAX_DISTANCE * MAX_DISTANCE
        );
    }

    public static void create(World world, Entity source, @Nullable Entity target, Vec3d startPoint, Vec3d endPoint, float damage) {
        PoiseSpark spark = SoterianEntities.POISE_SPARK.create(world);
        world.spawnEntity(spark);
        spark.refreshPositionAfterTeleport(startPoint);
        spark.setTarget(endPoint);
        spark.setOwner(source);
        spark.setVelocity(endPoint.subtract(source.getEyePos()).normalize().multiply(9999999));
        spark.damage = damage;
        spark.initialPos = startPoint;
        if (target != null) {
            spark.hitOrDeflect(new EntityHitResult(target));
        }
    }

    private int ticksLeft = MAX_TICKS;
    private float damage;
    public final long seed;
    protected Vec3d initialPos;


    public PoiseSpark(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.seed = this.getRandom().nextLong();
        this.setNoGravity(true);
    }

    public void setTarget(Vec3d target) {
        this.setAttached(POISE_TARGET, target.toVector3f());
    }

    @Nullable
    public Vec3d getTarget() {
        var r = this.getAttached(POISE_TARGET);
        return r == null ? null : new Vec3d(r);
    }

    @Override
    public void tick() {
        super.tick();
        ticksLeft--;
        if (ticksLeft < 0) {
            this.remove(RemovalReason.DISCARDED);
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

    public int ticksLeft() {
        return ticksLeft;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    protected void onDeflected(@Nullable Entity deflector, boolean fromAttack) {
        if (!this.getWorld().isClient) {
            // now faces a new direction
            Vec3d start = Objects.requireNonNullElseGet(this.getTarget(), this::getEyePos);
            Vec3d end = start.add(this.getVelocity().normalize().multiply(MAX_DISTANCE));
            EntityHitResult hit = fire(deflector, start, end);
            Entity owner = getOwner();
            if (hit != null) {
                create(getWorld(), owner == null ? this : owner, hit.getEntity(), start, hit.getPos(), this.damage + 2);
            } else {
                create(getWorld(), owner == null ? this : owner, null, start, end, this.damage + 2);
            }
            this.setPosition(this.initialPos); // marksman sets position, must reset
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult ehr && this.getOwner() instanceof PlayerEntity player) {
            ehr.getEntity().damage(this.getWorld().getDamageSources().playerAttack(player), damage);
        }
    }
}
