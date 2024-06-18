package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Summon;
import Destiny2.Super_Power_Chooser;
import Destiny2.Variable_Construction.Equipments;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Map;


public class Thunder_Crash_Listener implements Listener {
    public static LinkedList<Player> Thunder_Crash_User=new LinkedList<>();
    Map<Player, Equipments> Player_Equipment=DestinyMain.Player_Equipment;
    @EventHandler
    public void Thunder_Crash(EntityToggleGlideEvent etge){
        if(etge.getEntity() instanceof Player player){
            if(Thunder_Crash_User.contains(player)){
                BukkitRunnable Hit= new BukkitRunnable() {
                    int time=220;
                    final double Radius=5;
                    Location location;
                    boolean isLightning=false;
                    @Override
                    public void run() {
                        if(!player.isGliding()&&!isLightning){
                            player.getWorld().createExplosion(player.getLocation(),4.5f,false,false,player);
                            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            }
                            Super_Power_Chooser.remove_User_List(player);
                            Thunder_Crash_User.remove(player);
                            if(!Super_Power_Chooser.task.get(player).isCancelled()){
                                Super_Power_Chooser.task.get(player).cancel();
                            }
                            player.getInventory().setChestplate(Player_Equipment.get(player).getChest());
                            Player_Equipment.remove(player);
                            isLightning=true;
                            location=player.getLocation();
                        }else if(isLightning&&location!=null){
                            if(time%40==0){
                                location.getWorld().strikeLightningEffect(location);
                                //使用效果
                                for (Entity ee : location.getWorld().getNearbyEntities(location,5.0f,5.0f,5.0f)) {
                                    if (ee instanceof LivingEntity le&&le.getUniqueId()!=player.getUniqueId()
                                            &&le.getLocation().distance(location)<=Radius) {
                                        le.damage(25-(le.getLocation().distanceSquared(location)/1.5),player);
                                        le.setNoDamageTicks(0);
                                    }
                                }
                            }
                        }
                        if(time%30==0){
                            int[] rgb={0,245,255};
                            Summon.summon_Redstone_Particle(location,5,40,2,false,rgb);
                        }
                        if(time<0){
                            this.cancel();
                            return;
                        }
                        time--;
                    }
                };
                Hit.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
            }
        }
    }
}
