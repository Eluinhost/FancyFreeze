package gg.uhc.fancyfreeze.uhc;

import gg.uhc.fancyfreeze.announcer.GlobalAnnouncer;

public class UhcAnnouncer implements GlobalAnnouncer {

    protected final UhcModule module;

    public UhcAnnouncer(UhcModule module) {
        this.module = module;
    }

    @Override
    public void announceState() {
        module.announceState();
    }
}
