package symbolics.division.soteria;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class Murmur extends MovingSoundInstance {
    private static Murmur murmur = null;

    public static void track(@NotNull LivingEntity e) {
        if (murmur == null && shouldPlay(e)) {
            murmur = new Murmur(SoterianSounds.MURMUR, SoundCategory.MUSIC, e);
            MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.MUSIC);
            MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.RECORDS);
            MinecraftClient.getInstance().getSoundManager().play(murmur);
        } else if (murmur != null && !shouldPlay(e)) {
            murmur = null;
        }
    }

    public static boolean cancelMusic() {
        return murmur != null;
    }

    public static void clear() {
        if (murmur != null) murmur.setDone();
        murmur = null;
    }

    private static boolean shouldPlay(LivingEntity e) {
        return e.soteria$unliving() || Mind.memoir;
    }

    private final LivingEntity entity;

    public Murmur(SoundEvent soundEvent, SoundCategory soundCategory, LivingEntity e) {
        super(soundEvent, soundCategory, e.getRandom());
        updateCoords();
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.01f;
        this.entity = e;
    }

    private void updateCoords() {
        if (entity == null) return;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    @Override
    public boolean canPlay() {
        return super.canPlay();
    }

    @Override
    public void tick() {
        updateCoords();
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (shouldPlay(this.entity)) {
            if (player != null && this.volume < 0.7f && player.soteria$unliving()) {
                this.volume = 0.2f * (1f - player.soteria$residue());
            }
        } else {
            this.volume /= 2;
            if (this.volume <= 0.05f || !shouldPlay(entity)) this.setDone();
        }
    }
}
