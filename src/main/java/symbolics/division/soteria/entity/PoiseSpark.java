package symbolics.division.soteria.entity;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoiseSpark extends Entity {

    private int ticksLeft = 10;
    private Vec3d target = Vec3d.ZERO;
    public final long seed;

    public PoiseSpark(EntityType<?> type, World world) {
        super(type, world);
        this.seed = this.getRandom().nextLong();
    }

    public void setTarget(Vec3d target) {
        this.target = target;
        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.target);
    }

    public Vec3d getTarget() {
        return target;
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
