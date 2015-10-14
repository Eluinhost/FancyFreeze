package gg.uhc.fancyfreeze;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import gg.uhc.fancyfreeze.api.CustomEffect;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.api.NMSHandler;
import gg.uhc.fancyfreeze.api.nms.MovementspeedRemover;
import gg.uhc.fancyfreeze.commands.FreezeCommand;
import gg.uhc.fancyfreeze.commands.GlobalFreezeCommand;
import gg.uhc.fancyfreeze.effects.ColouredDustParticleEffect;
import gg.uhc.fancyfreeze.effects.DummyEffect;
import gg.uhc.fancyfreeze.effects.ParticleEffect;
import gg.uhc.fancyfreeze.effects.SoundEffect;
import gg.uhc.fancyfreeze.effects.wrappers.CombinationEffect;
import gg.uhc.fancyfreeze.effects.wrappers.RingEffect;
import gg.uhc.fancyfreeze.effects.wrappers.VerticalSpreadEffect;
import gg.uhc.fancyfreeze.listeners.DamageListener;
import gg.uhc.fancyfreeze.listeners.InteractListeners;
import gg.uhc.fancyfreeze.listeners.PortalListener;
import gg.uhc.fancyfreeze.listeners.PotionListener;
import gg.uhc.fancyfreeze.uhc.UhcModule;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Entry extends JavaPlugin {

    protected Freezer freezer = null;

    @Override
    public void onEnable() {
        NMSHandler handler = getNMSHandler();

        if (handler == null) {
            setEnabled(false);
            getLogger().severe("This version of Spigot is not supported by this plugin");
            return;
        }

        FileConfiguration configuration = getConfig();
        configuration.options().copyDefaults(true);
        saveConfig();

        MovementspeedRemover movementspeedRemover;
        List<PotionEffect> effects = Lists.newArrayList();

        if (configuration.getBoolean("remove movement speed")) {
            movementspeedRemover = handler.getMovementspeedRemover();
            effects.add(new PotionEffect(PotionEffectType.SLOW, Short.MAX_VALUE, 5));
        } else {
            movementspeedRemover = new DummyMovementspeedRemover();
        }

        if (configuration.getBoolean("remove jumping")) {
            effects.add(new PotionEffect(PotionEffectType.JUMP, Short.MAX_VALUE, -Byte.MAX_VALUE));
        }

        if (configuration.getBoolean("add mining fatigue")) {
            effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, Short.MAX_VALUE, 5));
        }

        if (configuration.getBoolean("add blindness")) {
            effects.add(new PotionEffect(PotionEffectType.BLINDNESS, Short.MAX_VALUE, 5));
        }

        double maxDistance = configuration.getDouble("max distance");

        FreezePotionsApplier potionsApplier = new FreezePotionsApplier(handler.getFakePotionApplier(), effects);

        // setup particle effects
        CustomEffect frozenEffect;
        CustomEffect warpEffect;

        if (configuration.getBoolean("use particles")) {
            CustomEffect dust = new ColouredDustParticleEffect(190, 240, 250, 30);
            CustomEffect ring = new RingEffect(dust, maxDistance, 50);
            frozenEffect = new VerticalSpreadEffect(ring, 4D, 6, -.5D);

            CustomEffect warpParticle = new ParticleEffect(Effect.VILLAGER_THUNDERCLOUD, 0, 0, 1, 1, 1, 0, 10, 30);
            CustomEffect warpSound = new SoundEffect(Sound.ANVIL_LAND, 1, 0);
            warpEffect = new CombinationEffect(warpParticle, warpSound);
        } else {
            frozenEffect = new DummyEffect();
            warpEffect = frozenEffect;
        }

        freezer = new DefaultFreezer(this, potionsApplier, movementspeedRemover, frozenEffect, warpEffect, maxDistance);

        // register listeners
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

        // register commands
        getCommand("ff").setExecutor(new FreezeCommand(freezer));
        getCommand("ffg").setExecutor(new GlobalFreezeCommand(freezer));

        Plugin uhc = Bukkit.getPluginManager().getPlugin("UHC");
        if (uhc != null) {
            UhcModule.hook(uhc, freezer);
        }
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
