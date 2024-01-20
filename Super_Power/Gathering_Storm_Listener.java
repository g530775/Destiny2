package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Summon;
import Destiny2.Variable_Construction.Position;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;


/*
========代码待优化=========
 */

public class Gathering_Storm_Listener implements Listener {
    //检测三叉戟丢出
    @EventHandler
    public void Throw(ProjectileLaunchEvent event){
        Entity e=event.getEntity();
        if(e instanceof Trident tr){
            Player p;
            p=(Player)tr.getShooter();
            //tr.setPierceLevel(100);
            if(p==null){
                return;
            }
            if(p.getScoreboardTags().contains("Gathering_Storm_Using")){
                p.getScoreboardTags().remove("Gathering_Storm_Using");
                e.addScoreboardTag("Gathering_Storm");
                e.addScoreboardTag("Gathering_Storm_Trident");
                e.addScoreboardTag(p.getUniqueId().toString());
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 2.0f, 0.6f);
//                Gathering_Storm(e,p);

                Gathering_Storm_Fix(e,p);
            }
        }
    }

    //检测三叉戟击中
    @EventHandler
    public void Hit(ProjectileHitEvent phe){
        Entity entity=phe.getEntity();
        Entity hitE=phe.getHitEntity();
        Block block=phe.getHitBlock();


        if(entity instanceof Trident tr&&tr.getScoreboardTags().contains("Gathering_Storm_Trident")){
            if(block != null){
                tr.addScoreboardTag("Gathering_Storm_Hit");
            }
            if(hitE instanceof LivingEntity le&&!entity.getScoreboardTags().contains(hitE.getUniqueId().toString())){
                le.damage(25);
                le.setNoDamageTicks(0);
            }
//            tr.addScoreboardTag("Gathering_Storm_Hit");
//            tr.setVelocity(new Vector(0,0,0));
//            tr.setGravity(false);

        }
    }

    //判定,启动！
    private void Gathering_Storm_Fix(Entity Trident,Player p){
        //三叉戟追踪,唤雷
        //TIP:探测范围内百分百中,待改善
        BukkitRunnable Track = new BukkitRunnable() {
            LivingEntity Closely=null;
            List<Entity> mobs;
            boolean AA=DestinyMain.H.getAuto_Aim_Self();
            boolean Track=true;
            final double Radius=5.0;
            double x=5.0f,y=5.0f,z=5.0f;
            int time=0;
            public void run() {
                if(!Trident.getScoreboardTags().contains("Gathering_Storm_Hit")){
                    if(Closely==null){
                        Closely=Destiny2.Misc.Misc.NearlyTarget(Trident,x,y,z,AA);
                    }
                    else{
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
                    if(time>200){
                        this.cancel();
                        return;
                    }
                    if(time<100){
                        x-=0.03f;
                        y-=0.03f;
                        z-=0.03f;
                    }
                    time++;
                }else {
                    if(Track){time=DestinyMain.H.getGathering_Storm_Lightning_Times()*40;Track=false;}
                    if(time>0&&time%40==0){
                        mobs=Trident.getNearbyEntities(5.0f,5.0f,5.0f);
                        //使用效果
                        LightningStrike ls=Trident.getWorld().strikeLightningEffect(Trident.getLocation());
                        ls.addScoreboardTag("No_Fire");
                        ls.addScoreboardTag("Gathering_Storm_Lightning_Damage");
                        ls.addScoreboardTag(p.getUniqueId().toString());
                        for (Entity ee : mobs) {
                            if (ee instanceof LivingEntity le&& !Trident.getScoreboardTags().contains(ee.getUniqueId().toString())
                                    &&le.getLocation().distance(Trident.getLocation())<=Radius) {
                                le.damage(25,p);
                                le.setNoDamageTicks(0);
                            }
                        }

                    }else if(time<=0){
                        Trident.remove();
                        this.cancel();
                        return;
                    }if(time%15==0){
                        int[] rgb={0,255,255};
                        Summon.summon_Redstone_Particle(Trident.getWorld(),Trident.getLocation(),5,100,1,false,rgb);
                    }
                    time--;
                }
            }
        };

        Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain .class),5,1);
    }
}
