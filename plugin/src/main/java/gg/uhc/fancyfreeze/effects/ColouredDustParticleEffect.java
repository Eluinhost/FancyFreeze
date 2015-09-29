package gg.uhc.fancyfreeze.effects;

import com.google.common.base.Preconditions;
import org.bukkit.Effect;

public class ColouredDustParticleEffect extends ParticleEffect {

    public ColouredDustParticleEffect(int R, int G, int B, int playerRadius) {
        super(Effect.COLOURED_DUST, 0, 0, convert(R), convert(G), convert(B), 1F, 0, playerRadius);
    }

    protected static float convert(int value) {
        Preconditions.checkArgument(value >=0 && value <=255);

        return ((float) value / 255F);
    }
}
