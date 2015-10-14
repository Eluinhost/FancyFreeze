package gg.uhc.fancyfreeze.uhc;

import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.events.GlobalFreezeEvent;
import gg.uhc.fancyfreeze.events.GlobalUnfreezeEvent;
import gg.uhc.uhc.UHC;
import gg.uhc.uhc.modules.DisableableModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class UhcModule extends DisableableModule implements Listener {

    protected static final String ICON_NAME = "Fancy Freeze";

    protected final Freezer freezer;

    public static void hook(Plugin uhcPlugin, Freezer freezer) {
        ((UHC) uhcPlugin).getRegistry().register(new UhcModule(freezer), "FancyFreeze");
    }

    public UhcModule(Freezer freezer) {
        this.freezer = freezer;

        this.iconName = ICON_NAME;
        this.icon.setType(Material.INK_SACK);
        this.icon.setDurability((short) 4);
    }

    @Override
    public void rerender() {
        super.rerender();

        icon.setLore(isEnabled() ? "Global freeze is enabled" : "Global freeze is disabled");
    }

    @EventHandler
    public void on(GlobalFreezeEvent event) {
        this.enabled = true;
        this.config.set("enabled", Boolean.valueOf(true));
        this.saveConfig();
        rerender();
    }

    @EventHandler
    public void on(GlobalUnfreezeEvent event) {
        this.enabled = false;
        this.config.set("enabled", Boolean.valueOf(false));
        this.saveConfig();
        rerender();
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
