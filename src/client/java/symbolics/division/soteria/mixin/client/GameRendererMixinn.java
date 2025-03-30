package symbolics.division.soteria.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.soteria.Mind;
import symbolics.division.soteria.api.Unliving;

@Mixin(GameRenderer.class)
public class GameRendererMixinn {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void render(RenderTickCounter tickCounter, CallbackInfo ci) {
        Mind.perceive(tickCounter.getLastFrameDuration());
    }

    @Inject(
            method = "loadPrograms",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;loadBlurPostProcessor(Lnet/minecraft/resource/ResourceFactory;)V", shift = At.Shift.AFTER)
    )
    private void loadEigengrauProcessor(ResourceFactory factory, CallbackInfo ci) {
        Mind.init(factory);
    }

    @Inject(
            method = "onResized",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;onResized(II)V")
    )
    public void resize(int width, int height, CallbackInfo ci) {
        Mind.resize(width, height);
    }

    @Inject(
            method = "close",
            at = @At("RETURN")
    )
    public void close(CallbackInfo ci) {
        Mind.close();
    }

    @WrapOperation(
            method = "render",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z")
    )
    private boolean camel(GameOptions instance, Operation<Boolean> original) {
        return original.call(instance) || (MinecraftClient.getInstance().getCameraEntity() instanceof Unliving u && u.soteria$residue() < 0.5);
    }

    @WrapWithCondition(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    )
    private boolean needleEye(InGameHud instance, DrawContext context, RenderTickCounter tickCounter, @Local(ordinal = 0) DrawContext ctx) {
        if (Mind.memoir)
            ctx.fill(0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), 0xff000000);
        return !(MinecraftClient.getInstance().getCameraEntity() instanceof Unliving u && u.soteria$residue() < 0.5);
    }
}
