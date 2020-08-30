package me.fede1132.explosion.enchants;

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
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
            Region cube = new CuboidRegion(pos1,pos2);
            session.setBlocks(cube, new BaseBlock(0));
            List<Material> blocks = cube.getChunkCubes().stream()
                    .map(locX->((CraftWorld) event.getBlock().getWorld()).getBlockTypeIdAt(
                            locX.getBlockX(),
                            locX.getBlockY(),
                            locX.getBlockZ()))
                    .map(Material::getMaterial)
                    .collect(Collectors.toList());
            session.flushQueue();
            return new BreakResult(blocks, session.getBlockChangeCount());
        }
        return null;
    }
}
