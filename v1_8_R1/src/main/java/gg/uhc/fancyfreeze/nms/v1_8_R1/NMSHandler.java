package gg.uhc.fancyfreeze.nms.v1_8_R1;


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
}
