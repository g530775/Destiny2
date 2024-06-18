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
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Nova_Bomb_Listener implements Listener {
    static private final String[] CataclysmTags={"Nova_Bomb","Nova_Bomb_Cataclysm","No_Fire"};
    static private final String[] VortexTags={"Nova_Bomb","Nova_Bomb_Vortex","No_Fire"};
    static private final String[] PTag={"Nova_Bomb","Nova_Bomb_Using","Nova_Bomb_Cataclysm","Nova_Bomb_Vortex","No_Fire"};

    public static LinkedList<EnderCrystal> Ender_Crystals=new LinkedList<>();
    public static LinkedList<Player> Nova_Bomb_User=new LinkedList<>();
    public static Map<Player,Boolean> Cataclysm_Bomb_Model=new HashMap<>();
    public static LinkedList<Snowball> Cataclysm_Bomb=new LinkedList<>();
    public static LinkedList<Snowball> Vortex_Bomb=new LinkedList<>();
    public static LinkedList<ShulkerBullet> Cataclysm_Small_Bomb=new LinkedList<>();
    public static LinkedList<LivingEntity> Hit_LivingEntity=new LinkedList<>();
    public final double[] xVortex_Damage_Radius={5.00,5.00,5.00};
    public final double[] xVortex_Pull_Radius={12.0,7.0,12.0};
    public final double[] xCataclysm_Bomb_Track_Radius={6.00,6.00,6.00};

    public final double Cataclysm_Bomb_Damage=35.00;
    public final double Cataclysm_Small_Bomb_Damage=25.00;
    public final double Vortex_Bomb_Damage=5.00;

    void clean(Player p){
        Nova_Bomb_User.remove(p);
        Cataclysm_Bomb_Model.remove(p);
    }
    public static void clean(Snowball b){
        Cataclysm_Bomb.remove(b);
        Vortex_Bomb.remove(b);

    }

    public static void List_Clean(){
        Ender_Crystals.clear();
        Cataclysm_Bomb_Model.clear();
        Cataclysm_Bomb.clear();
        Vortex_Bomb.clear();
        Cataclysm_Small_Bomb.clear();
        Hit_LivingEntity.clear();
    }

    //检测发射
    @EventHandler
    public void Nova_Bomb(ProjectileLaunchEvent pete) {
//        Entity e = pete.getEntity();
//        Entity Shooter;
//        Snowball sb;
//        if(e instanceof Snowball){
//            sb=(Snowball) e;
//            if(sb.getShooter() instanceof Player){
//                Shooter= (Entity) sb.getShooter();
//            }else{
//                return;
//            }
//        }else{return;}
        if(pete.getEntity() instanceof Snowball sb&&sb.getShooter() instanceof Player p&&Nova_Bomb_User.contains(p)){
            if(Cataclysm_Bomb_Model.get(p)){
                Cataclysm_Bomb.add(sb);
                sb.setGravity(false);
                sb.setVelocity(sb.getVelocity().multiply(0.25f));
                Spawner(sb, true);
            }else{
                Vortex_Bomb.add(sb);
                sb.setVelocity(sb.getVelocity().multiply(0.45f));
                Spawner(sb, false);
            }
            sb.addScoreboardTag("No_Break");
            clean(p);
            Super_Power_Chooser.remove_User_List(p);
        }

//        //取发射者,判断发射者Tag
//        if (Shooter instanceof Player p &&Shooter.getScoreboardTags().contains("Nova_Bomb_Using")) {
//
//            e.addScoreboardTag(p.getUniqueId().toString());
//            e.setInvulnerable(true);
//            if (p.getScoreboardTags().contains("Nova_Bomb_Cataclysm")) {
//                e.setVelocity(e.getVelocity().multiply(0.3f));
//                e.setGravity(false);
//                sb.setVelocity(sb.getVelocity().multiply(0.5f));
//                Spawner(e, p, CataclysmTags,true);
//            } else if (p.getScoreboardTags().contains("Nova_Bomb_Vortex")) {
//                e.setVelocity(e.getVelocity().multiply(0.45f));
//                e.setGravity(true);
//                Spawner(e, p, VortexTags,false);
//            }
//            Tagger(p,PTag,true);
//        }
    }

    //击中判定
    @EventHandler
    public void Got_hit(ProjectileHitEvent phe){
        Entity ball= phe.getEntity();
        Entity entity=phe.getHitEntity();
        Block block=phe.getHitBlock();
        //目前暂时禁止打爆水晶&&禁止砸自己

//        if(entity!=null){
//            if((entity instanceof EnderCrystal||entity instanceof ShulkerBullet)&&entity.getScoreboardTags().contains("Nova_Bomb")
//                    ||ball.getScoreboardTags().contains(entity.getUniqueId().toString())){
//                phe.setCancelled(true);
//                return;
//            }
//        }


//        //伤害归属者
//        Player p;
//        if(ball instanceof Snowball){
//            ProjectileSource Shooter=((Snowball)ball).getShooter();
//            if(Shooter instanceof Player){p=(Player)Shooter;}else{return;}
//        }else if(ball instanceof ShulkerBullet){
//            ProjectileSource Shooter=((ShulkerBullet)ball).getShooter();
//            if(Shooter instanceof Player){p=(Player)Shooter;}else{return;}
//        }else{
//            return;
//        }

        //击中爆炸
        if(ball instanceof Snowball sb&&sb.getShooter() instanceof Player player){
            if(Cataclysm_Bomb.contains(sb)){
                Misc.Make_Explode(ball.getWorld(),ball.getLocation(), 8.0f,sb);
                Summon.Nova_Small_Bomb(ball.getLocation().add(0,3,0),sb);
                if(!ball.getPassengers().isEmpty()&&ball.getPassengers().get(0) instanceof EnderCrystal ec){
                    ec.remove();
                    Ender_Crystals.remove(ec);
                }

            }
            if(Vortex_Bomb.contains(sb)){
                //Make_Explode(ball.getWorld(),ball.getLocation(), 8.0f,p);
                ball.getWorld().playSound(ball.getLocation(), Sound.AMBIENT_CRIMSON_FOREST_MOOD, SoundCategory.PLAYERS,3.0f,1.0f);
                Vortex((EnderCrystal) ball.getPassengers().get(0),player);
            }
        }else if(ball instanceof ShulkerBullet sb&&Cataclysm_Small_Bomb.contains(sb)
                &&sb.getShooter()instanceof Player player &&entity instanceof LivingEntity le){
            le.damage(Cataclysm_Small_Bomb_Damage,player);
            Cataclysm_Small_Bomb.remove(sb);
            Hit_LivingEntity.add(le);
            le.setNoDamageTicks(0);
        }
    }

    //爆炸伤害检测修改
    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbe){
        Entity entity=edbe.getEntity();
        Entity damager=edbe.getDamager();
        EntityDamageEvent.DamageCause Cause= edbe.getCause();
        if(Cause==EntityDamageEvent.DamageCause.ENTITY_EXPLOSION&&damager instanceof Snowball sb){
            if(!(entity instanceof LivingEntity)){
                edbe.setCancelled(true);
            }
            if(entity instanceof LivingEntity le&&sb.getShooter() instanceof Player player){
                if(le.getUniqueId().equals(player.getUniqueId())){
                    edbe.setDamage(Cataclysm_Bomb_Damage*0.2);
                    return;
                }
                edbe.setDamage(Cataclysm_Bomb_Damage);
            }
        }
    }

    //消除药水效果
    @EventHandler
    public void No_Effect(EntityPotionEffectEvent epee){
        if(epee.getEntity() instanceof LivingEntity le&&epee.getCause()== EntityPotionEffectEvent.Cause.ATTACK){
            if(Hit_LivingEntity.contains(le)){
                epee.setCancelled(true);
                Hit_LivingEntity.remove(le);
            }
        }
    }

    private void Spawner(Snowball sb,boolean is_Nova_Bomb) {
        EnderCrystal bomb = (EnderCrystal) sb.getWorld().spawnEntity(sb.getLocation(), EntityType.ENDER_CRYSTAL);
        bomb.setInvulnerable(true);
        bomb.setShowingBottom(false);
        Ender_Crystals.add(bomb);
        bomb.addScoreboardTag("No_Explode");
        sb.addPassenger(bomb);
        if(is_Nova_Bomb){
            Misc.aTrack(sb,xCataclysm_Bomb_Track_Radius,200,1,0,0,false,true,1);
        }
    }

