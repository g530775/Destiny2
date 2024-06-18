package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shadow_Shot_Fix_Listener implements Listener {
    public static LinkedList<Player> Shadow_Shot_User=new LinkedList<>();
    public static LinkedList<Player> Using_Mobius=new LinkedList<>();
    public static Map<Player,Integer> Amount=new HashMap<>();
    public static Map<Arrow,LinkedList<LivingEntity>> Trapped=new HashMap<>();
    public static LinkedList<Arrow> Mobius_Arrow=new LinkedList<>();
    public static LinkedList<Arrow> Dead_Fall_Arrow=new LinkedList<>();
    public final int Dead_Fall_Time=15;
    public final int Mobius_Time=7;
    public final int Waiting_Time=30;
    public final double Dead_Fall_Radius=10.00;
    public final double Mobius_Radius=4.00;
    public final double Damage_Rate=0.15;

    public final double[] xDead_Fall_Radius= {10.00,10.00,10.00};
    public final double[] xMobius_Radius={4.00,4.00,4.00};

    public void clean(Player p){
        DestinyMain.Super_Power_User.remove(p);
        Shadow_Shot_User.remove(p);
        Using_Mobius.remove(p);
        Amount.remove(p);
    }
    public void cleanArrow(Arrow a){
        Mobius_Arrow.remove(a);
        Dead_Fall_Arrow.remove(a);
        Trapped.remove(a);
    }

    public static void List_Clean(){
        Using_Mobius.clear();
        Amount.clear();
        Trapped.clear();
        Mobius_Arrow.clear();
        Dead_Fall_Arrow.clear();
    }

    //伤害并联
    @EventHandler
    public void Damges(EntityDamageByEntityEvent edbe){
        if(edbe.getEntity() instanceof LivingEntity Hit_Le&&!edbe.getDamager().getScoreboardTags().contains("Shadow_Shot_Arrow")){
            if(!Hit_Le.hasPotionEffect(PotionEffectType.SLOW)
                    &&!Hit_Le.hasPotionEffect(PotionEffectType.BLINDNESS)
                    &&!Hit_Le.hasPotionEffect(PotionEffectType.GLOWING)){
                return;
            }
            for(Entity en:Hit_Le.getNearbyEntities(10,10,10)){
                if(en instanceof Arrow ar&&Trapped.containsKey(ar)){
                    for(LivingEntity le:Trapped.get(ar)){
                        if(!le.getUniqueId().equals(Hit_Le.getUniqueId())){
                            Misc.Damage(le,edbe.getDamage()*Damage_Rate,edbe.getDamager());
                        }
                    }
                    return;
                }
            }
        }
    }

    //优化版
    @EventHandler
    public void Arrow_Fix(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p && Shadow_Shot_User.contains(p)) {
            //获取箭矢实体,转化
            Arrow a = (Arrow) e.getProjectile();
            if (Using_Mobius.contains(p)&&Amount.containsKey(p)) {
                Amount.put(p,Amount.get(p)-1);
                if(Amount.get(p)<=0){
                    clean(p);
                }
                //额外两只箭
//                Arrow leftArrow = a.getWorld().spawn(a.getLocation(), EntityType.ARROW);
                Arrow leftArrow=(Arrow)a.getWorld().spawnEntity(a.getLocation(),EntityType.ARROW);
                leftArrow.setShooter(a.getShooter());
                leftArrow.setVelocity(a.getVelocity().clone().rotateAroundY(-Math.PI / 12));
//                Arrow rightArrow = a.getWorld().spawn(a.getLocation(), Arrow.class);
                Arrow rightArrow=(Arrow)a.getWorld().spawnEntity(a.getLocation(),EntityType.ARROW);
                rightArrow.setShooter(a.getShooter());
                rightArrow.setVelocity(a.getVelocity().clone().rotateAroundY(Math.PI / 12));

                //设置箭矢穿透力0,击退0,禁止拾取
                leftArrow.setKnockbackStrength(0);
                leftArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                Mobius_Arrow.add(leftArrow);
                a.setKnockbackStrength(0);
                a.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                Mobius_Arrow.add(a);
                rightArrow.setKnockbackStrength(0);
                rightArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                Mobius_Arrow.add(rightArrow);
//                Misc.Track(leftArrow, 3, 3, 3, 300, 3);
//                Misc.Track(a, 3, 3, 3, 300, 3);
//                Misc.Track(rightArrow, 3, 3, 3, 300, 3);
                leftArrow.addScoreboardTag("Shadow_Shot_Arrow");
                a.addScoreboardTag("Shadow_Shot_Arrow");
                rightArrow.addScoreboardTag("Shadow_Shot_Arrow");
                Misc.aTrack(leftArrow,xMobius_Radius,200,1,0,0,false,false,0);
                Misc.aTrack(a,xMobius_Radius,200,1,0,0,false,false,0);
                Misc.aTrack(rightArrow,xMobius_Radius,200,1,0,0,false,false,0);
            }else{
                clean(p);
                a.setKnockbackStrength(0);
                a.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                Dead_Fall_Arrow.add(a);
            }

        }
    }

    //弓箭击中
    @EventHandler
    public void Hit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow arrow&&arrow.getShooter() instanceof Player p){
            if(Mobius_Arrow.contains(arrow)||Dead_Fall_Arrow.contains(arrow)){
                if(Mobius_Arrow.contains(arrow)){
                    arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 2.0f, 0.5f);
                    arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 1.5f, 0.5f);
                    if(e.getHitEntity()!=null&& e.getHitEntity() instanceof LivingEntity le){
                        le.damage(20);
                        le.setNoDamageTicks(0);
                    }
                    Summon_Chain(p,arrow,true);

                }else if(Dead_Fall_Arrow.contains(arrow)){
                    arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 3.0f, 0.5f);
