package gg.uhc.fancyfreeze.api;

import gg.uhc.fancyfreeze.api.nms.FakePotionApplier;
import gg.uhc.fancyfreeze.api.nms.MovementspeedRemover;

public interface NMSHandler {
    FakePotionApplier getFakePotionApplier();
    MovementspeedRemover getMovementspeedRemover();
}
