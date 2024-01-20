package Destiny2.Misc;

import Destiny2.DestinyMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Misc {
    public static void Reduce_DAMAGE_RESISTANCE_PotionEffect(LivingEntity le,int reduce_ticks){
        if(le.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
            if(le.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration()>=reduce_ticks){
                int time=le.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration();
                le.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,time-reduce_ticks,2,false));
            }else{
                le.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
        }
    }
    public static void Reduce_PotionEffect(LivingEntity le,PotionEffectType pe,int Level,int reduce_ticks){
        if(le.hasPotionEffect(pe)){
            if(Level<=0&&Level!=-1){
                Level=1;
            }
            if(Level==-1){
                Level=le.getPotionEffect(pe).getAmplifier();
            }
            if(le.getPotionEffect(pe).getDuration()>reduce_ticks){
                int time=le.getPotionEffect(pe).getDuration();
                le.removePotionEffect(pe);
                le.addPotionEffect(new PotionEffect(pe,time-reduce_ticks,Level-1,false));
            }else{
                le.removePotionEffect(pe);
            }
        }
    }
    public static void Remove_Items(Player p){
        ItemStack[] item=p.getInventory().getStorageContents();
        for(ItemStack i:item){
            if(i==null){
                continue;
            }
            if(i.getEnchantmentLevel(Enchantment.BINDING_CURSE)==5){
                p.getInventory().remove(i);
            }
        }
    }
    static public LivingEntity NearlyTarget(Entity e, double x, double y, double z, boolean AA) {
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
        LivingEntity en = null;
        task:for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) {
                if(entity.isDead()||!AA&&e.getScoreboardTags().contains(entity.getUniqueId().toString())){
                    continue;
                }
                for(String s:entity.getScoreboardTags()){
                    if(DestinyMain.SpecialTag.contains(s)){
                        continue task;
                    }
                }
                en = (LivingEntity) entity;
            }
        }
        return en;
    }

    static public void Make_Explode(World world, Location loc, float range, Player player){
        world.createExplosion(loc,range,false,false,player);
    }
    static public void Make_Explode(World world, Location loc, float range, Entity entity){
        world.createExplosion(loc,range,false,false,entity);
    }

    static public void Track(Entity original_from,Entity ball,double range_x,double range_y,double range_z,int MaxTimeTick,int coolTimeTicks){
        double steer=0.05f;
        Vector Ball_v=ball.getVelocity();
        BukkitRunnable Track = new BukkitRunnable() {
            double x=range_x,y=range_y,z=range_z;
            LivingEntity Closely=null;
            int time=MaxTimeTick;
            int ct=coolTimeTicks;
            public void run() {
                if(ct>0){
                    ct--;
                    return;
                }
                if(Closely==null||Closely.isDead()){
                    if(ball.getScoreboardTags().contains("Nova_Bomb_Cataclysm")){
                        ball.setVelocity(Ball_v);
                    }
                    Closely=Destiny2.Misc.Misc.NearlyTarget(ball,x,y,z,false);
                }
                if(Closely!=null){
                    Location loc=Closely.getLocation().add(0,Closely.getEyeHeight()/2,0);
                    double speed=0.3;
                    Vector Aim = loc.toVector().subtract(ball.getLocation().toVector());
                    if(ball.getScoreboardTags().contains("Nova_Bomb_Shulker_Bullet")){
                        ball.setGravity(false);
                        if(ball.getLocation().getY()>Closely.getLocation().getY()+Closely.getEyeHeight()){
                            ball.setGravity(true);
                        }
                        double Y=Aim.subtract(ball.getVelocity()).normalize().getY();
                        Vector steering = Aim.subtract(ball.getVelocity()).normalize().multiply(steer);
                        Vector velocity = ball.getVelocity().normalize().multiply(speed).add(steering);
                        ball.setVelocity(velocity);
                    }
                    else{
                        Vector steering = Aim.subtract(ball.getVelocity()).normalize().multiply(steer);
                        Vector velocity = ball.getVelocity().normalize().multiply(speed).add(steering);
                        ball.setVelocity(velocity);
                    }
                }
                if(time<0){
                    if(ball.getScoreboardTags().contains("Nova_Bomb_Cataclysm")&&!ball.getPassengers().isEmpty()){
                        ball.getPassengers().get(0).remove();
                    }
                    ball.remove();
                    this.cancel();
                }
                time--;
            }
        };
        Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }

    static public ItemStack itemDamage(ItemStack is,int Number_or_1ofPercent,boolean isPercent){
        Damageable bd=(Damageable) is.getItemMeta();
        if(isPercent){
            bd.setDamage(bd.getDamage()+is.getType().getMaxDurability()/Number_or_1ofPercent);
        }else{
            bd.setDamage(bd.getDamage()+Number_or_1ofPercent);
        }
        is.setItemMeta((ItemMeta) bd);
        return is;
    }
}
