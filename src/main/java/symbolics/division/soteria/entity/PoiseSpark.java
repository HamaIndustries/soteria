package symbolics.division.soteria.entity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import symbolics.division.soteria.Soteria;

public class PoiseSpark extends Entity {

    public static AttachmentType<Vector3f> POISE_TARGET = AttachmentRegistry.create(
            Soteria.id("spark_target"), builder -> builder.syncWith(PacketCodecs.VECTOR3F, (attachmentTarget, serverPlayerEntity) -> true)
    );

    private int ticksLeft = 5;
    private Vec3d target = Vec3d.ZERO;
    public final long seed;

    public PoiseSpark(EntityType<?> type, World world) {
        super(type, world);
        this.seed = this.getRandom().nextLong();
    }

    public void setTarget(Vec3d target) {
        this.setAttached(POISE_TARGET, target.toVector3f());
        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.target);
    }

    @Nullable
    public Vec3d getTarget() {
        return new Vec3d(this.getAttachedOrCreate(POISE_TARGET, Vector3f::new));
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
}
