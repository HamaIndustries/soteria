package symbolics.division.soteria.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.soteria.SoterianDamageTypes;
import symbolics.division.soteria.SoterianItems;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {
    @ModifyReturnValue(
            method = "playerAttack",
            at = @At("RETURN")
    )
    public DamageSource replaceDamageType(DamageSource original) {
        ItemStack stack = original.getWeaponStack();
        if (stack != null && stack.isOf(SoterianItems.ARMISTICE)) {
            return SoterianDamageTypes.of(original.getSource().getWorld(), SoterianDamageTypes.MEMORY);
        }
        return original;
    }
}
