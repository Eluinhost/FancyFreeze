/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.effects.wrappers.RingEffect
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
