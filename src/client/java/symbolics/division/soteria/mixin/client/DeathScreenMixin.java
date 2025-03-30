package symbolics.division.soteria.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.soteria.Mind;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    @Shadow
    @Final
    private Text message;

    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Unique
    Text eulogy = null;

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V")
    )
    public void memoir(DrawContext instance, TextRenderer textRenderer, Text text, int centerX, int y, int color, Operation<Void> original) {
        if (eulogy == null)
            eulogy = Text.translatable("deathScreen.title.soteria.memoir" + this.textRenderer.random.nextInt(5));
        if (text == ((DeathScreen) (Object) (this)).getTitle() && Mind.memoir) {
            original.call(instance, textRenderer, eulogy, centerX, y, color);
        } else {
            original.call(instance, textRenderer, text, centerX, y, color);
        }
    }

    @Inject(
            method = "fillBackgroundGradient",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void fillBackgroundGradient(DrawContext context, int width, int height, CallbackInfo ci) {
        if (Mind.memoir) {
            context.fill(0, 0, width, height, 0xff000000);
            ci.cancel();
        }
    }
}
