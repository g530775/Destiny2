package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Blade_Barrage_Listener implements Listener {
    public static final int Per_Arrows=6;
    public static final double[] Track_Radius={3.00,3.00,3.00};
    public static final double Explode_Damage=35.00;

    //»÷ÖÐÉËº¦
    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbee){
        if(edbee.getEntity() instanceof LivingEntity le
                &&edbee.getDamager() instanceof Arrow ar
                &&ar.getScoreboardTags().contains("Blade_Barrage")){
            if(edbee.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
                Misc.Damage(le,Explode_Damage,(Entity) ar.getShooter());
            }
        }
    }

    //»÷ÖÐ±¬Õ¨
    @EventHandler
    public void Hit_Ground(ProjectileHitEvent phe){
        if(phe.getEntity() instanceof Arrow ar&&ar.getScoreboardTags().contains("Blade_Barrage")){
            BukkitRunnable explode=new BukkitRunnable() {
                @Override
                public void run() {
                    LivingEntity le=(LivingEntity) phe.getHitEntity();
                    Block block=phe.getHitBlock();
                    if(le!=null){
                        Misc.Make_Explode(ar,le,1.5f);
                        le.setVelocity(le.getVelocity().setY(le.getVelocity().getY()*0.2));
                    }else{
                        Misc.Make_Explode(ar,block.getLocation(),1.5f);
                    }
                    ar.remove();

                }
            };
            explode.runTaskLater(DestinyMain.getPlugin(DestinyMain.class),20);
            if(phe.getHitEntity() instanceof LivingEntity le){
//                Misc.Damage(le,0.5f,ar);
                ar.setDamage(0.5f);
                le.setNoDamageTicks(0);
            }
        }
    }
}
