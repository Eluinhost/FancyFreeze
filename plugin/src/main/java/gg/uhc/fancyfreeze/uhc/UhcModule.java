/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.uhc.UhcModule
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

package gg.uhc.fancyfreeze.uhc;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.events.GlobalFreezeEvent;
import gg.uhc.fancyfreeze.events.GlobalUnFreezeEvent;
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
    public void on(GlobalUnFreezeEvent event) {
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
