package symbolics.division.soteria.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.soteria.SoterianDamageTypes;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @WrapWithCondition(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V")
    )
    private boolean notHere(ServerPlayNetworkHandler handler, Packet packet, PacketCallbacks packetCallbacks, @Local(argsOnly = true) DamageSource source) {
        return !source.isOf(SoterianDamageTypes.MEMORY);
    }

    @WrapWithCondition(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V")
    )
    public boolean sendStatus(World instance, Entity entity, byte status, @Local(argsOnly = true) DamageSource source) {
        return !source.isOf(SoterianDamageTypes.MEMORY);
    }
}
