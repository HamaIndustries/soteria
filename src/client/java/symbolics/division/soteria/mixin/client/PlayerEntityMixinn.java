package symbolics.division.soteria.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import symbolics.division.soteria.SoterianDamageTypes;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixinn extends LivingEntity {
    protected PlayerEntityMixinn(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapWithCondition(
            method = "attack",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addEnchantedHitParticles(Lnet/minecraft/entity/Entity;)V")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V")
    )
    public boolean merciful(World instance, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, @Local(ordinal = 0) DamageSource damageSource) {
        return !damageSource.isOf(SoterianDamageTypes.MEMORY);
    }
}
