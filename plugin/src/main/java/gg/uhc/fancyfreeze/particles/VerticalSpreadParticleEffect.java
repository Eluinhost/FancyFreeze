package gg.uhc.fancyfreeze.particles;

import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VerticalSpreadParticleEffect implements CustomParticleEffect {

    protected final CustomParticleEffect effect;
    protected final int count;
    protected final double yOffsetPerEffect;
    protected final double offset;

    public VerticalSpreadParticleEffect(CustomParticleEffect effect, double height, int count, double offset) {
        this.effect = effect;
        this.count = count;
        this.yOffsetPerEffect = height / Math.max(count - 1, 1);
        this.offset = offset;
    }

    @Override
    public void playAtLocation(Location location, Player... players) {
        Location centre = location.clone().add(0, offset, 0);

        for (int effectNo = 0; effectNo < count; effectNo++) {
            effect.playAtLocation(centre, players);
            centre.add(0, yOffsetPerEffect, 0);
        }
    }
}
