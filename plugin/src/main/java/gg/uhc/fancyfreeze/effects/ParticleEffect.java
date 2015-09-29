package gg.uhc.fancyfreeze.effects;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleEffect implements CustomEffect {

    protected final Effect effect;
    protected final int dataValue;
    protected final int blockID;
    protected final float dX;
    protected final float dY;
    protected final float dZ;
    protected final float speed;
    protected final int count;
    protected final int playerRadius;

    public ParticleEffect(Effect effect, int dataValue, int blockID, float dX, float dY, float dZ, float speed, int count, int playerRadius) {
        this.effect = effect;
        this.dataValue = dataValue;
        this.blockID = blockID;
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
        this.speed = speed;
        this.count = count;
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
