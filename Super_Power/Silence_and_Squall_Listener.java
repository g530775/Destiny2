package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;

import Destiny2.Misc.Summon;
import net.minecraft.server.v1_16_R3.EntityBlaze;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBlaze;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Silence_and_Squall_Listener implements Listener {
    public static LinkedList<LivingEntity> Freeze_Mobs=new LinkedList<>();
    public static Map<Blaze,Player> Silence_Squall_Damager=new HashMap<>();

    public final static double[] Freeze_Range={8.00,5.00,8.00};
    public final static double[] Storm_Range={8.00,5.00,8.00};
    public final static double[] Storm_Track_Range={18.00,8.00,18.00};
    public final static float Freeze_Range_First=8.00f;
    public final static int Storm_Max_Time_Ticks=300;

    public static void List_Clean(){
        Freeze_Mobs.clear();
        Silence_Squall_Damager.clear();
    }

    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbee){
        if(edbee.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                &&edbee.getDamager() instanceof ArmorStand as&&as.getScoreboardTags().contains("Silence_and_Squall_1")){
            if(edbee.getEntity() instanceof Player p&&as.getScoreboardTags().contains(p.getUniqueId().toString())){
                edbee.setCancelled(true);
                return;
            }
            if(edbee.getEntity() instanceof Player player){
                Freeze_Mobs.add(player);
                edbee.setDamage(25);
                return;
            }
            if(edbee.getEntity() instanceof LivingEntity le){
                Freeze_Mobs.add(le);
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,4,false));
                le.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,199,false));
                edbee.setDamage(25);
            }
        }
    }



    //½ûÖ¹ÒÆ¶¯
    @EventHandler
    public void No_Move(PlayerMoveEvent pme){
        if(Freeze_Mobs.contains(pme.getPlayer())){
            pme.setFrom(pme.getFrom().setDirection(pme.getTo().getDirection()));
            pme.setCancelled(true);
        }
    }

    //ËÀÍöÒÆ³ý
    @EventHandler
    public void Dead(EntityDeathEvent ede){
        LivingEntity le= ede.getEntity();
        Freeze_Mobs.remove(le);
    }

    public static void Rotate(ArmorStand as,Player player,boolean Second){
        BukkitRunnable rotate=new BukkitRunnable() {
            float rotation=0;
            int time=0;
            boolean Enable=false;
            @Override
            public void run() {
                if(Second){
                    as.setRotation(rotation,rotation);
                    rotation-=30.0f;
                    if(as.isOnGround()){
                        Enable=true;
                    }
                }else{
                    as.setRightArmPose(as.getRightArmPose().add(0.25,0,0));
                    if(as.isOnGround()){
                        Misc.Make_Explode(as,Freeze_Range_First);
                        LinkedList<LivingEntity> mobs=Misc.NearlyTargets(as,Freeze_Range[0],Freeze_Range[1],Freeze_Range[2]);
                        for(LivingEntity le:mobs){
                            if(as.getScoreboardTags().contains(le.getUniqueId().toString())){
                                mobs.remove(le);
                                Freeze_Mobs.addAll(mobs);
                                break;
                            }
                        }
                        for(LivingEntity le:mobs){
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,99,false));
                            le.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,140,99,false));
                        }
                        this.cancel();
                        as.remove();
                        return;
                    }
                }
                as.setVelocity(as.getVelocity().setY(-0.2));

                if(Enable){
                    Blaze blaze=(Blaze)as.getWorld().spawnEntity(as.getLocation(),EntityType.BLAZE);
                    Silence_Squall_Damager.put(blaze,player);
                    blaze.addScoreboardTag(player.getUniqueId().toString());
                    blaze.addScoreboardTag("No_Track");
                    blaze.setInvulnerable(true);
                    blaze.setCollidable(false);
                    blaze.setNoDamageTicks(500);
                    Storm(blaze);
                    as.remove();
                    this.cancel();
                    return;
                }

                if(time>Storm_Max_Time_Ticks||as.isDead()){
                    as.remove();
                    this.cancel();
                }
                time++;
            }
        };
        rotate.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }

    public static void Storm(Blaze blaze){
        BukkitRunnable Storm=new BukkitRunnable() {
            Map<LivingEntity,Integer> Range_mobs=new HashMap<>();
            int time=0;
            int[] rgb={0,255,255};
            @Override
            public void run() {
                Misc.navigateToo(blaze,Misc.NearlyTarget(blaze,Storm_Track_Range[0],Storm_Track_Range[1],Storm_Track_Range[2]));
                for(LivingEntity le:Misc.NearlyTargets(blaze,Storm_Range[0],Storm_Range[1],Storm_Range[2])){
                    if(blaze.getScoreboardTags().contains(le.getUniqueId().toString())){
                        continue;
                    }
                    if(Freeze_Mobs.contains(le)){
                        Vector v=le.getVelocity().clone();
                        Misc.Damage(le,25,Silence_Squall_Damager.get(blaze));
                        le.getWorld().playSound(le.getLocation(), Sound.BLOCK_GLASS_BREAK,SoundCategory.PLAYERS,1,0.5f);
                        Freeze_Mobs.remove(le);
                    }else{
                        if(Range_mobs.containsKey(le)&&Range_mobs.get(le)>=100){
                            Freeze_Mobs.add(le);
                            Range_mobs.remove(le);
                        }else if(Range_mobs.containsKey(le)&&Range_mobs.get(le)<100){
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,0,false));
                            Range_mobs.put(le,Range_mobs.get(le)+10);
                        }else{
                            Range_mobs.put(le,20);
                        }
                        Misc.Damage(le,2,Silence_Squall_Damager.get(blaze));
                        if(le instanceof Player p){
                            p.playSound(p.getLocation(),Sound.ITEM_CROSSBOW_LOADING_START,SoundCategory.PLAYERS,1,1);
                        }
                    }
                }
                if(time>Storm_Max_Time_Ticks/3){
                    blaze.remove();
                    Silence_Squall_Damager.remove(blaze);
                    Freeze_Mobs.clear();
                    this.cancel();
                }
                time++;
                ((CraftBlaze) blaze).getHandle().setGoalTarget(null);
                Summon.summon_Redstone_Particle(blaze.getLocation(),Storm_Range[0],150,1,false,rgb);
            }
        };
        Storm.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),20,5);
    }


}
