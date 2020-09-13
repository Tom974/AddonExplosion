package me.fede1132.explosion.enchants;

import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.CuboidRegionFactory;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.plasmaprisoncore.PlasmaPrisonCore;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantJackHammer extends Enchant {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    public EnchantJackHammer() {
        super("jackhammer",
                "JackHammer", 100, 1, "6", 100,
                new SimpleEntry<>("fast-mode", true), new SimpleEntry<>("region-blacklist", Arrays.asList("example", "region")));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
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
            session.setBlocks(cube, new BaseBlock(0));
            session.flushQueue();
            return new BreakResult(blocks, session.getBlockChangeCount());
        }
        return null;
    }
}
