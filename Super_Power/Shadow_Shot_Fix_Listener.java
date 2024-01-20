package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.LinkedList;
import java.util.List;

public class Shadow_Shot_Fix_Listener implements Listener {
    //优化版
    @EventHandler
    public void Arrow_Fix(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player&&e.getEntity().getScoreboardTags().contains("Shadow_Shot_Using")){
            Player p=(Player) e.getEntity();
            if(p.getScoreboardTags().contains("Shadow_Shot_DeadFall")){
                p.getScoreboardTags().remove("Shadow_Shot_Using");
                p.getScoreboardTags().remove("Shadow_Shot_DeadFall");
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                //获取箭矢实体,转化
                Entity entity=e.getProjectile();
                AbstractArrow a=(AbstractArrow) entity;
                //添加玩家UUID
                a.addScoreboardTag("Shooter_UID"+p.getUniqueId());
                a.addScoreboardTag("Arrow_DeadFall");
                //设置箭矢穿透力0,击退0,禁止拾取
                a.setKnockbackStrength(0);
                a.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            }//莫比乌斯
            else if(p.getScoreboardTags().contains("Shadow_Shot_Mobius") ||p.getScoreboardTags().contains("Shadow_Shot_Arrow_1")){
                if(!p.getScoreboardTags().contains("Shadow_Shot_Arrow_1")){
                    p.getScoreboardTags().remove("Shadow_Shot_Mobius");
                    p.getScoreboardTags().remove("Shadow_Shot_Using");
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                p.getScoreboardTags().remove("Shadow_Shot_Arrow_1");
                //获取箭矢实体,转化

                Entity entity=e.getProjectile();
                AbstractArrow a=(AbstractArrow) entity;

                //额外两只箭
                Arrow leftArrow = a.getWorld().spawn(a.getLocation(), Arrow.class);
                leftArrow.setVelocity(a.getVelocity().clone().rotateAroundY(-Math.PI / 10));
                Arrow rightArrow = a.getWorld().spawn(a.getLocation(), Arrow.class);
                rightArrow.setVelocity(a.getVelocity().clone().rotateAroundY(Math.PI / 10));
                leftArrow.addScoreboardTag("Shooter_UID"+e.getEntity().getUniqueId());
                rightArrow.addScoreboardTag("Shooter_UID"+e.getEntity().getUniqueId());
                leftArrow.addScoreboardTag("Arrow_Mobius");
                rightArrow.addScoreboardTag("Arrow_Mobius");



                //获取莫比乌斯射箭方式
                if(DestinyMain.H.getMobius_Quiver_Whether_Solo_Arrow()){

                }
                //添加玩家UUID
                a.addScoreboardTag("Shooter_UID"+e.getEntity().getUniqueId());
                a.addScoreboardTag("Arrow_Mobius");
                //设置箭矢穿透力0,击退0,禁止拾取
                Misc.Track(p,a,3,3,3,300,3);
                Misc.Track(p,leftArrow,3,3,3,300,3);
                Misc.Track(p,rightArrow,3,3,3,300,3);
                a.setKnockbackStrength(0);
                a.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            }
        }
    }

    //弓箭击中
    @EventHandler
    public void Hit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Arrow){
            AbstractArrow arrow=(AbstractArrow) e.getEntity();
            Player p=(Player) arrow.getShooter();
            if(arrow.getScoreboardTags().contains("Arrow_Mobius")){
                arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 2.0f, 0.5f);
                arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 1.5f, 0.5f);
                if(e.getHitEntity()!=null&& e.getHitEntity() instanceof LivingEntity le){
                    le.damage(20);
                    le.setNoDamageTicks(0);
                }
                Summon_Chain(p,arrow,arrow,false);
            }else if(arrow.getScoreboardTags().contains("Arrow_DeadFall")){
                arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.PLAYERS, 3.0f, 0.5f);
                arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 2.5f, 0.5f);
                if(e.getHitEntity()!=null&& e.getHitEntity() instanceof LivingEntity le){
                    le.damage(20);
                    le.setNoDamageTicks(0);
                }
                Summon_Chain(p,arrow,arrow,true);
            }
        }
    }

    private int getTime(Boolean i){
        if(i){return 15;}else{return 7;}
    }
    //召唤锁(发包,暂时无法实现)
    private void Summon_Chain(Player p,Entity arrow,AbstractArrow a,boolean isDeadFall) {
        List<Entity> Played=new LinkedList<>();
        //移除虚弱Tag(暂未实现)
        BukkitRunnable RemoveTag = new BukkitRunnable() {
            List<Entity> mobs = arrow.getNearbyEntities(5.0f, 5.0f, 5.0f);
            @Override
            public void run() {
                for (Entity e : mobs) {
                    if (e.getType() == EntityType.PLAYER && e.getScoreboardTags().contains("Shadow_Shot_Weak")) {
                        LivingEntity le = (LivingEntity) e;
                        e.removeScoreboardTag("Shadow_Shot_Weak");
                    }
                }
                this.cancel();
            }
        };

        //优化版
        BukkitRunnable Debuff = new BukkitRunnable() {
            List<Entity> mobs;
            int time=getTime(isDeadFall);
            double radius=0;
            @Override
            public void run() {
                if(isDeadFall){
                    mobs = arrow.getNearbyEntities(10.0f, 7.0f, 10.0f);
                    radius=10;
                }else{
                    mobs = arrow.getNearbyEntities(4.0f, 4.0f, 4.0f);
                    radius=4;
                }
                if (time >= 0) {
                    for (Entity e : mobs) {
                        if(e instanceof Player p
                                &&!arrow.getScoreboardTags().contains("Shooter_UID"+e.getUniqueId())
                                &&(Played.isEmpty()||!Played.contains(e))
                                &&p.getLocation().distance(arrow.getLocation())<=radius){
                            Played.add(e);
                            p.playSound(e.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.VOICE,1,0.5f);
                        }
                        if(e instanceof LivingEntity le
                                &&!(e instanceof ArmorStand)
                                &&!arrow.getScoreboardTags().contains("Shooter_UID"+e.getUniqueId())
                                &&le.getLocation().distance(arrow.getLocation())<radius){
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 4, false));
                            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 4, false));
                            le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,50,1,false));
                            //e.addScoreboardTag("Shadow_Shot_Weak");
                        }
                    }
                } else {
                    arrow.remove();
                    this.cancel();
                }
                int[] rgb={148,0,211};
                if(isDeadFall){
                    Summon.summon_Redstone_Particle(arrow.getWorld(),arrow.getLocation(),10,32,2,false,rgb);
                }else{
                    Summon.summon_Redstone_Particle(arrow.getWorld(),arrow.getLocation(),4,8,2,false,rgb);
                }
                time--;
            }
        };
        Debuff.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class), 20, 20);
    }
}
