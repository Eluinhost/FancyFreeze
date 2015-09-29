package gg.uhc.fancyfreeze;

import com.google.common.collect.ImmutableList;
import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.api.NMSHandler;
import gg.uhc.fancyfreeze.commands.FreezeCommand;
import gg.uhc.fancyfreeze.commands.GlobalFreezeCommand;
import gg.uhc.fancyfreeze.listeners.DamageListener;
import gg.uhc.fancyfreeze.listeners.InteractListeners;
import gg.uhc.fancyfreeze.listeners.PortalListener;
import gg.uhc.fancyfreeze.listeners.PotionListener;
import gg.uhc.fancyfreeze.particles.ColouredDustParticleEffect;
import gg.uhc.fancyfreeze.particles.RingParticleEffect;
import gg.uhc.fancyfreeze.particles.SoundEffect;
import gg.uhc.fancyfreeze.particles.VerticalSpreadParticleEffect;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Entry extends JavaPlugin {

    protected Freezer freezer = null;

    @Override
    public void onEnable() {
        double maxDistance = 3D;

        CustomParticleEffect dust = new ColouredDustParticleEffect(190, 240, 250, 30);
        CustomParticleEffect ring = new RingParticleEffect(dust, maxDistance, 50);
        CustomParticleEffect frozenEffect = new VerticalSpreadParticleEffect(ring, 4D, 6, -.5D);

        NMSHandler handler = getNMSHandler();

        if (handler == null) {
            setEnabled(false);
            getLogger().severe("This version of Spigot is not supported by this plugin");
            return;
        }

        CustomParticleEffect warpEffect = new SoundEffect(Sound.ANVIL_LAND, 1, 0);

        freezer = new DefaultFreezer(this, handler.getFakePotionApplier(), handler.getMovementspeedRemover(), frozenEffect, warpEffect, maxDistance);

        List<Listener> listeners = ImmutableList.<Listener>builder()
                .add(freezer)
                .add(new PotionListener(freezer))
                .add(new DamageListener(freezer))
                .add(new PortalListener(freezer))
                .add(new InteractListeners(freezer))
                .build();

        PluginManager manager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }

        getCommand("ff").setExecutor(new FreezeCommand(freezer));
        getCommand("ffg").setExecutor(new GlobalFreezeCommand(freezer));
    }

    public void onDisable() {
        if (freezer != null) {
            // to avoid changes in NMS not carrying over on reload we have to remove all current freezes
            freezer.disableGlobalFreeze();
            freezer.clearAlwaysFreezeList();

            freezer = null;
        }
    }

    protected NMSHandler getNMSHandler() {
        String packageName = getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);

        switch (version) {
            case "v1_8_R1":
                return new gg.uhc.fancyfreeze.nms.v1_8_R1.NMSHandler();
            case "v1_8_R2":
                return new gg.uhc.fancyfreeze.nms.v1_8_R2.NMSHandler();
            case "v1_8_R3":
                return new gg.uhc.fancyfreeze.nms.v1_8_R3.NMSHandler();
            default:
                return null;
        }
    }
}
