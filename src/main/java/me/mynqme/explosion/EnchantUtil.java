package me.mynqme.explosion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.function.mask.*;
import org.bukkit.World;

import java.util.Random;

public class EnchantUtil {
    private static WorldGuardMask wgMask;
    public static Mask getMask(World world, EditSession session) {
        if (wgMask==null) wgMask = new WorldGuardMask(world);
        // use replacement of new BaseBlock since baseblock is deprecated
        return new MaskIntersection(Masks.negate(new BlockMask(session, new BaseBlock(7))), wgMask);
    }

    public static boolean chance(int max, int lvl, double maxChance) {
        int rnd = new Random().nextInt(99);
        double percent = ((double) lvl / (double) max) * 100D;
        if (percent>maxChance) percent = maxChance;
        return percent>=rnd;
    }
}
