package Destiny2.Misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffectType;


public class Damage_Listener implements Listener {
    //FLY_INTO_WALL撞击伤害
    //FALL摔落伤害
    final private EntityDamageEvent.DamageCause FLY_INTO_WALL= EntityDamageEvent.DamageCause.FLY_INTO_WALL;
    final private EntityDamageEvent.DamageCause FALL= EntityDamageEvent.DamageCause.FALL;
    final private EntityDamageEvent.DamageCause EEXPLODE= EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    final private EntityDamageEvent.DamageCause BEXPLODE= EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
    final private EntityDamageEvent.DamageCause PROJECTILE= EntityDamageEvent.DamageCause.PROJECTILE;
    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbe){
        Entity e=edbe.getEntity();
        Entity damager=edbe.getDamager();
        //雷击不对自身造成伤害
//        if(damager.getScoreboardTags().contains(e.getUniqueId().toString())){
//            edbe.setCancelled(true);
//        }
        if(damager instanceof LightningStrike ls&&ls.getScoreboardTags().contains("Thunder_Crash_Lightning_Damage")
                &&e.getScoreboardTags().contains(e.getUniqueId().toString())){
            edbe.setCancelled(true);
        }

        if(damager.getScoreboardTags().contains("No_Fire")&&e.getFireTicks()!=0){
            e.setFireTicks(0);
        }
        //被虚弱的增伤(*30%)
        if(e.getScoreboardTags().contains("Shadow_Shot_Weak")){
            edbe.setDamage(edbe.getDamage()*1.30f);
        }
        if(damager instanceof ShulkerBullet&& e instanceof LivingEntity
                &&damager.getScoreboardTags().contains("Nova_Bomb")){
            Misc.Reduce_PotionEffect((LivingEntity) e,PotionEffectType.LEVITATION,1,200);
        }
    }
    //无摔落伤害,自爆炸伤害
    @EventHandler
    public void No_Damage(EntityDamageEvent e){
        //鞘翅撞墙,摔落伤害
        if(e.getEntityType()==EntityType.PLAYER&&(e.getCause()== FLY_INTO_WALL||e.getCause()==FALL)){
            Player p=(Player) e.getEntity();
            if(p.getScoreboardTags().contains("No_Fall_Damage")){
                p.getScoreboardTags().remove("No_Fall_Damage");
                e.setCancelled(true);
            }
        }
        //禁止爆炸炸坏新星
        if(e.getCause()== EEXPLODE||e.getCause()==BEXPLODE){
            Entity E= e.getEntity();
            //新星防炸
            if((E instanceof EnderCrystal||E instanceof Snowball||E instanceof ShulkerBullet)
                    &&E.getScoreboardTags().contains("Nova_Bomb")){
                e.setCancelled(true);
            }
        }

    }
    //爆炸不产生破坏
    @EventHandler
    public void No_Explode(EntityExplodeEvent e){
        Entity E= e.getEntity();
        if(E.getScoreboardTags().contains("Nova_Bomb")){
            e.blockList().clear();
        }
    }
}
