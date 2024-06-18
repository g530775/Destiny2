package Destiny2.Misc;

import Destiny2.DestinyMain;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.UUID;

public class Test_Listener implements Listener {
    //玩家跳跃禁止
//    private Set<UUID> prevPlayersOnGround = Sets.newHashSet();
//
//    @EventHandler
//    public void onMove(PlayerMoveEvent e) {
//        Player player = e.getPlayer();
//        if (player.getVelocity().getY() > 0) {
//            double jumpVelocity = (double) 0.42F;
//            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
//                if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
//                    player.sendMessage("Jumping!");
//                }
//            }
//        }
//        if (player.isOnGround()) {
//            prevPlayersOnGround.add(player.getUniqueId());
//        } else {
//            prevPlayersOnGround.remove(player.getUniqueId());
//        }
//    }

    //Armor_Stand_Pose
    @EventHandler
    public void test(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ArmorStand&&e.getDamager() instanceof Player){
            Entity E=e.getEntity();
            Player p=(Player) e.getDamager();
            ArmorStand as=(ArmorStand) E;
            if(p.getInventory().getItemInMainHand().getType()==Material.AIR){
                return;
            }
            if(E.getType()==EntityType.ARMOR_STAND&&E.getScoreboardTags().contains("Pose")&&!E.getScoreboardTags().contains("p")){
                ((ArmorStand) E).setArms(true);
                if(p.getInventory().getItemInOffHand().getType()!=Material.AIR){
                    ((ArmorStand) E).getEquipment().setItemInMainHand(p.getInventory().getItemInOffHand());
                    E.addScoreboardTag("p");
                }
            }

            if(E instanceof ArmorStand ass&&E.getScoreboardTags().contains("Pose")){
                e.setCancelled(true);
                switch (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()) {
//                    case "x" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0.01D, 0, 0));
//                    case "10x" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0.1D, 0, 0));
//                    case "y" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0, 0.01D, 0));
//                    case "10y" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0, 0.1D, 0));
//                    case "z" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0, 0, 0.01D));
//                    case "10z" -> ((ArmorStand) E).setRightArmPose(as.getRightArmPose().add(0, 0, 0.1D));
//                    case "remove" -> as.remove();
                    case "x" ->rotate(p.isSneaking(),ass,0.01,0,0);
                    case "10x" ->rotate(p.isSneaking(),ass,0.1,0,0);
                    case "y" -> rotate(p.isSneaking(),ass,0,0.01,0);
                    case "10y" -> rotate(p.isSneaking(),ass,0,0.1,0);
                    case "z" -> rotate(p.isSneaking(),ass,0,0,0.01);
                    case "10z" -> rotate(p.isSneaking(),ass,0,0,0.1);
                }
                p.sendMessage(as.getRightArmPose().getX()+"/"+as.getRightArmPose().getY()+"/"+as.getRightArmPose().getZ());
            }

            if(E.getType()==EntityType.ARMOR_STAND){
                e.setCancelled(true);
                switch (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "lock"-> E.setGravity(false);
                    case "unlock"-> E.setGravity(true);
                    case "up"->E.teleport(E.getLocation().add(0,0.5f,0));
                    case "down"->E.teleport(E.getLocation().subtract(0,0.5f,0));
                }
            }
        }
    }

    public void rotate(boolean sneaking,ArmorStand as,double x,double y,double z){
        if(sneaking){
            as.setRightArmPose(as.getRightArmPose().subtract(x,y,z));
        }else{
            as.setRightArmPose(as.getRightArmPose().add(x,y,z));
        }
    }






    public static Vector getBackVector(Location loc) {
        float newZ = (float)(loc.getZ() + 0.75D * Math.sin(Math.toRadians((loc.getYaw() + 90.0F))));
        float newX = (float)(loc.getX() + 0.75D * Math.cos(Math.toRadians((loc.getYaw() + 90.0F))));
        return new Vector(newX - loc.getX(), 0.0D, newZ - loc.getZ());
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static void active(Location location) {
        boolean[][] shape={
                {false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false }, {
                false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false }, {
                false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false }, {
                false, false, false, false, false, true , false, false, false, false,
                false, false, false, true , false, false, false, false, false, false }, {
                false, false, false, false, true , true , true , false, false, false,
                false, false, true , true , true , false, false, false, false, false }, {
                false, false, false, true , true , true , true , true , false, true ,
                false, true , true , true , true , true , false, false, false, false }, {
                false, false, true , true , true , true , true , true , true , true ,
                true , true , true , true , true , true , true , false, false, false }, {
                false, false, true , true , true , true , true , true , true , true ,
                true , true , true , true , true , true , true , false, false, false }, {
                false, true , false, false, true , true , true , true , true , true ,
                true , true , true , true , true , false, false, true , false, false }, {
                false, false, false, false, false, true , true , true , true , true ,
                true , true , true , true , false, false, false, false, false, false },
                {
                false, false, false, false, false, true , false, true , true , true ,
                true , true , false, true , false, false, false, false, false, false }, {
                false, false, false, false, true , false, false, false, true , false,
                true , false, false, false, true , false, false, false, false, false }, {
                false, false, false, false, false, false, false, false, true , false,
                true , false, false, false, false, false, false, false, false, false } };
        double space = 0.2D;
        double defX = location.getX() - space * (shape[0]).length / 2.0D + space;
        double x = defX;
        double y = location.clone().getY() + 2.8D;
        double angle = -((location.getYaw() + 180.0F) / 60.0F);
        angle += (location.getYaw() < -180.0F) ? 3.25D : 2.985D;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < (shape[i]).length; j++) {
                if (shape[i][j]) {
                    Location target = location.clone();
                    target.setX(x);
                    target.setY(y);
                    Vector v = target.toVector().subtract(location.toVector());
                    Vector v2 = getBackVector(location);
                    v = rotateAroundAxisY(v, angle);
                    double iT = i / 18.0D;
                    v2.setY(0).multiply(-0.2D - iT);
                    location.add(v);
                    location.add(v2);
                    for (int k = 0; k < 3; k++) {
                        location.getWorld().spawnParticle(Particle.FLAME,location,0,0,0,0);
                    }
                    location.subtract(v2);
                    location.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }




}
