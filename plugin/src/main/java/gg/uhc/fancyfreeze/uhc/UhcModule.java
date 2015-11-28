package gg.uhc.fancyfreeze.uhc;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.events.GlobalFreezeEvent;
import gg.uhc.fancyfreeze.events.GlobalUnfreezeEvent;
import gg.uhc.uhc.UHC;
import gg.uhc.uhc.modules.DisableableModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class UhcModule extends DisableableModule implements Listener {

    protected static final String ICON_NAME = "Fancy Freeze";

    protected final Freezer freezer;

    public static Optional<UhcModule> hook(Plugin uhcPlugin, Freezer freezer) {
        UhcModule module = new UhcModule(freezer);
        boolean loaded = ((UHC) uhcPlugin).getRegistry().register(module);

        if (loaded) return Optional.of(module);

        return Optional.absent();
    }

    public UhcModule(Freezer freezer) {
        this.freezer = freezer;
        setId("FancyFreeze");

        this.iconName = ICON_NAME;
        this.icon.setType(Material.INK_SACK);
        this.icon.setDurability((short) 4);
    }

    protected List<String> getEnabledLore() {
        return Lists.newArrayList("Global freeze is enabled");
    }

    protected List<String> getDisabledLore() {
        return Lists.newArrayList("Global freeze is disabled");
    }

    @EventHandler
    public void on(GlobalFreezeEvent event) {
        enable();
    }

    @EventHandler
    public void on(GlobalUnfreezeEvent event) {
        disable();
    }

    @Override
    public void onEnable() {
        freezer.enableGlobalFreeze();
    }

    @Override
    public void onDisable() {
        freezer.disableGlobalFreeze();
    }

    @Override
    protected boolean isEnabledByDefault() {
        return false;
    }
}
