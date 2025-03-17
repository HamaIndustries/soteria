package symbolics.division.soteria;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
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
        Vec3d target = entity.getRotationVec(tickDelta).multiply(30);
        Vec3d pos = Vec3d.ZERO;

        Vec3d sparkDir = target.multiply(-1);
        Vec3d lookDir = MinecraftClient.getInstance().cameraEntity.getRotationVec(tickDelta);
        Vector3f up = sparkDir.crossProduct(lookDir).toVector3f().normalize().mul(0.2f);
        Vector3f along = sparkDir.toVector3f().normalize();


        float segmentLength = 0.3f;
        float overallLength = (float) target.distanceTo(pos);
        float nSegs = Math.max(0, (overallLength - segmentLength) / segmentLength);
        Vector3f from = new Vector3f(entity.getPos().toVector3f());
        Vector3f to = new Vector3f();
        for (int i = 0; i < nSegs; i++) {
            to = along.mul(i * segmentLength, to);//.add(new Vector3f(up).mul(3).rotateAxis(random.nextFloat() * MathHelper.PI * 2, along.x, along.y, along.z));
            drawSegment(mat, vc, from, to, up);
            from = to;
        }
        // we have at least segmentLength left
        drawSegment(mat, vc, from, target.toVector3f(), up);
    }

    private void drawSegment(
            Matrix4f matrix,
            VertexConsumer buffer,
            Vector3fc from,
            Vector3fc to,
            Vector3fc up
    ) {
        buffer.vertex(matrix, from.x() - up.x(), from.y() - up.y(), from.z() - up.z()).color(1, 1, 1, 0.5f);
        buffer.vertex(matrix, from.x() + up.x(), from.y() + up.y(), from.z() + up.z()).color(1, 1, 1, 0.5f);
        buffer.vertex(matrix, to.x() - up.x(), to.y() - up.y(), to.z() - up.z()).color(1, 1, 1, 0.5f);
        buffer.vertex(matrix, to.x() + up.x(), to.y() + up.y(), to.z() + up.z()).color(1, 1, 1, 0.5f);
    }

    @Override
    public Identifier getTexture(PoiseSpark entity) {
        return null;
    }
}
