package symbolics.division.soteria.mixin.client;

import net.minecraft.client.sound.MusicTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.soteria.Murmur;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void whispering(CallbackInfo ci) {
        if (Murmur.cancelMusic()) {
            ci.cancel();
        }
    }
}
