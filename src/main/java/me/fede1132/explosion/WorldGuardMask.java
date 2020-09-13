package me.fede1132.explosion;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldGuardMask implements Mask {
    private final Explosion explosion = Explosion.inst();
    private final WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
    private final RegionContainer container = worldGuardPlugin.getRegionContainer();
    private final List<ProtectedRegion> blacklist = new ArrayList<>();
    private final RegionManager manager;
    public WorldGuardMask(World world) {
        manager = container.get(world);
        explosion.config.getStringList("regions.blacklist").stream().map(manager::getRegion).filter(Objects::nonNull).forEach(blacklist::add);
    }

    @Override
    public boolean test(Vector vector) {
        return manager.getApplicableRegions(vector).getRegions().stream().anyMatch(region->region.getFlag(DefaultFlag.BLOCK_BREAK)== StateFlag.State.ALLOW);
    }
}
