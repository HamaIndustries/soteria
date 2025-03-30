package symbolics.division.soteria;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import symbolics.division.soteria.api.Unliving;

import java.io.IOException;

public class Mind {
    public static boolean memoir = false;

    private static final Identifier EIGENGRAU_NODE_ACCESS = Soteria.id("shaders/post/eigengrau.json");
    private static PostEffectProcessor eigengrau;

    public static void perceive(float delta) {
        if (eigengrau == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getCameraEntity() instanceof Unliving u && u.soteria$unliving()) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            float r = u.soteria$residue();
            eigengrau.setUniforms("residue", r);
            eigengrau.render(delta);
        }
    }

    public static void init(ResourceFactory factory) {
        if (eigengrau != null) eigengrau.close();
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            eigengrau = new PostEffectProcessor(client.getTextureManager(), factory, client.getFramebuffer(), EIGENGRAU_NODE_ACCESS);
            eigengrau.setupDimensions(client.getWindow().getWidth(), client.getWindow().getHeight());
        } catch (IOException ioexception) {
            Soteria.LOGGER.warn("Failed to load shader: {}", EIGENGRAU_NODE_ACCESS, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            Soteria.LOGGER.warn("Failed to parse shader: {}", EIGENGRAU_NODE_ACCESS, jsonsyntaxexception);
        }
    }

    public static void resize(int width, int height) {
        if (eigengrau != null) eigengrau.setupDimensions(width, height);
    }

    public static void close() {
        if (eigengrau != null) eigengrau.close();
    }
}
