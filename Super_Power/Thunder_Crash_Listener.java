package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Summon;
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


public class Thunder_Crash_Listener implements Listener {
    @EventHandler
    public void Thunder_Crash(EntityToggleGlideEvent etge){
        if(etge.getEntity() instanceof Player player){
            if(player.getScoreboardTags().contains("Thunder_Crash")&&player.isGliding()){
                BukkitRunnable Hit= new BukkitRunnable() {
                    int time=220;
                    double Radius=5;
                    Location location;
                    boolean isLightning=false;
                    @Override
                    public void run() {
                        if(!player.isGliding()&&!isLightning){
                            player.getWorld().createExplosion(player.getLocation(),4.5f,false,false,player);
                            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            player.getScoreboardTags().remove("Thunder_Crash");
                            player.getInventory().setChestplate(new ItemStack(Material.AIR));
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
                            Summon.summon_Redstone_Particle(location.getWorld(),location,5,40,2,false,rgb);
                        }
                        if(time<0){
                            player.getEquipment().setChestplate(new ItemStack(Material.AIR));
                            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            }
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
