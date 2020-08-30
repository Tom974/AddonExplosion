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
import me.fede1132.plasmaprisoncore.internal.util.regions.Region;
import me.fede1132.plasmaprisoncore.internal.util.regions.shapes.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EnchantTile extends Enchant {
    public EnchantTile() {
        super("tile", "Tile", 100, 1, "f", 100,
                new SimpleEntry<>("max-radius", 10));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(),getId());
        if (!EnchantUtil.chance(max,lvl,maxChance)) return null;
        double radius = (Integer) options[0].getValue();
        radius = Math.round(radius / max * lvl);
        if (radius==0) radius=1;
        Location loc = event.getBlock().getLocation();
        Region select = CuboidRegion.fromCenter(event.getBlock().getWorld(), loc, (int) radius, false);
        EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(select.getBlocks().stream().map(BukkitUtil::toVector).collect(Collectors.toSet()), new BaseBlock(0));
        List<Material> blocks = select.getBlocks().stream().map(locX->((CraftWorld) event.getBlock().getWorld()).getBlockTypeIdAt(
                locX.getBlockX(),
                locX.getBlockY(),
                locX.getBlockZ()))
                .map(Material::getMaterial)
                .collect(Collectors.toList());
        select.close();
        session.flushQueue();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
