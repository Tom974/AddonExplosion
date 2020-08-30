package me.fede1132.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.explosion.Explosion;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import me.fede1132.plasmaprisoncore.internal.util.regions.Region;
import me.fede1132.plasmaprisoncore.internal.util.regions.shapes.CuboidRegion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EnchantExplosive extends Enchant {
    private final Explosion instance = ((Explosion) Explosion.getInstance());
    public EnchantExplosive() {
        super("explosive", "Explosive", 100, 1, "d", 100,
                new SimpleEntry<>("max-radius",10));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(),getId());
        if (!EnchantUtil.chance(max,lvl,maxChance)) return null;
        int rnd = new Random().nextInt((Integer) options[0].getValue());
        rnd = rnd<0?1:rnd;
        Region region = CuboidRegion.fromCenter(event.getBlock().getWorld(), event.getBlock().getLocation(), rnd, true);
        EditSession session = new EditSessionBuilder(FaweAPI.getWorld(event.getBlock().getWorld().getName())).fastmode(true).build();
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(region.getBlocks().stream().map(BukkitUtil::toVector).collect(Collectors.toSet()), new BaseBlock(0));
        List<Material> blocks = region.getBlocks().stream().map(locX->((CraftWorld) event.getBlock().getWorld()).getBlockTypeIdAt(
                locX.getBlockX(),
                locX.getBlockY(),
                locX.getBlockZ()))
                .map(Material::getMaterial)
                .collect(Collectors.toList());
        region.close();
        session.flushQueue();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
