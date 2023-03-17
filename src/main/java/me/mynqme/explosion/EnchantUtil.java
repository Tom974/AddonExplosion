package me.mynqme.explosion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.function.mask.*;
import org.bukkit.World;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantUtil {
    private static WorldGuardMask wgMask;
    public static Mask getMask(World world, EditSession session) {
        if (wgMask==null) wgMask = new WorldGuardMask(world);
        return new MaskIntersection(Masks.negate(new BlockMask(session, new BaseBlock(7))), wgMask);
    }

    public static boolean chance(int max, int lvl, double maxChance) {
        double percent = ((double) lvl / (double) max) * maxChance;
        if (percent>maxChance) percent = maxChance;
        double random = ThreadLocalRandom.current().nextDouble(0.0, 100D);
        return percent >= random;
    }
}
