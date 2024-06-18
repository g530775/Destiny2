package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
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

import java.util.LinkedList;


public class Spectral_Blades_Listener implements Listener {
    public static LinkedList<Player> Spectral_Blades_User=new LinkedList<>();

    public final double Spectral_Blades_Damage=25.00;
    public final int Invisible_Normal_Time_Ticks=140;
    public final int Invisible_Time_Max_Ticks=240;
    public final int Invisible_Time_Plus_Ticks=100;
    //伤害事件
    @EventHandler
    public void Damage_Listener(EntityDamageByEntityEvent edbey){
        if(edbey.getEntity() instanceof LivingEntity&&edbey.getDamager() instanceof Player p
                &&Spectral_Blades_User.contains(p)){
            Misc.Reduce_DAMAGE_RESISTANCE_PotionEffect(p,20);
            edbey.setDamage(Spectral_Blades_Damage);
        }
    }
    //击杀延长隐身
    @EventHandler
    public void Kill(EntityDeathEvent ede){
        Player p;
        p=ede.getEntity().getKiller();
        if(p!=null&&Spectral_Blades_User.contains(p)){
            if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                if (p.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() >= Invisible_Normal_Time_Ticks) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Invisible_Time_Max_Ticks, 4, false));
                } else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, p.getPotionEffect(PotionEffectType.INVISIBILITY).getDuration() + Invisible_Time_Plus_Ticks, 4, false));
                }
            }else{
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Invisible_Time_Plus_Ticks, 4, false));
            }
        }
    }
}
