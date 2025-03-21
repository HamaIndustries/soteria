package symbolics.division.soteria.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static symbolics.division.soteria.SoterianAttachments.POISE_TARGET;

public class PoiseSpark extends Entity {

    public static int MAX_TICKS = 25;
    private int ticksLeft = MAX_TICKS;
    private Vec3d target = Vec3d.ZERO;
    public final long seed;

    public PoiseSpark(EntityType<?> type, World world) {
        super(type, world);
        this.seed = this.getRandom().nextLong();
    }

    public void setTarget(Vec3d target) {
        this.setAttached(POISE_TARGET, target.toVector3f());
//        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target);
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
}
