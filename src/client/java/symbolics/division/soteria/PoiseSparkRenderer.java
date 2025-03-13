package symbolics.division.soteria;

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
        Vec3d target = entity.getPos().subtract(entity.getTarget());
        Vec3d pos = Vec3d.ZERO;

        float segmentLength = 0.3f;
        float overallLength = (float) target.distanceTo(pos);
        float nSegs = Math.max(0, (overallLength - segmentLength) / segmentLength);
        for (int i = 0; i < nSegs; i++) {

        }
        // we have at least segmentLength left
    }

    private void drawSegment(
            Matrix4f matrix,
            VertexConsumer buffer,
            Vector3fc from,
            Vector3fc to,
            Vector3fc left
    ) {
        float t = 0.2f;
        // ccw
        buffer.vertex(matrix, to.x(), to.y() - t, to.z()).color(1, 1, 1, 0.5f);
    }

    @Override
    public Identifier getTexture(PoiseSpark entity) {
        return null;
    }
}
