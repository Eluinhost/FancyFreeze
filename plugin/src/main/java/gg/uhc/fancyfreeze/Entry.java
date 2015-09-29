package gg.uhc.fancyfreeze;

import com.google.common.collect.ImmutableList;
import gg.uhc.fancyfreeze.api.CustomEffect;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.api.NMSHandler;
import gg.uhc.fancyfreeze.commands.FreezeCommand;
import gg.uhc.fancyfreeze.commands.GlobalFreezeCommand;
import gg.uhc.fancyfreeze.effects.ColouredDustParticleEffect;
import gg.uhc.fancyfreeze.effects.ParticleEffect;
import gg.uhc.fancyfreeze.effects.SoundEffect;
import gg.uhc.fancyfreeze.effects.wrappers.CombinationEffect;
import gg.uhc.fancyfreeze.effects.wrappers.RingEffect;
import gg.uhc.fancyfreeze.effects.wrappers.VerticalSpreadEffect;
import gg.uhc.fancyfreeze.listeners.DamageListener;
import gg.uhc.fancyfreeze.listeners.InteractListeners;
import gg.uhc.fancyfreeze.listeners.PortalListener;
import gg.uhc.fancyfreeze.listeners.PotionListener;
import org.bukkit.Effect;
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

        CustomEffect dust = new ColouredDustParticleEffect(190, 240, 250, 30);
        CustomEffect ring = new RingEffect(dust, maxDistance, 50);
        CustomEffect frozenEffect = new VerticalSpreadEffect(ring, 4D, 6, -.5D);

        NMSHandler handler = getNMSHandler();

        if (handler == null) {
            setEnabled(false);
            getLogger().severe("This version of Spigot is not supported by this plugin");
            return;
        }

        CustomEffect warpParticle = new ParticleEffect(Effect.VILLAGER_THUNDERCLOUD, 0, 0, 1, 1, 1, 0, 10, 30);
        CustomEffect warpSound = new SoundEffect(Sound.ANVIL_LAND, 1, 0);
        CustomEffect warpEffect = new CombinationEffect(warpParticle, warpSound);

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
