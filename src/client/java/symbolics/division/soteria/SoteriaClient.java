package symbolics.division.soteria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.soteria.item.SoterianLance;
import symbolics.division.soteria.network.PoiseSparkAttackC2S;
import symbolics.division.spirit_vector.logic.ISpiritVectorUser;

public class SoteriaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SoterianLance.hitCallback = (target, player, damage, pos) -> {
            if (player instanceof ISpiritVectorUser user && user.spiritVector() != null) {
                int id = target == null ? 0 : target.getId();
                ClientPlayNetworking.send(new PoiseSparkAttackC2S(damage, id, pos));

                World world = player.getWorld();
                Vec3d anchor = player.getEyePos().add(player.getRotationVec(0).multiply(2));
                for (float i = 0; i < 10; i++) {
                    Vec3d p = anchor.add(0.5f + world.getRandom().nextFloat(), 0.5f + world.getRandom().nextFloat(), 0.5f + world.getRandom().nextFloat());
                    Vec3d d = p.subtract(anchor);
                    world.addParticle(ParticleTypes.FLASH, p.x, p.y, p.z, d.x, d.y, d.z);
                }
            }
            return true;
        };

        EntityRendererRegistry.register(SoterianEntities.POISE_SPARK, PoiseSparkRenderer::new);
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity == MinecraftClient.getInstance().player) Mind.memoir = false;
        });
    }
}