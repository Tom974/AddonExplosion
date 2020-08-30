package me.fede1132.explosion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskUnion;
import com.sk89q.worldedit.function.mask.Masks;
import org.bukkit.World;

import java.util.Random;

public class EnchantUtil {
    private static WorldGuardMask wgMask;
    public static Mask getMask(World world, EditSession session) {
        if (wgMask==null) wgMask = new WorldGuardMask(world);
        return Masks.negate(new MaskUnion(wgMask, new BlockMask(session, new BaseBlock(7))));
    }

    public static boolean chance(int max, int lvl, int maxChance) {
        int rnd = new Random().nextInt(99);
        double percent = ((double) lvl / (double) max) * 100D;
        if (percent>maxChance) percent = maxChance;
        return percent>=rnd;
    }
}
