package symbolics.division.soteria;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import symbolics.division.soteria.entity.PrideSpark;

public class PrideSparkRenderer extends EntityRenderer<PrideSpark> {
    public static boolean renderingBeam = false;

    protected PrideSparkRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(PrideSpark entity) {
        return null;
    }

    @Override
    public void render(PrideSpark entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        if (entity.target == null) return;
        renderingBeam = true;
        matrices.push();
        Vec3d p = entity.target.getClientCameraPosVec(tickDelta);
        matrices.translate(p.getX(), p.getY(), p.getZ());
        matrices.translate(-entity.getX(), -entity.getY(), -entity.getZ());
        int color = hueRotate((float) (entity.age + tickDelta) / 20);
        BeaconBlockEntityRenderer.renderBeam(
                matrices, vertexConsumers, BeaconBlockEntityRenderer.BEAM_TEXTURE, tickDelta, 1, entity.getWorld().getTime(), 0, 400, color, 0.2f, 0.25f
        );
        matrices.pop();
    }

    public static int hueRotate(float phase) {
        float off = MathHelper.PI * 2 / 3;
        int r = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase), 0, 1));
        int g = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase + off), 0, 1));
        int b = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase + off + off), 0, 1));
        return (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean shouldRender(PrideSpark entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
