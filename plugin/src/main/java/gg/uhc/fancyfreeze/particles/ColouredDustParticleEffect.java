package gg.uhc.fancyfreeze.particles;

import com.google.common.base.Preconditions;
import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ColouredDustParticleEffect implements CustomParticleEffect {

    protected final Effect effect = Effect.COLOURED_DUST;

    protected final int dataValue = 0;
    protected final int blockID = 0;

    protected final float dX;
    protected final float dY;
    protected final float dZ;

    protected final float speed = 1F;
    protected final int count = 0;

    protected final int playerRadius;

    public ColouredDustParticleEffect(int R, int G, int B, int playerRadius) {
        Preconditions.checkArgument(R >=0 && R <=255);
        Preconditions.checkArgument(G >=0 && G <=255);
        Preconditions.checkArgument(B >=0 && B <=255);

        this.dX = ((float) R / 255F);
        this.dY = ((float) G / 255F);
        this.dZ = ((float) B / 255F);
        this.playerRadius = playerRadius;
    }

    @Override
    public void playAtLocation(Location location, Player... players) {
        if (players.length == 0) {
            location.getWorld().spigot().playEffect(
                    location,
                    effect,
                    blockID,
                    dataValue,
                    dX,
                    dY,
                    dZ,
                    speed,
                    count,
                    playerRadius
            );
        } else {
            for (Player player : players) {
                player.spigot().playEffect(
                        location,
                        effect,
                        blockID,
                        dataValue,
                        dX,
                        dY,
                        dZ,
                        speed,
                        count,
                        playerRadius
                );
            }
        }
    }
}
