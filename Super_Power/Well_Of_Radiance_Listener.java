package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Summon;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;


import java.util.List;

public class Well_Of_Radiance_Listener implements Listener {
    private static List<Entity> mobs;
    //不准取下盔甲架道具
    @EventHandler
    public void No_Item_Take_From_Stand(PlayerArmorStandManipulateEvent easme){
        Player p=easme.getPlayer();
        Entity armor_stand=easme.getRightClicked();
        if(armor_stand.getScoreboardTags().contains("Well_Of_Radiance_ArmorStand")){
            easme.setCancelled(true);
        }
    }


    //回血
    public static void Recovery(Entity Armor_Stand){
        //double Health=Health_Per_Second/20.0D;
        double Health=0.5D;
        Armor_Stand.getWorld().playSound(Armor_Stand.getLocation(), Sound.BLOCK_FIRE_AMBIENT,3,1);
        Armor_Stand.getWorld().playSound(Armor_Stand.getLocation(), Sound.BLOCK_ANVIL_LAND,3,2);
        //回血
        BukkitRunnable Recovery = new BukkitRunnable() {
            int times = 600;//DestinyMain.W.getStand_Time();
            final double Radius=5;
            @Override
            public void run() {
                mobs = Armor_Stand.getNearbyEntities(5.0f, 5.0f, 5.0f);
                if (times >= 0) {
                    for (Entity e : mobs) {
                        if (e instanceof LivingEntity le&&!le.isDead()
                                &&le.getLocation().distance(Armor_Stand.getLocation())<=Radius) {
                            if(Armor_Stand.getScoreboardTags().contains(e.getUniqueId().toString())){
                                le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,10,1,false));
                                double MaxHealth=le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                                le.setHealth(Math.min(le.getHealth() + Health, MaxHealth));
                            }
                        }
                    }
                } else if (Armor_Stand.isDead()||times<=0) {
                    Armor_Stand.remove();
                    this.cancel();
                    return;
                }
                if(times%20==0){
                    //Summon.Summon_REDSTONE_Particle(Armor_Stand.getWorld(),Armor_Stand.getLocation(),5,16,true,2);
                    int[] rgb={255,215,0};
                    Summon.summon_Redstone_Particle(Armor_Stand.getWorld(),Armor_Stand.getLocation(),5,16,2,false,rgb);
                }
                times--;
            }
        };
        Recovery.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }
}
