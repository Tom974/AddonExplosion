package me.fede1132.explosion.enchants;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.fede1132.explosion.EnchantUtil;
import me.fede1132.plasmaprisoncore.enchant.BreakResult;
import me.fede1132.plasmaprisoncore.enchant.Enchant;
import me.fede1132.plasmaprisoncore.enchant.EnchantManager;
import me.fede1132.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnchantAtomBomb extends Enchant {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    public EnchantAtomBomb() {
        super("atombomb",
                "Atom Bomb",
                1, 1, "4", 100,
                new SimpleEntry<>("fast-mode", true));
    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(), getId());
        if (lvl==0 || !EnchantUtil.chance(max, lvl, maxChance)) return null;
        Optional<ProtectedRegion> opt = container.get(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation()).getRegions().stream().filter(region->region.getFlag(DefaultFlag.BLOCK_BREAK)==StateFlag.State.ALLOW&&!region.getId().equals("__global__")).findFirst();
        if (!opt.isPresent()) return null;
        ProtectedRegion region = opt.get();
        CuboidRegion cuboid = new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
        List<Material> blocks = cuboid.getChunkCubes().stream()
                .map(locX->((CraftWorld) event.getBlock().getWorld()).getBlockTypeIdAt(
                        locX.getBlockX(),
                        locX.getBlockY(),
                        locX.getBlockZ()))
                .map(Material::getMaterial)
                .collect(Collectors.toList());
        EditSession session = new EditSessionBuilder(event.getBlock().getWorld().getName()).fastmode(Boolean.parseBoolean(options[0].getValue().toString())).build();
        session.setMask(EnchantUtil.getMask(event.getBlock().getWorld(), session));
        session.setBlocks(cuboid, new BaseBlock(0));
        session.flushQueue();
        return new BreakResult(blocks, session.getBlockChangeCount());
    }
}
