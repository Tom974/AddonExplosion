package me.mynqme.explosion.enchants;

import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
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

public class EnchantAtomBomb extends Enchant {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    private final Explosion instance = ((Explosion) Explosion.getInstance());
    public EnchantAtomBomb() {
        super("atombomb", "Atom Bomb", 1, 1, "&d▎ &3%name% &f%level%", 100.0);
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
//        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(), getId());
        if (lvl==0 || !EnchantUtil.chance(max, lvl, maxChance)) return null;
        Optional<ProtectedRegion> opt = container.get(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation())
                .getRegions().stream().filter(region->region.getFlag(DefaultFlag.BLOCK_BREAK)==StateFlag.State.ALLOW&&!region.getId().equals("__global__")
                        && !region.getId().equals("mine-event")).findFirst();
        if (!opt.isPresent()) return null;
        ProtectedRegion region = opt.get();
        CuboidRegion cuboid = new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
        EditSession session = new EditSessionBuilder(event.getBlock().getWorld().getName()).fastmode(Boolean.parseBoolean(options[0].getValue().toString())).build();
        FaweQueue queue = session.getQueue();
        List<Material> blocks = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cuboid.iterator(), Spliterator.ORDERED), false)
                .map(queue::getLazyBlock)// loads every block in the selection
                .filter(Objects::nonNull) // null check
                .filter(block->!block.isAir()) // air check
                .map(BaseBlock::getId) // get block ids
                .map(Material::getMaterial) // map to materials
                .collect(Collectors.toList()); // collect to list
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(cuboid, new BaseBlock(0));
        session.flushQueue();
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
