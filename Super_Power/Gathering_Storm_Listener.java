package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import Destiny2.Super_Power_Chooser;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/*
========代码待优化=========
 */

public class Gathering_Storm_Listener implements Listener {
    public static LinkedList<Player> Gathering_Storm_User=new LinkedList<>();
    public static LinkedList<Trident> Tridents=new LinkedList<>();
    public static LinkedList<Trident> Trident_Hit=new LinkedList<>();
    public static final int Super_Power_Activate_Time=8;
    public final int Gathering_Storm_Time=15;
    public final int Lighting_Times=7;
//    //{ X , Y , Z , avg}相关代码未编写
//    public final double[] xTrack_Radius={5.00,5.00,5.00};
//    public final double[] xGathering_Storm_Radius={6.00,6.00,6.00};
    public final double Track_Radius=5.00;
    public final double Gathering_Storm_Radius=6.00;
    //per tick
    public final double Track_Radius_Decay=0.15;
    public final double Track_Radius_Decay_Max=3.00;
    //检测三叉戟丢出

    public static void List_Clean(){
        Tridents.clear();
        Trident_Hit.clear();
    }

    public void clean(Trident tr){
        Tridents.remove(tr);
    }
    @EventHandler
    public void Throw(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Trident tr&&tr.getShooter() instanceof Player p){
            if(Gathering_Storm_User.contains(p)){
//                tr.getWorld().playSound(tr.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 2.0f, 0.6f);
                Gathering_Storm_Fix(tr,p);
                Gathering_Storm_User.remove(p);
                Super_Power_Chooser.remove_User_List(p);
            }
        }
    }

    //检测三叉戟击中
    @EventHandler
    public void Hit(ProjectileHitEvent phe){
        Entity entity=phe.getEntity();
        Entity hitE=phe.getHitEntity();
        Block block=phe.getHitBlock();


        if(entity instanceof Trident tr&&Tridents.contains(tr)
                &&tr.getShooter() instanceof Player player){
            if(block != null){
                Trident_Hit.add(tr);
                tr.setGravity(false);
                tr.teleport(tr);
                tr.setVelocity(new Vector(0,0,0));

            }
            if(hitE instanceof LivingEntity le&&!le.getUniqueId().equals(player.getUniqueId())){
                le.damage(25);
                le.setNoDamageTicks(0);
                Trident_Hit.add(tr);
                tr.setKnockbackStrength(0);
                tr.setGravity(false);
                tr.teleport(tr);
                tr.setVelocity(new Vector(0,0,0));
            }
        }
    }

    //判定,启动！
    private void Gathering_Storm_Fix(Trident Trident,Player p){
        Tridents.add(Trident);
        //三叉戟追踪,唤雷
        //TIP:探测范围内百分百中,待改善
        BukkitRunnable Track = new BukkitRunnable() {
            LivingEntity Closely=null;
            List<Entity> mobs;
            boolean Track=true;
            boolean First_Lighting=false;
            double x=Track_Radius,y=Track_Radius,z=Track_Radius;
            int time=0;
            double decay=0;
            int Lighting_times=Lighting_Times;
            public void run() {
                if((Closely==null||Closely.isDead())&&time>3){
                    Closely= Misc.Projectile_NearlyTarget(Trident,x,y,z,false);
                }
                if(Closely!=null&&!Trident_Hit.contains(Trident)&&time>3){
                    //time=0;
                    Vector calE=Closely.getLocation().subtract(Trident.getLocation()).toVector().normalize();
                    Vector calT=Trident.getLocation().getDirection().normalize();
                    Vector cal=calE.multiply(calT);
                    //Bukkit.broadcastMessage(cal.getZ()*cal.getY()*cal.getX()+"");
                    if(cal.getZ()*cal.getY()*cal.getX()>0&&Trident.getLocation().distance(Closely.getLocation())<5){
                        Trident.setGravity(false);
                        Location loc=Closely.getLocation();
                        double speed=0.3;
                        Vector Aim = loc.toVector().subtract(Trident.getLocation().toVector());
                        Vector Steering = Aim.subtract(Trident.getVelocity()).normalize().multiply(0.35f);
                        Vector Velocity = Trident.getVelocity().normalize().multiply(speed).add(Steering);
                        Trident.setVelocity(Velocity);
                    }
                }
                if(time>3&&decay<Track_Radius_Decay_Max){
                    x-=Track_Radius_Decay;
                    y-=Track_Radius_Decay;
                    z-=Track_Radius_Decay;
                    decay+=Track_Radius_Decay;
                    if(decay>Track_Radius_Decay_Max){
                        x=Track_Radius_Decay_Max;
                        y=Track_Radius_Decay_Max;
                        z=Track_Radius_Decay_Max;
                    }
                }

//                //追踪
//                if(Track){
//                    time=DestinyMain.H.getGathering_Storm_Lightning_Times()*40;
//                    Track=false;
//                }

                if(Trident_Hit.contains(Trident)
                        &&(time%40==0||!First_Lighting)&&Lighting_times>0){
                    First_Lighting=true;
                    Lighting_times--;
                    mobs=Trident.getNearbyEntities(Gathering_Storm_Radius,Gathering_Storm_Radius,Gathering_Storm_Radius);
                    //闪电效果
                    Trident.getWorld().strikeLightningEffect(Trident.getLocation());
                    //闪电伤害
                    for (Entity ee : mobs) {
                        if (ee instanceof LivingEntity le
                                &&le.getLocation().distance(Trident.getLocation())<=Gathering_Storm_Radius
                                &&Trident.getShooter() instanceof Player p
                                &&!p.getUniqueId().equals(ee.getUniqueId())) {
                            le.damage(25,p);
                            le.setNoDamageTicks(0);
                        }
                    }

                }
                if(Trident_Hit.contains(Trident)&&time%15==0){
                    int[] rgb={0,255,255};
                    Summon.summon_Redstone_Particle(Trident.getLocation(),Gathering_Storm_Radius,64,1,false,rgb);
                }
                if(Lighting_times<1||time>Gathering_Storm_Time*20){
                    Trident.remove();
                    clean(Trident);
                    this.cancel();
                    return;
                }
                time++;
            }
        };
        Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain .class),0,1);

    }
}
