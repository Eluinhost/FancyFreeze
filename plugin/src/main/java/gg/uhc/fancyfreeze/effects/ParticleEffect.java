/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.effects.ParticleEffect
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
