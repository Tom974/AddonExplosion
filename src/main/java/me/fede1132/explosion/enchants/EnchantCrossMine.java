package me.fede1132.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionIntersection;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.plasmaprisoncore.PlasmaPrisonCore;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantCrossMine extends Enchant {
    public EnchantCrossMine() {
        super("crossmine", "CrossMine", 100, 1, "f", 100,
                new SimpleEntry<>("max-size", 10), new SimpleEntry<>("max-size-random", true));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(),getId());
        if (!EnchantUtil.chance(max,lvl,maxChance)) return null;
        int maxSize = (Integer) options[0].getValue();
        boolean rnd = (Boolean) options[1].getValue();
        if (rnd) {
            maxSize = new Random().nextInt(maxSize);
            if (maxSize==0) maxSize=1;
        } else {
            maxSize = (int) Math.round((((double)maxSize / (double)max) * lvl));
        }
        Location loc = event.getBlock().getLocation();
        CuboidRegion horizontal = new CuboidRegion(FaweAPI.getWorld(loc.getWorld().getName()), BukkitUtil.toVector(loc.clone().add(maxSize,maxSize+1,0)), BukkitUtil.toVector(loc.clone().subtract(maxSize,maxSize+1,0)));
        CuboidRegion vertical = new CuboidRegion(FaweAPI.getWorld(loc.getWorld().getName()), BukkitUtil.toVector(loc.clone().add(0,maxSize+1,maxSize)), BukkitUtil.toVector(loc.clone().subtract(0,maxSize+1,maxSize)));
        Region cross = new RegionIntersection(horizontal, vertical);
        EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
        FaweQueue queue = session.getQueue();
        List<Material> blocks = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cross.iterator(), Spliterator.ORDERED), false)
                .map(queue::getLazyBlock)// loads every block in the selection
                .filter(Objects::nonNull) // null check
                .filter(block->!block.isAir()) // air check
                .map(BaseBlock::getId) // get block ids
                .map(Material::getMaterial) // map to materials
                .collect(Collectors.toList()); // collect to list
        session.setBlocks(cross, new BaseBlock(0));
        session.flushQueue();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
