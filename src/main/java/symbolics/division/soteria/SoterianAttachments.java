package symbolics.division.soteria;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;
import org.joml.Vector3f;

public class SoterianAttachments {
    public static AttachmentType<Vector3f> POISE_TARGET = AttachmentRegistry.create(
            Soteria.id("spark_target"), builder -> builder.syncWith(PacketCodecs.VECTOR3F, (attachmentTarget, serverPlayerEntity) -> true)
    );

    public static void init() {
    }
}
