package gg.uhc.fancyfreeze.effects.wrappers;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RingEffect implements CustomEffect {

    protected final CustomEffect effect;
    protected final Vector[] ringOffsets;

    public RingEffect(CustomEffect effect, double radius, int pointCount) {
        this.effect = effect;
        this.ringOffsets = generateRingOffsets(radius, pointCount);
    }

    @Override
    public void playAtLocation(Location location, Player... players) {
        for (Vector ringOffset : ringOffsets) {
            // play at the center + point offset
            effect.playAtLocation(location.clone().add(ringOffset), players);
        }
    }

    protected Vector[] generateRingOffsets(double radius, int points) {
        double anglePer = Math.PI * 2 / points;

        Vector[] offsets = new Vector[points];

        double angle;
        for (int i  = 0; i < points; i++) {
            angle = i * anglePer;

            // convert radians back into cartesian offset
            offsets[i] = new Vector(Math.cos(angle) * radius, 0, Math.sin(angle) * radius);
        }

        return offsets;
    }
}
