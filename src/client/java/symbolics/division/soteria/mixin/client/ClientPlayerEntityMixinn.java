package symbolics.division.soteria.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixinn extends LivingEntity {
    protected ClientPlayerEntityMixinn(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "updateHealth",
            at = @At("HEAD")
    )
    public void materialize(float health, CallbackInfo ci) {
        if (health > 0) this.soteria$materialize();
        else this.soteria$disperse();
    }
}
