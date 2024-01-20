package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import java.util.List;



public class Nova_Bomb_Listener implements Listener {
    static private final String[] CataclysmTags={"Nova_Bomb","Nova_Bomb_Cataclysm","No_Fire"};
    static private final String[] VortexTags={"Nova_Bomb","Nova_Bomb_Vortex","No_Fire"};
    static private final String[] PTag={"Nova_Bomb","Nova_Bomb_Using","Nova_Bomb_Cataclysm","Nova_Bomb_Vortex","No_Fire"};

    //检测发射
    @EventHandler
    public void Nova_Bomb(ProjectileLaunchEvent pete) {
        Entity e = pete.getEntity();
        Entity Shooter;
        Snowball sb;
        if(e instanceof Snowball){
            sb=(Snowball) e;
            if(sb.getShooter() instanceof Player){
                Shooter= (Entity) sb.getShooter();
            }else{
                return;
            }
        }else{return;}
        //取发射者,判断发射者Tag
        if (Shooter instanceof Player p &&Shooter.getScoreboardTags().contains("Nova_Bomb_Using")) {

            e.addScoreboardTag(p.getUniqueId().toString());
            e.setInvulnerable(true);
            if (p.getScoreboardTags().contains("Nova_Bomb_Cataclysm")) {
                e.setVelocity(e.getVelocity().multiply(0.3f));
                e.setGravity(false);
                sb.setVelocity(sb.getVelocity().multiply(0.5f));
                Spawner(e, p, CataclysmTags,true);
            } else if (p.getScoreboardTags().contains("Nova_Bomb_Vortex")) {
                e.setVelocity(e.getVelocity().multiply(0.45f));
                e.setGravity(true);
                Spawner(e, p, VortexTags,false);
            }
            Tagger(p,PTag,true);
        }
    }

    //生成时添加Tag
    private void Spawner(Entity e, Player p, String[] BTags,boolean AA) {
        Entity bomb = p.getWorld().spawnEntity(p.getLocation(), EntityType.ENDER_CRYSTAL);
        bomb.setInvulnerable(true);
        ((EnderCrystal)bomb).setShowingBottom(false);
        Tagger(bomb,BTags,false);
        Tagger(e,BTags,false);
        e.addPassenger(bomb);
        if(AA){
            Misc.Track(p,e,5,5,5,200,0);
        }
    }

    //击中判定
    @EventHandler
    public void Got_hit(ProjectileHitEvent phe){
        Entity ball= phe.getEntity();
        Entity entity=phe.getHitEntity();
        //目前暂时禁止打爆水晶&&禁止砸自己

        if(entity!=null){
            if((entity instanceof EnderCrystal||entity instanceof ShulkerBullet)&&entity.getScoreboardTags().contains("Nova_Bomb")
                    ||ball.getScoreboardTags().contains(entity.getUniqueId().toString())){
                phe.setCancelled(true);
                return;
            }
        }


        //伤害归属者
        Player p;
        if(ball instanceof Snowball){
            ProjectileSource Shooter=((Snowball)ball).getShooter();
            if(Shooter instanceof Player){p=(Player)Shooter;}else{return;}
        }else if(ball instanceof ShulkerBullet){
            ProjectileSource Shooter=((ShulkerBullet)ball).getShooter();
            if(Shooter instanceof Player){p=(Player)Shooter;}else{return;}
        }else{
            return;
        }

        //击中爆炸
        if(ball instanceof Snowball sb){
            if(ball.getScoreboardTags().contains("Nova_Bomb_Cataclysm")){
                if(!ball.getPassengers().isEmpty()){
                    ball.getPassengers().get(0).remove();
                }
                Make_Explode(ball.getWorld(),ball.getLocation(), 8.0f,p);
                Summon.Nova_Small_Bomb(ball.getLocation().add(0,3,0),p);
            }else if(ball.getScoreboardTags().contains("Nova_Bomb_Vortex")&&!ball.getPassengers().isEmpty()){
                //Make_Explode(ball.getWorld(),ball.getLocation(), 8.0f,p);
                ball.getWorld().playSound(ball.getLocation(), Sound.AMBIENT_CRIMSON_FOREST_MOOD, SoundCategory.PLAYERS,3.0f,1.0f);
                Vortex(ball.getPassengers().get(0),p);
            }
        }
        if(ball instanceof ShulkerBullet sb&&entity instanceof LivingEntity le){
            le.addScoreboardTag("No_Float");
            le.damage(25,p);
            le.setNoDamageTicks(0);
        }
    }

    //击中不漂浮
    @EventHandler
    public void No_Float(EntityPotionEffectEvent epee){
        Entity entity=epee.getEntity();
        if(epee.getCause()== EntityPotionEffectEvent.Cause.ATTACK
                &&entity instanceof LivingEntity le
                &&entity.getScoreboardTags().contains("No_Float")){
            epee.setCancelled(true);
            le.getScoreboardTags().remove("No_Float");
        }
    }

    //伤害判定

    //添加Tag
    private void Tagger(Entity entity,String[] Tags,boolean remove){
        if(remove){
            for(String s:Tags){
                entity.getScoreboardTags().remove(s);
            }
        }else {
            for (String s : Tags) {
                entity.addScoreboardTag(s);
            }
        }
    }


    //创建爆炸
    static public void Make_Explode(World world, Location loc, float range,Player player){
        world.createExplosion(loc,range,false,false,player);
    }

    //创建涡流
    public void Vortex(Entity bomb,Player player){
        BukkitRunnable VortexDamage=new BukkitRunnable() {
            boolean pulled=false;
            int times=32;
            final double Damage=5.0D;
            final Location bombLocation=bomb.getLocation().add(0,10,0);
            List<Entity> entities=bomb.getNearbyEntities(12.0,7.0,12.0);
            List<Entity> attackEn=bomb.getNearbyEntities(7.0,7.0,7.0);
            final double Radius=7;
            @Override
            public void run() {
                if(!pulled){
                    for(Entity e:entities){
                        if(e instanceof LivingEntity le&&le.getLocation().distance(bomb.getLocation())<=Radius+5
                                &&le.getUniqueId()!=player.getUniqueId()){
                            //拉动实体
                            Location entityLocation=e.getLocation();
                            double multiply=entityLocation.distance(bombLocation);
                            //优化一下拉动距离
                            if(multiply>10){
                                multiply/=5;
                            }else{
                                multiply/=4;
                            }
                            //拉动一次
                            Vector v=bombLocation.toVector().subtract(entityLocation.toVector()).normalize().multiply(multiply).setY(0.5);
                            le.setVelocity(v);
                        }
                    }
                    pulled=true;
                }
                if(attackEn!=null&&times<=28) {
                    for (Entity e : entities) {
                        if (e instanceof LivingEntity le
                                &&le.getUniqueId()!=player.getUniqueId()
                                &&le.getLocation().distance(bomb.getLocation())<=Radius) {
                            le.damage(Damage,player);
                            le.setNoDamageTicks(0);
                        }
                    }
                }
                if(times<0){
                    this.cancel();
                    bomb.remove();
                    return;
                }
                if(times%4==0){
                    int[] rgb={72,61,139};
                    Summon.summon_Redstone_Particle(bomb.getWorld(),bomb.getLocation(),7,24,2,false,rgb);
                }
                times--;
                entities=bomb.getNearbyEntities(5.0,5.0,5.0);
            }
        };
        VortexDamage.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,5);
    }
}
