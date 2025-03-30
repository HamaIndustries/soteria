package symbolics.division.soteria.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.soteria.SoterianDamageTypes;
import symbolics.division.soteria.SoterianSounds;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDeathSound()Lnet/minecraft/sound/SoundEvent;")
    )
    public SoundEvent painless(LivingEntity instance, Operation<SoundEvent> original, @Local(argsOnly = true) DamageSource source) {
        if (source.isOf(SoterianDamageTypes.MEMORY)) return SoterianSounds.STAFF_SHOOT;
        else return original.call(instance);
    }
}