//    //生成时添加Tag
//    private void Spawner(Entity e, Player p, String[] BTags,boolean AA) {
//        Entity bomb = p.getWorld().spawnEntity(p.getLocation(), EntityType.ENDER_CRYSTAL);
//        bomb.setInvulnerable(true);
//        ((EnderCrystal)bomb).setShowingBottom(false);
//        Tagger(bomb,BTags,false);
//        Tagger(e,BTags,false);
//        e.addPassenger(bomb);
//        if(AA){
//            Misc.Track(p,e,5,5,5,200,0);
//        }
//    }



//    //击中不漂浮
//    @EventHandler
//    public void No_Float(EntityPotionEffectEvent epee){
//        Entity entity=epee.getEntity();
//        if(epee.getCause()== EntityPotionEffectEvent.Cause.ATTACK
//                &&entity instanceof LivingEntity le
//                &&entity.getScoreboardTags().contains("No_Float")){
//            epee.setCancelled(true);
//            le.getScoreboardTags().remove("No_Float");
//        }
//    }


//    //创建爆炸
//    static public void Make_Explode(World world, Location loc, float range,Player player){
//        world.createExplosion(loc,range,false,false,player);
//    }

    //创建涡流
    public void Vortex(EnderCrystal bomb,Player player){
        BukkitRunnable VortexDamage=new BukkitRunnable() {
            boolean pulled=false;
            int times=32;
            final double Damage=5.0D;
            final Location bombLocation=bomb.getLocation().add(0,10,0);
            List<Entity> entities=bomb.getNearbyEntities(xVortex_Pull_Radius[0],xVortex_Pull_Radius[1],xVortex_Pull_Radius[2]);
            List<Entity> attackEn=bomb.getNearbyEntities(xVortex_Damage_Radius[0],xVortex_Damage_Radius[1],xVortex_Damage_Radius[2]);
            final double Radius=7;
            @Override
            public void run() {
                //吸引
                if(!pulled){
                    for(Entity e:entities){
                        if(e instanceof LivingEntity le&&le.getLocation().distance(bomb.getLocation())<=Radius+5
                                &&le.getUniqueId()!=player.getUniqueId()){
                            //拉动实体
                            Location entityLocation=e.getLocation();
                            //斜长最大20.8
                            //cos度数=0.576923
                            double multiply=entityLocation.distance(bombLocation);
                            //优化一下拉动距离
                            multiply=Math.sqrt(Math.sqrt(multiply));
                            //拉动一次
                            Vector v=bombLocation.toVector().subtract(entityLocation.toVector()).normalize().multiply(multiply).setY(0.5);
                            le.setVelocity(v);
                        }
                    }
                    pulled=true;
                }
                if(attackEn!=null&&times<=28) {
                    for (Entity e : attackEn) {
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
                    Ender_Crystals.remove(bomb);
                    return;
                }
                if(times%4==0){
                    int[] rgb={72,61,139};
                    Summon.summon_Redstone_Particle(bomb.getLocation(),7,24,2,false,rgb);
                }
                times--;
                attackEn=bomb.getNearbyEntities(xVortex_Damage_Radius[0],xVortex_Damage_Radius[1],xVortex_Damage_Radius[2]);
            }
        };
        VortexDamage.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,5);
    }
}