//                    arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 2.5f, 0.5f);
                    if(e.getHitEntity()!=null&& e.getHitEntity() instanceof LivingEntity le){
                        le.damage(35);
                        le.setNoDamageTicks(0);
                    }
                    Summon_Chain(p,arrow,false);
                }
                Trapped.put(arrow,new LinkedList<>());
            }

        }
    }

    private int getTime(Boolean isMobius){
        return isMobius?Mobius_Time:Dead_Fall_Time;
    }
    //召唤
    private void Summon_Chain(Player p,Arrow arrow,boolean isMobius) {
        List<Entity> Played=new LinkedList<>();
        //优化版
        BukkitRunnable Debuff = new BukkitRunnable() {
            List<Entity> mobs=new LinkedList<>();
            int time=getTime(isMobius)*4;
            int waiting=Waiting_Time*4;
            boolean enable=false;
            double[] radius={0.00,0.00,0.00};
            @Override
            public void run() {
                if(isMobius){
                    radius[0]=xMobius_Radius[0];
                    radius[1]=xMobius_Radius[1];
                    radius[2]=xMobius_Radius[2];
                    enable=true;
                    if(time%10==0){
                        mobs = arrow.getNearbyEntities(radius[0], radius[1], radius[2]);
                    }
                }else{
                    radius[0]=xDead_Fall_Radius[0];
                    radius[1]=xDead_Fall_Radius[1];
                    radius[2]=xDead_Fall_Radius[2];
                    if(time%10==0){
                        mobs = arrow.getNearbyEntities(radius[0],radius[1],radius[2]);
                    }
                    if(!enable){
                        if(waiting<=0){
                            enable=true;
                        }
                        for(Entity entity:mobs){
                            if(entity instanceof Player player&&!player.getUniqueId().equals(p.getUniqueId())){
                                enable=true;
                                arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 2.5f, 0.5f);
                                break;
                            }else if(entity instanceof LivingEntity le&&!(le instanceof Player)){
                                enable=true;
                                arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 2.5f, 0.5f);
                                break;
                            }
                        }
                    }
                }

                if (enable) {

                    if(time%4==0){
                        Trapped.get(arrow).clear();
                        Trapped.get(arrow).addAll(Misc.NearlyTargets(arrow,radius[0],radius[1],radius[2]));
                    }
                    for (Entity e : mobs) {
                        if(e instanceof  LivingEntity le&&!(e instanceof ArmorStand)){
                            if(le instanceof Player p &&!Played.contains(p)
                                    &&arrow.getShooter() instanceof Player player&&!player.getUniqueId().equals(p.getUniqueId())){
                                Played.add(e);
                                p.playSound(e.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.VOICE,1,0.5f);
                            }
                            if(!le.equals(arrow.getShooter())){
                                if(le instanceof Player p&&(p.getGameMode()!=GameMode.CREATIVE||p.getGameMode()!=GameMode.SPECTATOR)){
                                    p.setGliding(false);
                                }
                                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 4, false));
                                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 4, false));
                                le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,50,1,false));
                            }
                        }
                    }
                    time--;
                }else{
                    waiting--;
                }

                if(enable&&time%10==0){
                    int[] rgb={148,0,211};
                    if(isMobius){
                        Summon.summon_Redstone_Particle(arrow.getLocation(),4,16,2,false,rgb);
                    }else{
                        Summon.summon_Redstone_Particle(arrow.getLocation(),10,32,2,false,rgb);
                    }
                }
                if(time<=0){
                    cleanArrow(arrow);
                    arrow.remove();
                    this.cancel();
                }
            }
        };
        Debuff.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class), 10, 5);
    }
}
