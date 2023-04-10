package me.mynqme.explosion.enchants;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.mynqme.explosion.EnchantUtil;
import me.mynqme.explosion.Explosion;
import me.mynqme.plasmaprisoncore.PlasmaPrisonCore;
import me.mynqme.plasmaprisoncore.enchant.BreakResult;
import me.mynqme.plasmaprisoncore.enchant.Enchant;
import me.mynqme.plasmaprisoncore.enchant.EnchantManager;
import me.mynqme.plasmaprisoncore.internal.util.SimpleEntry;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnchantSplash extends Enchant {
    private final Explosion instance = ((Explosion) Explosion.getInstance());

    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
    public EnchantSplash() {
        super("splash", "Splash", 100, 1, "&d▎ &3%name% &f%level%", 100.0, new SimpleEntry("max-sphere-size", 10), new SimpleEntry("max-sphere-size-random", true));
    }

    private void spawnFallingBlocks(Block b, int xMin, int xMax, int z){
        for (int coord = xMin; coord <= xMax; coord++) {
            FallingBlock fb = b.getWorld().spawnFallingBlock(new org.bukkit.Location(b.getWorld(), coord, b.getY(), z), b.getType(), b.getData());
//            fb.setDropItem(false);
            // pick a random decimal number between 0.0 and 1.0
            double random = Math.random();
            fb.setVelocity(new Vector(0, random, 0));

            // remove falling block after 5 seconds
            Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
                public void run() {
                    Bukkit.getLogger().warning("Removing falling block");
                    fb.remove();
                }
            }, 50);
//                b.setType(Material.AIR);

        }
    }

    public void spawnAnvil(Location blockLocation) {

        List<Location> locs = new ArrayList<>();
        locs.add(new Location(blockLocation.getWorld(), 0, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), 2, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), 3, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), -2, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), -3, 14, 0));
        locs.add(new Location(blockLocation.getWorld(), 0, 14, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 14, 1));
        locs.add(new Location(blockLocation.getWorld(), 5, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), 4, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), -2, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), -3, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), -4, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), -5, 15, 0));
        locs.add(new Location(blockLocation.getWorld(), 3, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), 2, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), 1, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), -1, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), -2, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), -3, 15, -1));
        locs.add(new Location(blockLocation.getWorld(), -3, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), -2, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), -1, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), 1, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), 2, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), 3, 15, 1));
        locs.add(new Location(blockLocation.getWorld(), -5, 16, 1));
        locs.add(new Location(blockLocation.getWorld(), -4, 16, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 16, 1));
        locs.add(new Location(blockLocation.getWorld(), 4, 16, 1));
        locs.add(new Location(blockLocation.getWorld(), 5, 16, 1));
        locs.add(new Location(blockLocation.getWorld(), 6, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), 5, 16, -1));
        locs.add(new Location(blockLocation.getWorld(), 4, 16, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 16, -1));
        locs.add(new Location(blockLocation.getWorld(), -4, 16, -1));
        locs.add(new Location(blockLocation.getWorld(), -5, 16, -1));
        locs.add(new Location(blockLocation.getWorld(), -6, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), 3, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), 2, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), -2, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), -3, 16, 0));
        locs.add(new Location(blockLocation.getWorld(), 7, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), 6, 17, -1));
        locs.add(new Location(blockLocation.getWorld(), 6, 17, 1));
        locs.add(new Location(blockLocation.getWorld(), 5, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), 4, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), 0, 17, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 17, 1));
        locs.add(new Location(blockLocation.getWorld(), -4, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), -5, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), -7, 17, 0));
        locs.add(new Location(blockLocation.getWorld(), -6, 17, 1));
        locs.add(new Location(blockLocation.getWorld(), -6, 17, -1));
        locs.add(new Location(blockLocation.getWorld(), 7, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), 7, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), 7, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), 5, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), 5, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), 5, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), 6, 18, 1));
        locs.add(new Location(blockLocation.getWorld(), 6, 19, 1));
        locs.add(new Location(blockLocation.getWorld(), 6, 20, 1));
        locs.add(new Location(blockLocation.getWorld(), 6, 18, -1));
        locs.add(new Location(blockLocation.getWorld(), 6, 19, -1));
        locs.add(new Location(blockLocation.getWorld(), 6, 20, -1));
        locs.add(new Location(blockLocation.getWorld(), 6, 21, 0));
        locs.add(new Location(blockLocation.getWorld(), 0, 18, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 19, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 20, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 21, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 22, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 23, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 24, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 25, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 26, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 27, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 29, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 30, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 31, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 32, 1));
        locs.add(new Location(blockLocation.getWorld(), 0, 18, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 19, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 20, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 21, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 22, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 23, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 24, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 25, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 26, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 27, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 29, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 30, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 31, -1));
        locs.add(new Location(blockLocation.getWorld(), 0, 32, -1));
        locs.add(new Location(blockLocation.getWorld(), 1, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 21, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 22, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 23, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 24, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 25, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 26, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 21, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 22, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 23, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 24, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 25, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 26, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), -7, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), -5, 18, 0));
        locs.add(new Location(blockLocation.getWorld(), -6, 18, 1));
        locs.add(new Location(blockLocation.getWorld(), -6, 18, -1));
        locs.add(new Location(blockLocation.getWorld(), -7, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), -5, 19, 0));
        locs.add(new Location(blockLocation.getWorld(), -6, 19, 1));
        locs.add(new Location(blockLocation.getWorld(), -6, 19, -1));
        locs.add(new Location(blockLocation.getWorld(), -7, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), -5, 20, 0));
        locs.add(new Location(blockLocation.getWorld(), -6, 20, 1));
        locs.add(new Location(blockLocation.getWorld(), -6, 20, -1));
        locs.add(new Location(blockLocation.getWorld(), -6, 21, 0));
        locs.add(new Location(blockLocation.getWorld(), -2, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), -3, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), 2, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), 3, 27, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), 2, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), 3, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), 1, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), 2, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), 3, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), -1, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), -2, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), -3, 28, -1));
        locs.add(new Location(blockLocation.getWorld(), -1, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), -2, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), -3, 28, 1));
        locs.add(new Location(blockLocation.getWorld(), -4, 28, 0));
        locs.add(new Location(blockLocation.getWorld(), 4, 28, 0));
        locs.add(new Location(blockLocation.getWorld(), 3, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), 2, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), -3, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), -2, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 29, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 30, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 31, 0));
        locs.add(new Location(blockLocation.getWorld(), 1, 32, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 30, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 31, 0));
        locs.add(new Location(blockLocation.getWorld(), -1, 32, 0));
        locs.add(new Location(blockLocation.getWorld(), 0, 33, 0));

        for (Location lc : locs) {
            Location finalLoc = new Location(lc.getWorld(), blockLocation.getX() + lc.getX(), blockLocation.getY() + lc.getY(), blockLocation.getZ() + lc.getZ());
//            Bukkit.getLogger().info("I'm gonna spawn a block at coords: " + finalLoc.getX() + ", " + finalLoc.getY() + ", " + finalLoc.getZ());
            FallingBlock fb = finalLoc.getWorld().spawnFallingBlock(finalLoc, Material.getMaterial("STAINED_CLAY"), (byte) 14);
            fb.setDropItem(false);
            // get block at location and make it air
//            lc.getBlock().setType(Material.AIR);
            // pick a random decimal number between 0.0 and 2.0
//            double random = Math.random() * 2;
//            fb.setVelocity(new Vector(0, -10, 0));
            // set velocityspeed slower


            // make block fall

            // remove falling block after 5 seconds
//            if (fb.isOnGround()) {
//                Bukkit.getConsoleSender().sendMessage("Removing block at coords: "  + fb.getLocation().getX() + ", " + fb.getLocation().getY() + ", " + fb.getLocation().getZ());
//                fb.remove();
//            }
        }









    }

    @Override
    public BreakResult onBreak(BlockBreakEvent event) {
//        if (!instance.getExplosiveStatus(event.getPlayer().getUniqueId())) return null;
        Optional<ProtectedRegion> opt = container.get(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation()).getRegions().stream().filter(region->region.getFlag(DefaultFlag.BLOCK_BREAK)==StateFlag.State.ALLOW&&!region.getId().equals("__global__") && !region.getId().equals("mine-event")).findFirst();
        if (!opt.isPresent()) return null;

        int lvl = EnchantManager.getInst().getEnchantLevel(event.getPlayer().getInventory().getItemInMainHand(), getId());
        if (!EnchantUtil.chance(max, lvl, maxChance)) return null;
        Bukkit.getLogger().warning("User " + event.getPlayer().getName() + " has broken a block with the splash enchantment, this should not be possible!");
        // get blocks from mine
        Block b = event.getBlock();
//        l = event.getBlock().getLocation();
//        spawnAnvil(l);
//        start(2);
//
//        Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
//            public void run() {
//                stop();
//            }
//        }, 20);



        // get Y min location from region
//        int xMin = opt.get().getMinimumPoint().getBlockX();
//        // get Y max location from region
//        int xMax = opt.get().getMaximumPoint().getBlockX();
//        // get all coords in between and spawn falling block
//        int zMin = opt.get().getMinimumPoint().getBlockZ();
//        int zMax = opt.get().getMaximumPoint().getBlockZ();

        // create a sphere and get all of the coords inside of it
        // then spawn falling blocks at each of those coords
//        // get the radius of the sphere
//        int radius = 7;
//        // get the center of the sphere
//
//        // create a sphere of 1 block high
//        // get all of the coords inside of the sphere
//        List<Location> sphereCoords = StreamSupport.stream(new SphereRegionFactory().createCenteredAt(new com.sk89q.worldedit.Vector(b.getX(), b.getY(), b.getZ()), radius).spliterator(), false).map(vector -> BukkitUtil.toLocation(b.getWorld(), vector)).collect(Collectors.toList()).stream().filter(loc -> loc.getY() == b.getY()).collect(Collectors.toList());
//        // get only outer blocks of sphere
////        List<Location> outerSphereCoords = StreamSupport.stream(new SphereRegionFactory().createCenteredAt(new com.sk89q.worldedit.Vector(b.getX(), b.getY(), b.getZ()), radius).spliterator(), false).map(vector -> BukkitUtil.toLocation(b.getWorld(), vector)).collect(Collectors.toList()).stream().filter(loc -> loc.getY() == b.getY() && !sphereCoords.contains(loc)).collect(Collectors.toList());
//
//        Location centerLoc = new Location(b.getWorld(), b.getX() + 1, b.getY(), b.getZ());
//        // spawn falling blocks at each of the coords
//        for (Location loc : sphereCoords) {
//            // add 1 block on Y
////            float x = -3.0F + (float)(Math.random() * 3.0D);
////            float y = -2.0F + (float)(Math.random() * 3.0D);
////            float z = -3.0F + (float)(Math.random() * 3.0D);
//            Location finalLoc = new Location(loc.getWorld(), b.getX(), b.getY() + 1, b.getZ());
//            FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
//            fb.setDropItem(false);
//            // get block at location and make it air
//            finalLoc.getBlock().setType(Material.AIR);
//            // pick a random decimal number between 0.0 and 2.0
//            double random = Math.random() * 2;
//            fb.setVelocity(new Vector(10, random, 10));
//
//            // remove falling block after 5 seconds
//            if (fb.isOnGround()) {
//                Bukkit.getConsoleSender().sendMessage("Removing block at coords: "  + fb.getLocation().getX() + ", " + fb.getLocation().getY() + ", " + fb.getLocation().getZ());
//                fb.remove();
//            }
////            Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
////                public void run() {
////                    Bukkit.getLogger().warning("Removing falling block");
////                    // remove fallingblock
////                   fb.remove();
////                }
////            }, 20); // 3 seconds
//        }
//        for (int boord = zMin; boord <= zMax; boord++){
//            // wait 0.5 seconds before spawning the next row
//            int finalBoord = boord;
////            Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
////                public void run() {
//                    Bukkit.getLogger().info("Spawning falling block");
//                    spawnFallingBlocks(b, xMin, xMax, finalBoord);
////                }
////            }, 10);
//
//        }

//        FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
//        fb.setDropItem(false);
//        fb.setVelocity(new Vector(0, 1, 0));
//        Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
//            public void run() {
//                fb.remove();
//            }
//        }, 5000);
//        if (b.getType() != Material.TNT)
//            b.setType(Material.AIR);

//        float x = -3.0F + (float)(Math.random() * 7.0D);
//        float y = -2.0F + (float)(Math.random() * 5.0D);
//        float z = -3.0F + (float)(Math.random() * 7.0D);
//        FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
//        fb.setDropItem(false);
//        fb.setVelocity(new Vector(x, y, z));
//        Bukkit.getScheduler().runTaskLater(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), new Runnable() {
//            public void run() {
//                fb.remove();
//            }
//        }, 10);
//        if (b.getType() != Material.TNT)
//            b.setType(Material.AIR);

        ArrayList<Material> blocks = new ArrayList<>();
        blocks.add(b.getType());
        return new BreakResult(blocks, 0);
    }


    Runnable timer = new Runnable() {
        @SuppressWarnings("deprecation")
        public void run() {
            for(Location loc:getCircle(l,rad,(rad*((int)(Math.PI*2))))){
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, loc.getBlock().getType(),loc.getBlock().getData());
                fb.setVelocity(new Vector(0,.3,0));
                loc.getBlock().setType(Material.AIR);
            }
            rad++;
            rad=(((rad%20)==0)?1:rad);
        }
    };

    private Location l;
    private int rad = 1;
    private int id;


    /**
     * Return A List Of Locations That
     * Make Up A Circle Using A Provided
     * Center, Radius, And Desired Points.
     *
     * @param center
     * @param radius
     * @param amount
     * @return
     */
    private ArrayList<Location> getCircle(Location center, double radius, int amount){
        World world = center.getWorld();
        double increment = ((2 * Math.PI) / amount);
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++){
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    /**
     * Starts The Timer
     *
     * @param delay
     *
     *
     */
    private void start(int delay){
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PlasmaPrisonCore.getPlugin(PlasmaPrisonCore.class), timer, delay, delay);
    }

    /**
     * Stops The Timer
     *
     *
     */
    protected void stop(){
        Bukkit.getScheduler().cancelTask(id);
    }

}
