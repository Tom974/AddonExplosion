package me.mynqme.explosion;

import me.mynqme.explosion.enchants.*;
import de.leonhard.storage.Yaml;
import me.mynqme.plasmaprisoncore.addons.Addon;
import me.mynqme.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class Explosion extends Addon {
    private static Explosion instance;
    public Yaml config;
    @Override
    public void load() {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            plugin.getLogger().warning("Could not find plugin WorldGuard. Unloading..");
            unload();
        }
        instance=this;
        plugin.database.createExplosionTable();
        config = setupPersonalConfig("config.yml",
                new SimpleEntry<>("messages.explosive-toggle-true", "&fExplosive enchant toggled to true"),
                new SimpleEntry<>("messages.explosive-toggle-false", "&fExplosive enchant toggled to false"),
                new SimpleEntry<>("regions.blacklist", Arrays.asList("main"))
        );
        registerEnchants(new EnchantAtomBomb(), new EnchantCrossMine(), new EnchantExplosive(), new EnchantJackHammer(), new EnchantNuke());
        registerCommands(new CmdExplosive());
    }

    public boolean toggleExplosive(UUID uuid) {
         return plugin.database.toggleExplosive(uuid);
    }

    public boolean getExplosiveStatus(UUID uuid) {
         return plugin.database.getExplosiveStatus(uuid);
    }

    public static Explosion inst() {
        return instance;
    }

}
