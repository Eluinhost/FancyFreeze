/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.nms.v1_9_R2.NMSHandler
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

package gg.uhc.fancyfreeze.nms.v1_9_R2;


import org.bukkit.Sound;

public class NMSHandler implements gg.uhc.fancyfreeze.api.NMSHandler {

    protected final FakePotionApplier potionApplier;
    protected final MovementspeedRemover movementspeedRemover;

    public NMSHandler() {
        potionApplier = new FakePotionApplier();
        movementspeedRemover = new MovementspeedRemover();
    }

    @Override
    public FakePotionApplier getFakePotionApplier() {
        return potionApplier;
    }

    @Override
    public MovementspeedRemover getMovementspeedRemover() {
        return movementspeedRemover;
    }

    @Override
    public Sound getWarpSound() {
        return Sound.BLOCK_ANVIL_LAND;
    }
}
