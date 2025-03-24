package symbolics.division.soteria.compat;

import archives.tater.marksman.Marksman;
import archives.tater.marksman.Ricoshottable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import symbolics.division.soteria.entity.PoiseSpark;

public class MarksmanCompat implements ModCompatibility {
    @Override
    public void initialize(String modid, boolean inDev) {
        PoiseSpark.registerPredicate(Ricoshottable::canBeRicoshotted);
        PoiseSpark.registerDeflectionCallback(MarksmanCompat::handleDeflection);
    }

    private static boolean handleDeflection(PoiseSpark.DeflectionContext ctx) {
        if (ctx.deflection() != Marksman.AIM_AT_TARGET) return false;
        if (ctx.deflector() instanceof Ricoshottable ricoshottable) {
            ricoshottable.marksman$setRicoshotted();
        }

        Entity owner = ctx.spark().getOwner() instanceof PlayerEntity player
                ? player
                : ctx.deflector() instanceof ItemEntity itemEntity && itemEntity.getOwner() != null
                ? itemEntity.getOwner()
                : ctx.spark().getOwner();

        Entity target = owner != null && ctx.deflector() != null ?
                Marksman.getTarget(ctx.spark().getWorld(), owner, ctx.deflector(), ctx.spark().getRandom())
                : null;

        if (target != null) {
            PoiseSpark.create(
                    ctx.spark().getWorld(),
                    ctx.owner(),
                    target,
                    ctx.spark().getTarget(),
                    target.getEyePos(),
                    ctx.spark().getDamage() + 2
            );
        }
        return true;
    }
}
