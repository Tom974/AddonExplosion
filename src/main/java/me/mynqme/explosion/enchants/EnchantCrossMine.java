package me.mynqme.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionIntersection;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.mynqme.explosion.EnchantUtil;
import me.mynqme.explosion.Explosion;
import me.mynqme.plasmaprisoncore.enchant.BreakResult;
import me.mynqme.plasmaprisoncore.enchant.Enchant;
import me.mynqme.plasmaprisoncore.enchant.EnchantManager;
import me.mynqme.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantCrossMine extends Enchant {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    private final Explosion instance = ((Explosion) Explosion.getInstance());
    public EnchantCrossMine() {
        super("crossmine", "CrossMine", 100, 1, "&d▎ &3%name% &f%level%", 100.0, new SimpleEntry<>("max-size", 10), new SimpleEntry<>("max-size-random", true));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
//        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
        Optional<ProtectedRegion> opt = container.get(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation()).getRegions()
                .stream().filter(region->region.getFlag(DefaultFlag.BLOCK_BREAK)== StateFlag.State.ALLOW&&!region.getId().equals("__global__") && !region.getId().equals("mine-event")).findFirst();
        if (!opt.isPresent()) return null;
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
        com.sk89q.worldedit.Vector pos1 = opt.get().getMinimumPoint().setZ(event.getBlock().getZ());
        com.sk89q.worldedit.Vector pos2 = opt.get().getMaximumPoint().setZ(event.getBlock().getZ());

        com.sk89q.worldedit.Vector pos3 = opt.get().getMinimumPoint().setX(event.getBlock().getX());
        com.sk89q.worldedit.Vector pos4 = opt.get().getMaximumPoint().setX(event.getBlock().getX());
        CuboidRegion horizontal = new CuboidRegion(FaweAPI.getWorld(loc.getWorld().getName()), pos1, pos2);
        CuboidRegion vertical = new CuboidRegion(FaweAPI.getWorld(loc.getWorld().getName()), pos3, pos4);
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
