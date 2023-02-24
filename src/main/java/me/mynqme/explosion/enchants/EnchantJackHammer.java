package me.mynqme.explosion.enchants;

import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.mynqme.explosion.EnchantUtil;
import me.mynqme.explosion.Explosion;
import me.mynqme.plasmaprisoncore.enchant.BreakResult;
import me.mynqme.plasmaprisoncore.enchant.Enchant;
import me.mynqme.plasmaprisoncore.enchant.EnchantManager;
import me.mynqme.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantJackHammer extends Enchant {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    private final Explosion instance = ((Explosion) Explosion.getInstance());
    public EnchantJackHammer() {
        super("jackhammer", "JackHammer", 100, 1, "&d▎ &3%name% &f%level%", 100.0, new SimpleEntry<>("fast-mode", true), new SimpleEntry<>("region-blacklist", Arrays.asList("example", "region")));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
        RegionManager manager = container.get(event.getBlock().getWorld());
        for (ProtectedRegion region : manager.getApplicableRegions(event.getBlock().getLocation())) {
            if (region.getFlag(DefaultFlag.BLOCK_BREAK)!=StateFlag.State.ALLOW||region.getId().toLowerCase().equals("__global__")) continue;
            int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(), "jackhammer");
            if (!EnchantUtil.chance(max,lvl,maxChance)) return null;
            Vector pos1 = region.getMinimumPoint().setY(event.getBlock().getY());
            Vector pos2 = region.getMaximumPoint().setY(event.getBlock().getY());
            EditSession session = new EditSessionBuilder(event.getBlock().getWorld().getName()).fastmode((Boolean) options[0].getValue()).build();
            Region cube = new CuboidRegion(pos1,pos2);
            FaweQueue queue = session.getQueue();
            List<Material> blocks = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cube.iterator(), Spliterator.ORDERED), false)
                    .map(queue::getLazyBlock)// loads every block in the selection
                    .filter(Objects::nonNull) // null check
                    .filter(block->!block.isAir()) // air check
                    .map(BaseBlock::getId) // get block ids
                    .map(Material::getMaterial) // map to materials
                    .collect(Collectors.toList()); // collect to list
            session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
            // get amount of blocks from cube
            session.setBlocks(cube, new BaseBlock(0));
            session.flushQueue();
            return new BreakResult(blocks, session.getBlockChangeCount());
        }
        return null;
    }
}
