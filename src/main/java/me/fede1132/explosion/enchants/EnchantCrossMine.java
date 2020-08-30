package me.fede1132.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import me.fede1132.plasmaprisoncore.internal.util.regions.shapes.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        CuboidRegion line = new CuboidRegion(event.getBlock().getWorld(),
                loc.clone().add(maxSize,maxSize+1,0),
                loc.clone().subtract(maxSize,maxSize+1,0));
        CuboidRegion line2 = new CuboidRegion(event.getBlock().getWorld(),
                loc.clone().add(0,maxSize+1,maxSize),
                loc.clone().subtract(0,maxSize+1,maxSize));
        line.merge(line2);
        List<Material> blocks = line.getBlocks().stream().map(locX->((CraftWorld) event.getBlock().getWorld()).getBlockTypeIdAt(
                locX.getBlockX(),
                locX.getBlockY(),
                locX.getBlockZ()))
                .map(Material::getMaterial)
                .collect(Collectors.toList());
        line2.close();
        EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(line.getBlocks().stream().map(BukkitUtil::toVector).collect(Collectors.toSet()), new BaseBlock(0));
        session.flushQueue();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
