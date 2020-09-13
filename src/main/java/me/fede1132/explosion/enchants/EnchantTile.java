package me.fede1132.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantTile extends Enchant {
    public EnchantTile() {
        super("tile", "Tile", 100, 1, "f", 100,
                new SimpleEntry<>("max-radius", 10));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(),getId());
        if (lvl==0||!EnchantUtil.chance(max,lvl,maxChance)) return null;
        double radius = (Integer) options[0].getValue();
        radius = Math.round(radius / max * lvl);
        if (radius==0) radius=1;
        Location loc = event.getBlock().getLocation();
        Region select = new CuboidRegion(FaweAPI.getWorld(loc.getWorld().getName()), BukkitUtil.toVector(loc.clone().add(radius, 0, radius)), BukkitUtil.toVector(loc.subtract(radius, 0, radius)));
        EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
        FaweQueue queue = session.getQueue();
        List<Material> blocks = StreamSupport.stream(Spliterators.spliteratorUnknownSize(select.iterator(), Spliterator.ORDERED), false)
                .map(queue::getLazyBlock)// loads every block in the selection
                .filter(Objects::nonNull) // null check
                .filter(block->!block.isAir()) // air check
                .map(BaseBlock::getType) // get block ids
                .map(Material::getMaterial) // map to materials
                .collect(Collectors.toList()); // collect to list
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(select, new BaseBlock(0));
        session.flushQueue();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
