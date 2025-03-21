package symbolics.division.soteria;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import symbolics.division.soteria.entity.PoiseSpark;

public class PoiseSparkRenderer extends EntityRenderer<PoiseSpark> {

    protected PoiseSparkRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(PoiseSpark entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getLightning());
        Matrix4f mat = matrices.peek().getPositionMatrix();
        Random random = Random.create(entity.seed);
        Vec3d tgt = entity.getTarget();
        if (tgt == null) return;
        Vector3f pos = Vec3d.ZERO.toVector3f();
        Vector3f target = entity.getTarget().subtract(entity.getPos()).toVector3f();

        Vec3d sparkDir = new Vec3d(target);
        Vec3d lookDir = MinecraftClient.getInstance().cameraEntity.getRotationVec(tickDelta);
        Vector3f up = sparkDir.crossProduct(lookDir).toVector3f().normalize();
        Vector3f width = up.mul(0.05f, new Vector3f());
        Vector3f along = sparkDir.toVector3f().normalize();

        float segmentLength = 0.2f;
        float overallLength = target.length();
        float nSegs = Math.max(0, (overallLength - segmentLength) / segmentLength);
        Vector3f from = new Vector3f();
        Vector3f to = new Vector3f();
        Vector3f around = new Vector3f();

        float colorDiff = 0.1f;
        float c = random.nextFloat();
        int maxAlpha = PoiseSpark.MAX_TICKS;
        int alpha = (entity.ticksLeft() * 255 / maxAlpha) << 24;

        for (int i = 0; i < nSegs; i++) {
            around.set(up.mul(0.1f, around)).rotateAxis(random.nextFloat() * MathHelper.PI * 2, along.x, along.y, along.z);
            to = along.mul(segmentLength * (i + 1), to).add(pos).add(around);
            drawSegment(mat, vc, from, to, width, alpha | hueRotate(c + colorDiff * i), alpha | hueRotate(c + colorDiff * (i + 1)));
            from.set(to);
        }
    }

    private void drawSegment(
            Matrix4f matrix,
            VertexConsumer buffer,
            Vector3fc from,
            Vector3fc to,
            Vector3fc up,
            int c1,
            int c2
    ) {
        buffer.vertex(matrix, from.x() + up.x(), from.y() + up.y(), from.z() + up.z()).color(c1);
        buffer.vertex(matrix, from.x() - up.x(), from.y() - up.y(), from.z() - up.z()).color(c1);
        buffer.vertex(matrix, to.x() - up.x(), to.y() - up.y(), to.z() - up.z()).color(c2);
        buffer.vertex(matrix, to.x() + up.x(), to.y() + up.y(), to.z() + up.z()).color(c2);

        buffer.vertex(matrix, to.x() + up.x(), to.y() + up.y(), to.z() + up.z()).color(c1);
        buffer.vertex(matrix, to.x() - up.x(), to.y() - up.y(), to.z() - up.z()).color(c1);
        buffer.vertex(matrix, from.x() - up.x(), from.y() - up.y(), from.z() - up.z()).color(c2);
        buffer.vertex(matrix, from.x() + up.x(), from.y() + up.y(), from.z() + up.z()).color(c2);
    }

    @Override
    public Identifier getTexture(PoiseSpark entity) {
        return null;
    }

    public static int hueRotate(float phase) {
        float off = MathHelper.PI * 2 / 3;
        int r = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase), 0, 1));
        int g = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase + off), 0, 1));
        int b = (int) (0xff * MathHelper.clamp(MathHelper.sin(phase + off + off), 0, 1));
        return (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean shouldRender(PoiseSpark entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
