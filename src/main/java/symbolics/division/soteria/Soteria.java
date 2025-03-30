package symbolics.division.soteria;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.soteria.compat.ModCompatibility;
import symbolics.division.soteria.network.PoiseSparkAttackC2S;

public class Soteria implements ModInitializer {
    public static final String MOD_ID = "soteria";

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    public static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String identififer) {
        return new CustomPayload.Id<>(id(identififer));
    }

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        SoterianItems.init();
        SoterianAttachments.init();
        SoterianEntities.init();
        SoterianSounds.init();
        SoterianDamageTypes.init();

        registerC2S(PoiseSparkAttackC2S.ID, PoiseSparkAttackC2S.CODEC, PoiseSparkAttackC2S::HANDLER);

        ModCompatibility.init();
    }

    private <T extends CustomPayload>
    void registerC2S(CustomPayload.Id<T> pid, PacketCodec<? super RegistryByteBuf, T> codec, ServerPlayNetworking.PlayPayloadHandler<T> handler) {
        PayloadTypeRegistry.playC2S().register(pid, codec);
        ServerPlayNetworking.registerGlobalReceiver(pid, handler);
    }
}