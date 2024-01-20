package Destiny2.Misc;

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

    static public void summon_Redstone_Particle(World world, Location location, double radius, int amount, float size, boolean useRandomColor, int[] rgb) {
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
            Location particleLocation = new Location(world, x, centerY, z);

            // 如果粒子附近有方块，则向上移动
            Block block = particleLocation.getBlock();
            if (!block.isEmpty()) {
                particleLocation.add(0, 1, 0);
            }
            if (useRandomColor) {
                Random r = new Random();
                int[] randomRgb = {r.nextInt(256), r.nextInt(256), r.nextInt(256)};
                world.spawnParticle(Particle.REDSTONE, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.fromRGB(randomRgb[0], randomRgb[1], randomRgb[2]), size));
            } else {
                world.spawnParticle(Particle.REDSTONE, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, new Particle.DustOptions(Color.fromRGB(rgb[0], rgb[1], rgb[2]), size));
            }
        }
    }

    static public void Nova_Small_Bomb(Location location ,Player p) {
        double centerX = location.getX();
        double centerY = location.getY();
        double centerZ = location.getZ();
        World world=location.getWorld();
        int amount=6;
        double radius=2.0f;
        Vector[] vectors=new Vector[6];
        Entity e;
        //计算位置
        for (int i = 0; i < amount; i++) {
            double angle = 2 * Math.PI * i / amount;
            double x = centerX + radius * Math.sin(angle);
            double z = centerZ + radius * Math.cos(angle);
            Location newLocation = new Location(world, x, centerY+10, z);
            vectors[i]=newLocation.toVector().subtract(location.toVector()).multiply(0.1).normalize().setY(0.5);
        }
        for(Vector v:vectors){
            e=world.spawnEntity(location,EntityType.SHULKER_BULLET);
            ((ShulkerBullet)e).setShooter(p);
            e.setVelocity(v);
            e.addScoreboardTag("Nova_Bomb");
            e.addScoreboardTag("Nova_Bomb_Shulker_Bullet");
            e.addScoreboardTag(p.getUniqueId().toString());
            Misc.Track(p,e,5,5,5,200,10);
        }
    }


}
