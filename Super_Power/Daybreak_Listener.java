package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import org.bukkit.*;
import org.bukkit.block.data.type.Snow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.LinkedList;

public class Daybreak_Listener implements Listener {
    public static LinkedList<Player> Day_Break_User=new LinkedList<>();

    public double[] Ball_Track_Radius={3.00,3.00,3.00};
    public double Day_Break_Damage=25.00;
    public int Day_Break_Attack_Cool_Time_Ticks=18;
    public int Day_Break_Fly_Cool_Time_Ticks=20;


    //空中停滞
    @EventHandler
    public void Daybreak(PlayerToggleSneakEvent ptse){
        Player p=ptse.getPlayer();
        if(Day_Break_User.contains(p)&&!p.isSneaking()&&!p.isOnGround()){
            if(p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }else{
                p.setVelocity(p.getVelocity().setY(0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,600,4,false));
            }
        }
    }

    //击中
    @EventHandler
    public void hit(ProjectileHitEvent phe){
        if(phe.getEntity() instanceof Snowball sb&&sb.getScoreboardTags().contains("Daybreak_Ball")
                &&phe.getHitEntity() instanceof LivingEntity le
                &&sb.getShooter() instanceof Player p){
            le.setLastDamage(Day_Break_Damage);
            le.setNoDamageTicks(0);
            Misc.Make_Explode(sb.getWorld(),sb.getLocation(),1.5f,sb);
            for(Entity en:sb.getNearbyEntities(2,2,2)){
                if(en instanceof LivingEntity les&&!les.getUniqueId().equals(p.getUniqueId())){
                    les.setFireTicks(100);
                    les.setNoDamageTicks(0);
                }
            }
        }

    }

    //爆炸伤害
    @EventHandler
    public void damage(EntityDamageByEntityEvent edbee){
        if(edbee.getEntity() instanceof LivingEntity le&&edbee.getDamager() instanceof Snowball sb
                &&sb.getScoreboardTags().contains("Daybreak_Ball")
                &&sb.getShooter() instanceof Player p
                &&edbee.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            if(p.getUniqueId().equals(le.getUniqueId())){
                edbee.setCancelled(true);
            }else {
                edbee.setDamage(35 / sb.getLocation().distance(le.getLocation()));
            }
        }
    }

    //挥动判定
    @EventHandler
    public void judge(PlayerAnimationEvent pae){
        Player p= pae.getPlayer();
        //飞天判定
        if(Day_Break_User.contains(p)){
            //飞行判定
            if(p.isSneaking()
                    &&p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.BINDING_CURSE)==5
                    &&!p.hasCooldown(Material.GOLDEN_SWORD) &&pae.getAnimationType()== PlayerAnimationType.ARM_SWING){
                p.setVelocity(p.getLocation().getDirection().multiply(1.2).setY(0.4));
                p.setCooldown(Material.GOLDEN_SWORD,Day_Break_Fly_Cool_Time_Ticks);
                if(!p.hasCooldown(Material.GOLDEN_HOE)){
                    p.setCooldown(Material.GOLDEN_HOE,5);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10,4,false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,600,4,false));
                Misc.Reduce_DAMAGE_RESISTANCE_PotionEffect(p,10);
                p.getInventory().setItemInMainHand(Misc.itemDamage(p.getInventory().getItemInMainHand(),10,true));
                p.updateInventory();
            }
            //挥刀判定
            if(!p.hasCooldown(Material.GOLDEN_HOE)
                    &&p.getInventory().getItemInMainHand().getType()==Material.GOLDEN_SWORD
                    &&p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.BINDING_CURSE)==5
                    &&!p.isSneaking()) {


                p.setCooldown(Material.GOLDEN_HOE,Day_Break_Attack_Cool_Time_Ticks);
                p.setVelocity(p.getVelocity().setY(0.45));
                Entity e=p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.SNOWBALL);
                ((Snowball)e).setShooter(p);
                e.addScoreboardTag("Daybreak_Ball");
                e.addScoreboardTag("No_Break");
                e.setInvulnerable(true);

                float pitch=p.getLocation().getPitch();
                if(pitch<=0&&pitch>=-15){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(0.284-(pitch/-120)));
                }
                else if(pitch<-15&&pitch>-45){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(pitch/-90));
                }
                else if(pitch<=-45){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(pitch/-65));
                }
                else if(pitch>=0&&pitch<25){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(0.284-(pitch/120)));
                }
                else if(pitch>=25&&pitch<45){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(0.14-(pitch/150)));
                }
                else if(pitch>45){
                    e.setVelocity(p.getEyeLocation().getDirection().multiply(0.5));//.setY(0.362-(pitch/180))
                }

                e.getLocation().setDirection(p.getLocation().getDirection());
                Effect(e);
//                Misc.Track(p,e,5,5,5,200,0);
                Misc.aTrack(e,Ball_Track_Radius,60,1,0,0,false,false,0);
                Misc.Reduce_DAMAGE_RESISTANCE_PotionEffect(p,10);
                p.getInventory().setItemInMainHand(Misc.itemDamage(p.getInventory().getItemInMainHand(),10,true));
                p.updateInventory();
            }
        }
    }

    //落地检测
//    @EventHandler
    public void movement(PlayerMoveEvent pme){
        if(true){
            return;
        }
        Player p= pme.getPlayer();

        if(p.getScoreboardTags().contains("Daybreak_Using")){
            if(p.isOnGround()&&!p.hasPotionEffect(PotionEffectType.LEVITATION)&&p.hasPotionEffect(PotionEffectType.SLOW_FALLING)){
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
//            BukkitRunnable br= new BukkitRunnable() {
//                @Override
//                public void run() {
//                    // 获取玩家的位置和视角方向
//                    Location location = p.getLocation();
//                    Vector direction = location.getDirection();
//
//                    // 获取玩家移动的向量
//                    Location from = pme.getFrom();
//                    Location to = pme.getTo();
//                    Vector movement = to.toVector().subtract(from.toVector());
//
//                    // 计算玩家移动方向和视角方向的夹角
//                    double angle = direction.angle(movement);
//
//                    // 判断移动方向是前后还是左右，并输出结果
//                    if (angle < Math.PI / 4) {
//                        Bukkit.broadcastMessage("玩家 " + p.getName() + " 向前移动");
//                    } else if (angle > Math.PI / 4 && angle < 3 * Math.PI / 4) {
//                        // 判断玩家是向左还是向右移动
//                        Vector crossProduct = direction.getCrossProduct(movement);
//                        if (crossProduct.getY() > 0) {
//                            Bukkit.broadcastMessage("玩家 " + p.getName() + " 向左移动");
//                        } else {
//                            Bukkit.broadcastMessage("玩家 " + p.getName() + " 向右移动");
//                        }
//                    } else {
//                        Bukkit.broadcastMessage("玩家 " + p.getName() + " 向后移动");
//                    }
//                }
//            };

        }

    }

    //弹道特效
    void Effect(Entity e){

        new BukkitRunnable(){
            int times=100;
            @Override
            public void run() {
                e.getWorld().spawnParticle(Particle.SWEEP_ATTACK,e.getLocation(),1,0,0,0);
                if(e.isDead()|| times<0){
                    this.cancel();
                }
                times--;
            }
        }.runTaskTimerAsynchronously(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }
}
