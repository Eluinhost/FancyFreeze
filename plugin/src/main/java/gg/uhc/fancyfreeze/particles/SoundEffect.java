package gg.uhc.fancyfreeze.particles;

import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect implements CustomParticleEffect {

    protected final Sound sound;
    protected final float volume;
    protected final float pitch;

    public SoundEffect(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void playAtLocation(Location location, Player... players) {
        if (players.length == 0) {
            location.getWorld().playSound(location, sound, volume, pitch);
        } else {
            for (Player player : players) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }
}
