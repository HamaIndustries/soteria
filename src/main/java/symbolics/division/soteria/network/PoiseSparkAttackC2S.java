package symbolics.division.soteria.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import symbolics.division.soteria.Soteria;
import symbolics.division.soteria.item.SoterianLance;

public record PoiseSparkAttackC2S(float damage, int target, Vec3d pos) implements CustomPayload {
    public static final CustomPayload.Id<PoiseSparkAttackC2S> ID = Soteria.payloadId("poise_spark_attack");
    public static final PacketCodec<PacketByteBuf, PoiseSparkAttackC2S> CODEC =
            CustomPayload.codecOf(
                    (p, b) -> b.writeFloat(p.damage).writeInt(p.target).writeVec3d(p.pos),
                    b -> new PoiseSparkAttackC2S(b.readFloat(), b.readInt(), b.readVec3d())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void HANDLER(PoiseSparkAttackC2S payload, ServerPlayNetworking.Context context) {
        SoterianLance.makeSpark(context.player().getWorld(), context.player(), payload.pos);
        Entity target = context.player().getWorld().getEntityById(payload.target);
        if (target != null && target.isAttackable()) {
            target.damage(context.player().getWorld().getDamageSources().playerAttack(context.player()), payload.damage);
        }
    }
}
