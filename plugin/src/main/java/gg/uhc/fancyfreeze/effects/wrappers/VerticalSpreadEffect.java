/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.effects.wrappers.VerticalSpreadEffect
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

public class VerticalSpreadEffect implements CustomEffect {

    protected final CustomEffect effect;
    protected final int count;
    protected final double yOffsetPerEffect;
    protected final double offset;

    public VerticalSpreadEffect(CustomEffect effect, double height, int count, double offset) {
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
