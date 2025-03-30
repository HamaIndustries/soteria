package symbolics.division.soteria.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import symbolics.division.soteria.Mind;
import symbolics.division.soteria.SoterianDamageTypes;
import symbolics.division.soteria.api.Unliving;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixinn implements Unliving {
    @Shadow
    public abstract float getHealth();

    @Unique
    private int FADE_TIME = 20 * 30;

    @Unique
    private int memories = FADE_TIME;

    @Unique
    private boolean ephemeral = false;

    @Override
    public void soteria$materialize() {
        ephemeral = false;
    }

    @Override
    public boolean soteria$ephemeral() {
        return ephemeral;
    }

    @Inject(
            method = "onDamaged",
            at = @At("HEAD")
    )
    public void onDamaged(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.isOf(SoterianDamageTypes.MEMORY) && (LivingEntity) (Object) this instanceof ClientPlayerEntity) {
            ephemeral = true;
            ((LivingEntity) (Object) this).setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    @Inject(
            method = "isDead",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preventDeath(CallbackInfoReturnable<Boolean> cir) {
        if (this.soteria$unliving()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }


    @Unique
    private boolean fading = false;
    @Unique
    private boolean checked = false;
    @Unique
    private boolean anatman = false;

    @Override
    public boolean soteria$unliving() {
        if (!checked) {
            anatman = (Object) this instanceof ClientPlayerEntity;
            checked = true;
        }

        return anatman && ephemeral && fading && memories > 0;
    }

    @Override
    public void soteria$disperse() {
        fading = true;
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void tick(CallbackInfo ci) {
        if (fading && getHealth() <= 0) {
            memories--;
            if (memories <= 0) Mind.memoir = true;
        }
    }

    @WrapOperation(
            method = "onDamaged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getHurtSound(Lnet/minecraft/entity/damage/DamageSource;)Lnet/minecraft/sound/SoundEvent;")
    )
    public SoundEvent soundless(LivingEntity instance, DamageSource source, Operation<SoundEvent> original) {
        if (source.isOf(SoterianDamageTypes.MEMORY)) return null;
        else return original.call(instance, source);
    }

    @Override
    public float soteria$residue() {
        return Math.max(0, (float) memories / FADE_TIME);
    }
}
