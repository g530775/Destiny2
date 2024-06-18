package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Summon;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.scheduler.BukkitTask;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Well_Of_Radiance_Listener implements Listener {
    public static LinkedList<ArmorStand> ArmorStands=new LinkedList<>();
    public static Map<ArmorStand, List<Entity>> Well_Of=new HashMap<>();
    public static Map<ArmorStand,Double> Damage=new HashMap<>();
    public static Map<ArmorStand,Player> Well_Of_Radiance_Owner=new HashMap<>();
    public static Map<ArmorStand, BukkitTask>bk=new HashMap<>();
    public static final double Well_Of_Radiance_Radius=6.0;
    static final double maxHealth=150.00;


    private void clean(ArmorStand as){
        ArmorStands.remove(as);
        Well_Of.remove(as);
        Damage.remove(as);
        Well_Of_Radiance_Owner.remove(as);

    }

    public static void List_Clean(){
        ArmorStands.clear();
        Well_Of.clear();
        Damage.clear();
        Well_Of_Radiance_Owner.clear();
        bk.clear();

    }
    //不准取下盔甲架道具
    @EventHandler
    public void No_Item_Take_From_Stand(PlayerArmorStandManipulateEvent easme){
        Player p=easme.getPlayer();
        ArmorStand armor_stand=easme.getRightClicked();
        if(Well_Of_Radiance_Listener.ArmorStands.contains(armor_stand)){
            easme.setCancelled(true);
        }
    }

    //盔甲架伤害
    @EventHandler
    public void Armor_Stand_Damage(EntityDamageEvent ede){

        if(ede.getEntity() instanceof ArmorStand as&&ArmorStands.contains(as)&&ede.getCause()!= EntityDamageEvent.DamageCause.FALL){
            Damage.put(as,Damage.get(as)+ede.getDamage());
            if(Damage.get(as)>150.00){
                Damage.put(as,150.00);
                as.getWorld().playSound(as.getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,1,1);
                clean(as);
                as.remove();
                bk.get(as).cancel();
                bk.remove(as);
            }
        }
    }


    //回血
    public static void Recovery(ArmorStand Armor_Stand){
        //double Health=Health_Per_Second/20.0D;
        double Health=0.5D;
        Armor_Stand.getWorld().playSound(Armor_Stand.getLocation(), Sound.BLOCK_FIRE_AMBIENT,3,1);
        Armor_Stand.getWorld().playSound(Armor_Stand.getLocation(), Sound.BLOCK_ANVIL_LAND,3,2);
        Damage.put(Armor_Stand,0.00);

        //回血
        BukkitRunnable Recovery = new BukkitRunnable() {
            int times = 600;//DestinyMain.W.getStand_Time();
            int[] rgb={0,255,0};
            @Override
            public void run() {
                Well_Of.put(Armor_Stand,Armor_Stand.getNearbyEntities(Well_Of_Radiance_Radius, Well_Of_Radiance_Radius, Well_Of_Radiance_Radius));
                if (times >= 0) {
                    for (Entity e : Well_Of.get(Armor_Stand)) {
                        if (e instanceof LivingEntity le&&!le.isDead()
                                &&le.getLocation().distance(Armor_Stand.getLocation())<=Well_Of_Radiance_Radius) {
                            if(Well_Of_Radiance_Owner.get(Armor_Stand).getUniqueId().equals(le.getUniqueId())){
                                double MaxHealth=le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                                if(le.hasPotionEffect(PotionEffectType.HEALTH_BOOST)){
                                    int Level=le.getPotionEffect(PotionEffectType.HEALTH_BOOST).getAmplifier()+1;
                                    MaxHealth=le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+Level*4.00;
                                }
                                le.setHealth(Math.min(le.getHealth() + Health, MaxHealth));
                                le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,10,1,false));

                            }
                        }
                    }
                }else{
                    bk.get(Armor_Stand).cancel();
                    bk.remove(Armor_Stand);
                    Armor_Stand.remove();
                    return;
                }
                if(times%40==0){
                    //Summon.Summon_REDSTONE_Particle(Armor_Stand.getWorld(),Armor_Stand.getLocation(),5,16,true,2);
                    int[] rgb={255,215,0};
                    Summon.summon_Redstone_Particle(Armor_Stand.getLocation(),Well_Of_Radiance_Radius,16,2,false,rgb);
                }
                if(times%10==0){
                    rgb[0]=(int)((Damage.get(Armor_Stand)/maxHealth* 255)>=255?255:Damage.get(Armor_Stand)/maxHealth* 255);
                    rgb[1]=255-(int)((Damage.get(Armor_Stand)/maxHealth* 255)>=255?255:Damage.get(Armor_Stand)/maxHealth* 255);
                    Summon.summon_Redstone_Particle(Well_Of_Radiance_Owner.get(Armor_Stand),Armor_Stand.getLocation().add(0,Armor_Stand.getEyeHeight(),0),1,5,false,rgb);
                }
                times--;
            }
        };
        //Recovery.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
        bk.put(Armor_Stand,Recovery.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1));
    }
}
