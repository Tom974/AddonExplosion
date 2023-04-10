package me.mynqme.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.mynqme.explosion.EnchantUtil;
import me.mynqme.explosion.Explosion;
import me.mynqme.plasmaprisoncore.PlasmaPrisonCore;
import me.mynqme.plasmaprisoncore.enchant.BreakResult;
import me.mynqme.plasmaprisoncore.enchant.Enchant;
import me.mynqme.plasmaprisoncore.enchant.EnchantManager;
import me.mynqme.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantNuke extends Enchant {
    private final Explosion instance = ((Explosion) Explosion.getInstance());

    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    public EnchantNuke() {
        super("nuke", "Nuke", 100, 1, "&d▎ &3%name% &f%level%", 100.0, new SimpleEntry("max-sphere-size", 10), new SimpleEntry("max-sphere-size-random", true));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
//        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
            Optional<ProtectedRegion> opt = container.get(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation()).getRegions().stream().filter(region->region.getFlag(DefaultFlag.BLOCK_BREAK)==StateFlag.State.ALLOW&&!region.getId().equals("__global__") && !region.getId().equals("mine-event")).findFirst();
            if (!opt.isPresent()) return null;

            int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(), getId());
            if (!EnchantUtil.chance(max, lvl, maxChance)) return null;
            int maxSphereSize = (Integer) options[0].getValue();
            boolean rnd = (Boolean) options[1].getValue();
            if (rnd) {
                maxSphereSize = new Random().nextInt(maxSphereSize);
                if (maxSphereSize == 0) maxSphereSize = 1;
            }


            EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
            // get region center at the top
            ProtectedRegion reg = container.get(event.getBlock().getWorld()).getRegion(opt.get().getId());

            com.sk89q.worldedit.Vector centerTop = reg.getMaximumPoint().add(reg.getMinimumPoint()).divide(2);
            centerTop = centerTop.setY(reg.getMaximumPoint().getY());
            Location correctLocation = new Location(event.getBlock().getWorld(), centerTop.getX(), centerTop.getY(), centerTop.getZ());
            Region sphere = new SphereRegionFactory().createCenteredAt(BukkitUtil.toVector(correctLocation), maxSphereSize);
            FaweQueue queue = session.getQueue();
            List<Material> blocks = StreamSupport.stream(Spliterators.spliteratorUnknownSize(sphere.iterator(), Spliterator.ORDERED), false)
                    .map(queue::getLazyBlock)// loads every block in the selection
                    .filter(Objects::nonNull) // null check
                    .filter(block -> !block.isAir()) // air check
                    .map(BaseBlock::getId) // get block ids
                    .map(Material::getMaterial) // map to materials
                    .collect(Collectors.toList()); // collect to list

            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            // wait 1 second
            Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), () -> {
                session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
                session.setBlocks(sphere, new BaseBlock(0));
                session.flushQueue();
            }, 20);


            return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
