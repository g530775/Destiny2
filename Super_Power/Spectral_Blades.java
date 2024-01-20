package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Spectral_Blades implements Listener {
    //伤害事件
    @EventHandler
    public void Damage_Listener(EntityDamageByEntityEvent edbey){
        Entity e=edbey.getEntity();
        Entity d=edbey.getDamager();
        if(d.getScoreboardTags().contains("Spectral_Blades_Using")&&d instanceof Player){
            LivingEntity le=(LivingEntity) d;
            Destiny2.Misc.Misc.Reduce_DAMAGE_RESISTANCE_PotionEffect(le,20);
            edbey.setDamage(25);
        }
    }
    //击杀延长隐身
    @EventHandler
    public void Kill(EntityDeathEvent ede){
        LivingEntity le=ede.getEntity();
        Player p=le.getKiller();
        if(p==null){
            return;
        }
        if(p.getScoreboardTags().contains("Spectral_Blades_Using")) {
            if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                if (p.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() >= 140) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 240, 4, false));
                } else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, p.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() + 100, 4, false));
                }
            }else{
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 4, false));
            }

        }
    }
}
