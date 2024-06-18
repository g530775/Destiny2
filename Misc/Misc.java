package Destiny2.Misc;

import Destiny2.DestinyMain;
import Destiny2.Super_Power.Nova_Bomb_Listener;
import net.minecraft.server.v1_16_R3.EntityBlaze;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBlaze;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
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
//    static public LivingEntity NearlyTarget(Entity e, double x, double y, double z, boolean AA) {
//        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
//        LivingEntity en = null;
//        task:for (Entity entity : nearbyEntities) {
//            if (entity instanceof LivingEntity) {
//                if(entity.isDead()||!AA&&e.getScoreboardTags().contains(entity.getUniqueId().toString())){
//                    continue;
//                }
//                for(String s:entity.getScoreboardTags()){
//                    if(DestinyMain.SpecialTag.contains(s)){
//                        continue task;
//                    }
//                }
//                en = (LivingEntity) entity;
//            }
//        }
//        return en;
//    }

    public static LivingEntity NearlyTarget(Entity e, double x, double y, double z){
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity le&&!(le instanceof ArmorStand)) {
                if(le.isDead()||e.getScoreboardTags().contains(le.getUniqueId().toString())||le.getScoreboardTags().contains("No_Track")){
                    continue;
                }
                return le;
            }
        }
        return null;
    }

    public static LinkedList<LivingEntity> NearlyTargets(Entity e, double x, double y, double z){
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
        LinkedList<LivingEntity> les=new LinkedList<>();
        for(Entity en:nearbyEntities){
            if(en instanceof LivingEntity le&&!(le instanceof ArmorStand)&&!le.getScoreboardTags().contains("No_Track")){
                if(le.getLocation().getY()<=e.getLocation().getY()+y||le.getEyeLocation().getY()>=e.getLocation().getY()-y){
                    les.add(le);
                }
            }
        }
        return les;
    }

    public static LinkedList<LivingEntity> Projectile_NearlyTargets(Projectile e, double x, double y, double z){
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
        LinkedList<LivingEntity> les=new LinkedList<>();
        for(Entity en:nearbyEntities){
            if(en instanceof LivingEntity le&&e.getShooter() instanceof Player p&&!p.getUniqueId().equals(le.getUniqueId())){
                if(le.getLocation().getY()<=e.getLocation().getY()+y||le.getEyeLocation().getY()>=e.getLocation().getY()-y){
                    les.add(le);
                }
            }
        }
        return les;
    }

    public static LivingEntity Projectile_NearlyTarget(Projectile e, double x, double y, double z,boolean Direction_All){
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
//        LivingEntity en = null;
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity le&&e.getShooter() instanceof LivingEntity leshoot) {
                if(!Direction_All){
                    Vector v=e.getLocation().getDirection().normalize();
                    Location elo=e.getLocation();
                    Location lelo=le.getLocation();
                    double ax=lelo.getX()-elo.getX(),ay=lelo.getY()-elo.getY(),az= lelo.getZ()-elo.getZ();
                    Vector vector=new Vector(ax,ay,az).normalize();
                    if(entity.isDead()||le.getUniqueId().equals(leshoot.getUniqueId())
                            ||v.getX()*vector.getX()+v.getY()* vector.getY()+v.getZ()* vector.getZ()<=0){
                        continue;
                    }
                }
                if(le.getScoreboardTags().contains("No_Track")){
                    continue;
                }
                if(le.getUniqueId().equals(leshoot.getUniqueId())){
                    continue;
                }
//                en = le;
                return le;
            }
        }
        return null;
    }

    //(1.3*Power/0.225)*0.3±¬Õ¨°ë¾¶
    public static void Make_Explode(Entity entity,float Range){
        entity.getWorld().createExplosion(entity.getLocation(),0.225f/0.39f*Range,false,false,entity);
    }
    public static void Make_Explode(Entity Explosion_Source_Entity,Entity Explode_Entity,float Range){
        Explode_Entity.getWorld().createExplosion(Explode_Entity.getLocation(),0.225f/0.39f*Range,false,false,Explosion_Source_Entity);
    }
    public static void Make_Explode(Entity Explosion_Source_Entity,Location loc,float Range){
        loc.getWorld().createExplosion(loc,0.225f/0.39f*Range,false,false,Explosion_Source_Entity);
    }
    public static void Make_Explode(World world, Location loc, float range, Entity entity){
        world.createExplosion(loc,range,false,false,entity);
    }

    public static void Damage(LivingEntity le,double damage,Entity Source){
        le.setNoDamageTicks(0);
        Vector v=le.getVelocity().clone();
        le.damage(damage,Source);
        le.setVelocity(v);
        le.setNoDamageTicks(0);
    }

    public static void navigateToo(Blaze b,LivingEntity le) {
        if(le==null){
            return;
        }
        EntityBlaze blaze = ((CraftBlaze) b).getHandle();
        blaze.getNavigation().a(le.getLocation().getX(),le.getLocation().getY(),le.getLocation().getZ(), 1.0f);
    }

    public static void navigateToo(Blaze b,Location loc,float speed) {
        if(loc==null){
            return;
        }
        EntityBlaze blaze = ((CraftBlaze) b).getHandle();
        blaze.getNavigation().a(loc.getX(),loc.getY(),loc.getZ(), speed);
    }



    public static boolean aTrack(Entity entity,double[] Track_Radius,int Max_Time,int Cool_Time,double Decay,double Track_Radius_Decay_Max,boolean is_Decay,boolean Direction_ALL,int Model) {
        if (Track_Radius.length != 3) {
            return false;
        }
        double Stree=0.35;
        double Speed = 0.3;
        if(Model==1){
            Stree=0.075;
        }
        if(Model==999){

        }

        double finalStree = Stree;
        double finalSpeed = Speed;
        BukkitRunnable Track = new BukkitRunnable() {
            LivingEntity Closely = null;
            double x = Track_Radius[0], y = Track_Radius[1], z = Track_Radius[2];
            int time = 0;
            double stree= finalStree;
            double speed = finalSpeed;
            double decay = 0;

            public void run() {
                if ((Closely == null || Closely.isDead()) && time > Cool_Time) {
                    Closely = Projectile_NearlyTarget((Projectile) entity, x, y, z, Direction_ALL);
                }
                if (Closely != null && time > Cool_Time) {
                    Vector calE = Closely.getEyeLocation().subtract(entity.getLocation()).toVector().normalize();
                    Vector calT = entity.getLocation().getDirection().normalize();
                    Vector cal = calE.multiply(calT);
                    //Bukkit.broadcastMessage(cal.getZ()*cal.getY()*cal.getX()+"");
                    if (cal.getZ() * cal.getY() * cal.getX() > 0 && entity.getLocation().distance(Closely.getEyeLocation()) < 5) {
                        if(Model!=1){
                            entity.setGravity(false);
                        }
                        Location loc = Closely.getEyeLocation();
                        if(Model==2
                                &&entity.getLocation().getY()<=Closely.getLocation().getY()){
                            entity.setVelocity(new Vector(entity.getVelocity().getX(),0,entity.getVelocity().getZ()));
                        }
                        Vector Aim = loc.toVector().subtract(entity.getLocation().toVector());
                        Vector Steering = Aim.subtract(entity.getVelocity()).normalize().multiply(stree);
                        Vector Velocity = entity.getVelocity().normalize().multiply(speed).add(Steering);

                        entity.setVelocity(Velocity);
                    }
                }
                if(is_Decay){
                    if (time > Cool_Time && decay < Track_Radius_Decay_Max) {
                        x -= Decay;
                        y -= Decay;
                        z -= Decay;
                        decay += Decay;
                        if (decay > Track_Radius_Decay_Max) {
                            x = Track_Radius_Decay_Max;
                            y = Track_Radius_Decay_Max;
                            z = Track_Radius_Decay_Max;
                        }
                    }
                }
                if(time>Max_Time){
                    this.cancel();
                    if(Model==1){
                        if(!entity.getPassengers().isEmpty()&&entity.getPassengers().get(0) instanceof EnderCrystal ec){
                            ec.remove();
                            Nova_Bomb_Listener.Ender_Crystals.remove(ec);
                            Nova_Bomb_Listener.clean((Snowball) entity);
                            entity.remove();
                        }
                    }
                    entity.remove();
                    return;
                }
                time++;
            }
        };
        Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),Cool_Time,1);
        return true;
    }

