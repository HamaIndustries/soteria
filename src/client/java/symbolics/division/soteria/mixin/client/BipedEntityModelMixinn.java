package symbolics.division.soteria.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.soteria.SoterianItems;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixinn<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Inject(
            method = {"setAngles"},
            at = {@At("TAIL")}
    )
    public void setAngles(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (entity.getStackInHand(Hand.MAIN_HAND).isOf(SoterianItems.ARMISTICE)) {
            rightArm.roll = -0.25f;
            rightArm.pitch = -0.5f;
            rightArm.yaw = -0.2f;
            leftArm.roll = 1f;
            leftArm.pitch = -0.6f;

            ((BipedEntityModel<T>) (Object) this).body.yaw = 0;
        }
    }
}
