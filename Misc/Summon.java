package Destiny2.Misc;

import Destiny2.Super_Power.Nova_Bomb_Listener;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class Summon {

    static public void Summon(Player p,Particle particle,double radius,int amount) {
        Location location=p.getLocation();
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        World world=p.getWorld();
        //计算粒子位置
        for (int i = 0; i < amount; i++) {
            double angle = 2 * Math.PI * i / amount;
            double x = centerX + radius * Math.sin(angle);
            double z = centerZ + radius * Math.cos(angle);
            Location particleLocation = new Location(world, x, centerY, z);

            // 如果粒子附近有方块，则向上移动
            Block block = particleLocation.getBlock();
            if (!block.isEmpty()) {
                particleLocation.add(0, 1, 0);
            }

            // 生成粒子
            p.spawnParticle(particle, particleLocation, 1,0,0,0);
        }
    }
    static public void Summon(World world,Location location,Particle particle,double radius,int amount) {
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        //计算粒子位置
        for (int i = 0; i < amount; i++) {
            double angle = 2 * Math.PI * i / amount;
            double x = centerX + radius * Math.sin(angle);
            double z = centerZ + radius * Math.cos(angle);
            Location particleLocation = new Location(world, x, centerY, z);

            // 如果粒子附近有方块，则向上移动
            Block block = particleLocation.getBlock();
            if (!block.isEmpty()) {
                particleLocation.add(0, 1, 0);
            }

            // 生成粒子
            world.spawnParticle(particle, particleLocation, 1,0,0,0);
        }
    }

    static public void summon_Redstone_Particle(Location location, double radius, int amount, float size, boolean useRandomColor, int[] rgb) {
        if(!useRandomColor&&rgb==null){
            return;
        }
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        // 计算粒子位置
        for (int i = 0; i < amount; i++) {
            double angle = 2 * Math.PI * i / amount;
            double x = centerX + radius * Math.sin(angle);
            double z = centerZ + radius * Math.cos(angle);
            Location particleLocation = new Location(location.getWorld(), x, centerY, z);

            // 如果粒子附近有方块，则向上移动
            Block block = particleLocation.getBlock();
            if (!block.isEmpty()) {
                particleLocation.add(0, 1, 0);
            }
            if (useRandomColor) {
                Random r = new Random();
                int[] randomRgb = {r.nextInt(256), r.nextInt(256), r.nextInt(256)};
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.fromRGB(randomRgb[0], randomRgb[1], randomRgb[2]), size));
            } else {
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), size));
            }
        }
    }
    static public void summon_Redstone_Particle(Player player, Location location, int amount, float size, boolean useRandomColor, int[] rgb) {
        if(!useRandomColor&&rgb==null){
            return;
        }
        Location particleLocation = new Location(location.getWorld(), location.getX(),location.getY(),location.getZ());

        // 如果粒子附近有方块，则向上移动
        Block block = particleLocation.getBlock();
        if (!block.isEmpty()) {
            particleLocation.add(0, 1, 0);
        }
        player.spawnParticle(Particle.REDSTONE, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), size));
    }


    static public void summon_Lightning(Location location, double Range, int Amount) {
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        // 计算粒子位置
        for (int i = 0; i < Amount; i++) {
            double angle = 2 * Math.PI * i / Amount;
            double x = centerX + Range * Math.sin(angle);
            double z = centerZ + Range * Math.cos(angle);
            Location NewLocation = new Location(location.getWorld(), x, centerY, z);
            location.getWorld().strikeLightningEffect(NewLocation);
        }
    }

//    public static void Nova_Small_Bomb(Location location ,Player p) {
//        double centerX = location.getX();
//        double centerY = location.getY();
//        double centerZ = location.getZ();
//        World world=location.getWorld();
//        int amount=6;
//        double radius=2.0f;
//        double[] Track_Radius={5.00,5.00,5.00};
//        Vector[] vectors=new Vector[6];
//        Entity e;
//        //计算位置
//        for (int i = 0; i < amount; i++) {
//            double angle = 2 * Math.PI * i / amount;
//            double x = centerX + radius * Math.sin(angle);
//            double z = centerZ + radius * Math.cos(angle);
//            Location newLocation = new Location(world, x, centerY+10, z);
//            vectors[i]=newLocation.toVector().subtract(location.toVector()).multiply(0.1).normalize().setY(0.5);
//        }
//        for(Vector v:vectors){
//            e=world.spawnEntity(location,EntityType.SHULKER_BULLET);
//            ((ShulkerBullet)e).setShooter(p);
//            e.setVelocity(v);
//            e.addScoreboardTag("Nova_Bomb");
//            e.addScoreboardTag("Nova_Bomb_Shulker_Bullet");
//            e.addScoreboardTag(p.getUniqueId().toString());
//            Misc.Track(p,e,5,5,5,200,10);
//            Misc.aTrack(p,Track_Radius,200,10,0,0,false,true,0);
//        }
//    }

    public static void Nova_Small_Bomb(Location location ,Snowball sb) {
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        World world=location.getWorld();
        int amount=6;
        double radius=2.0f;
        double[] Track_Radius={5.00,5.00,5.00};
        Vector[] vectors=new Vector[6];
        ShulkerBullet e;
        //计算位置
        for (int i = 0; i < amount; i++) {
            double angle = 2 * Math.PI * i / amount;
            double x = centerX + radius * Math.sin(angle);
            double z = centerZ + radius * Math.cos(angle);
            Location newLocation = new Location(world, x, centerY+10, z);
            vectors[i]=newLocation.toVector().subtract(location.toVector()).multiply(0.1).normalize().setY(0.5);
        }
        for(Vector v:vectors){
            e=(ShulkerBullet)world.spawnEntity(location,EntityType.SHULKER_BULLET);
            e.setShooter(sb.getShooter());
            e.setVelocity(v);
            e.addScoreboardTag("No_Track");
//            Misc.Track(p,e,5,5,5,200,10);
            Nova_Bomb_Listener.Cataclysm_Small_Bomb.add(e);
            Misc.aTrack(e,Track_Radius,200,2,0,0,false,true,1);
        }
    }

}
