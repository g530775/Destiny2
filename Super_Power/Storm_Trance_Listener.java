package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Storm_Trance_Listener implements Listener {
    public static LinkedList<Player> Storm_Trance_User=new LinkedList<>();
    public static final int Storm_Lightning_Times=6;
    public static final double Storm_Lightning_Range=6.00;
    public static final float Storm_Explode_Range=8;
    public static final double Storm_Damage=8.00;
    public static final int Storm_Time=500;
    public static final double Storm_Explode_Damage=25.00;
    public static final double[] Storm_Attack_Range={8.00,4.00,8.00};


    public static boolean add(Player p){
        if(!Storm_Trance_User.contains(p)){
            Storm_Trance_User.add(p);
            return false;
        }
        return true;
    }

    public static void remove(Player p){
        Storm_Trance_User.remove(p);
        DestinyMain.Super_Power_User.remove(p);
    }



    //≥ı º…À∫¶
//    @EventHandler
//    public void Damage(EntityDamageByEntityEvent edbee){
//        if(edbee.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
//                &&edbee.getDamager() instanceof Player p&&Storm_Trance_User.contains(p)){
//            if(edbee.getEntity() instanceof LivingEntity le){
//                Misc.Damage(le,Storm_Explode_Damage,p);
//            }
//            edbee.setCancelled(true);
//        }
//    }


    //œÚ«∞¥´ÀÕ
    @EventHandler
    public void shift(PlayerToggleSneakEvent ptse){
        Player p=ptse.getPlayer();
        if(p.getEquipment().getItemInMainHand().getType()==Material.END_CRYSTAL){
            return;
        }
        if(Storm_Trance_User.contains(p)&&!p.hasCooldown(Material.ELYTRA)&&!p.isSneaking()){
            Location location=p.getLocation().add(p.getEyeLocation().getDirection().multiply(6));
            while(location.getBlock().getType()!=Material.AIR){
                location=location.subtract(p.getLocation().getDirection());
            }
            p.teleport(location);
            p.setCooldown(Material.ELYTRA,15);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        }
    }

    //π•ª˜
    @EventHandler
    public void Attack(PlayerInteractEvent pie){
        Action action=pie.getAction();
        Player p=pie.getPlayer();
        if(Storm_Trance_User.contains(p)&&!p.hasCooldown(Material.IRON_INGOT)
                &&(action==Action.RIGHT_CLICK_AIR||action==Action.LEFT_CLICK_AIR||action==Action.RIGHT_CLICK_BLOCK||action==Action.LEFT_CLICK_BLOCK)){
            pie.setCancelled(true);
            for(LivingEntity le:Misc.NearlyTargets(p,Storm_Attack_Range[0],Storm_Attack_Range[1],Storm_Attack_Range[2])){
                Vector v=p.getLocation().getDirection().normalize();
                Vector vs=(new Vector(le.getLocation().getX()-p.getLocation().getX(),0,le.getLocation().getZ()-p.getLocation().getZ())).normalize();
                if(vs.getX()*v.getX()+vs.getZ()*v.getZ()>0){
                    Misc.Damage(le,Storm_Damage,p);
                }
            }
            p.setCooldown(Material.IRON_INGOT,5);
        }
    }
    @EventHandler
    public void Attack(PlayerInteractEntityEvent piee){
        Player p=piee.getPlayer();
        if(Storm_Trance_User.contains(p)&&!p.hasCooldown(Material.IRON_INGOT)){
            piee.setCancelled(true);
            for(LivingEntity le:Misc.NearlyTargets(p,Storm_Attack_Range[0],Storm_Attack_Range[1],Storm_Attack_Range[2])){
                Vector v=p.getLocation().getDirection().normalize();
                Vector vs=(new Vector(le.getLocation().getX()-p.getLocation().getX(),0,le.getLocation().getZ()-p.getLocation().getZ())).normalize();
                if(vs.getX()*v.getX()+vs.getZ()*v.getZ()>0){
                    Misc.Damage(le,Storm_Damage,p);
                }
            }
            p.setCooldown(Material.IRON_INGOT,5);
        }
    }

    //À¿Õˆ¬‰¿◊
    @EventHandler
    public void Dead(EntityDeathEvent ded){
        Player player=ded.getEntity().getKiller();
        if(player!=null&&Storm_Trance_User.contains(player)){
            ded.getEntity().getWorld().strikeLightningEffect(ded.getEntity().getLocation());
        }
    }


}
