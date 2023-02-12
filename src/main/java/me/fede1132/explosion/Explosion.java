package me.fede1132.explosion;

import me.fede1132.explosion.enchants.*;
import de.leonhard.storage.Yaml;
import me.fede1132.plasmaprisoncore.addons.Addon;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
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
        // try (Connection connection = plugin.database.getConnection()) {
        //     connection.prepareStatement("CREATE TABLE IF NOT EXISTS explosion_prefs (" +
        //             " `uuid` CHAR(36) NOT NULL," +
        //             " `explosive` VARCHAR(5) NOT NULL," +
        //             " PRIMARY KEY(`uuid`))").executeUpdate();
        //     connection.close();
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        config = setupPersonalConfig("config.yml",
                new SimpleEntry<>("messages.explosive-toggle-true", "&fExplosive enchant toggled to true"),
                new SimpleEntry<>("messages.explosive-toggle-false", "&fExplosive enchant toggled to false"),
                new SimpleEntry<>("regions.blacklist", Arrays.asList("main")));
        registerEnchants(new EnchantAtomBomb(), new EnchantCrossMine(), new EnchantExplosive(), new EnchantJackHammer(), new EnchantNuke());
        registerCommands(new CmdExplosive());
    }

    public boolean toggleExplosive(UUID uuid) {
        // try (Connection connection = plugin.database.getConnection()) {
        //     PreparedStatement ps = connection.prepareStatement("SELECT * FROM explosion_prefs WHERE uuid = ?");
        //     ps.setString(1, uuid.toString());
        //     ResultSet rs = ps.executeQuery();
        //     boolean next = rs.next();
        //     boolean curr = !next || Boolean.parseBoolean(rs.getString("explosive"));
        //     PreparedStatement update = connection.prepareStatement(next?"UPDATE explosion_prefs SET explosive = ? WHERE uuid = ?":
        //             "INSERT INTO explosion_prefs (explosive, uuid) VALUES (?, ?)");
        //     update.setString(1, String.valueOf(curr=!curr));
        //     update.setString(2, uuid.toString());
        //     update.executeUpdate();
        //     connection.close();
        //     return curr;
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        return false;
    }

    public boolean getExplosiveStatus(UUID uuid) {
        // try (Connection connection = plugin.database.getConnection()) {
        //     PreparedStatement ps = connection.prepareStatement("SELECT * FROM explosion_prefs WHERE uuid = ?");
        //     ps.setString(1, uuid.toString());
        //     ResultSet rs = ps.executeQuery();
        //     if (!rs.next()) {
        //         connection.close();
        //         return true;
        //     } else {
        //         connection.close();
        //         return Boolean.parseBoolean(rs.getString("explosive"));
        //     }

            return true;
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        // return false;
    }

    public static Explosion inst() {
        return instance;
    }

}