//    public static void aTrack(Entity entity,double[] Track_Radius,int Max_Time,int Cool_Time,double Decay,double Track_Radius_Decay_Max,boolean is_Decay,boolean Direction_ALL,int Model) {
//        if (Track_Radius.length != 3) {
//            return;
//        }
//        BukkitRunnable Track = new BukkitRunnable() {
//            LivingEntity Closely = null;
//            double x = Track_Radius[0], y = Track_Radius[1], z = Track_Radius[2];
//            int time = 0;
//            double decay = 0;
//
//            public void run() {
//                if ((Closely == null || Closely.isDead()) && time > Cool_Time) {
//                    Closely = Projectile_NearlyTarget((Projectile) entity, x, y, z, true);
//                }
//                if (Closely != null && time > Cool_Time) {
//                    Vector calE = Closely.getLocation().subtract(entity.getLocation()).toVector().normalize();
//                    Vector calT = entity.getLocation().getDirection().normalize();
//                    Vector cal = calE.multiply(calT);
//                    //Bukkit.broadcastMessage(cal.getZ()*cal.getY()*cal.getX()+"");
//                    if (cal.getZ() * cal.getY() * cal.getX() > 0 && entity.getLocation().distance(Closely.getLocation()) < 5) {
//                        entity.setGravity(false);
//                        Location loc = Closely.getLocation();
//                        double speed = 0.3;
//                        Vector Aim = loc.toVector().subtract(entity.getLocation().toVector());
//                        Vector Steering = Aim.subtract(entity.getVelocity()).normalize().multiply(0.35f);
//                        Vector Velocity = entity.getVelocity().normalize().multiply(speed).add(Steering);
//                        entity.setVelocity(Velocity);
//                    }
//                }
//                if(is_Decay){
//                    if (time > Cool_Time && decay < Track_Radius_Decay_Max) {
//                        x -= Decay;
//                        y -= Decay;
//                        z -= Decay;
//                        decay += Decay;
//                        if (decay > Track_Radius_Decay_Max) {
//                            x = Track_Radius_Decay_Max;
//                            y = Track_Radius_Decay_Max;
//                            z = Track_Radius_Decay_Max;
//                        }
//                    }
//                }
//                if(time>Max_Time){
//                    this.cancel();
//                    return;
//                }
//                time++;
//            }
//        };
//        Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),Cool_Time,1);
//    }




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

//    public static ItemStack Remove_Item_Amount(ItemStack is,int Amount){
//        return
//    }

    public static int RandomPositiveMinus(){
        return Math.random()>0.5?1:-1;
    }
}
