package symbolics.division.soteria.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.soteria.api.Unliving;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(
            method = "onDeathMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventDeath(DeathMessageS2CPacket packet, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player instanceof Unliving e && e.soteria$unliving()) {
            ci.cancel();
        }
    }
}
