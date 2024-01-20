package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Golden_Gun_Listener implements Listener {
    //射击互动
    @EventHandler
    public void Golden_Gun_Listener(PlayerInteractEvent event){
        if(event.hasItem()&&(event.getAction()== Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)){
            ItemStack item=event.getItem();
            if(item.getType()!=Material.GOLDEN_HOE&&item.getEnchantmentLevel(Enchantment.BINDING_CURSE)!=5){
                return;
            }
            Player p=event.getPlayer();

            if(p.getScoreboardTags().contains("Golden_Gun_Using")&&!p.hasCooldown(Material.GOLDEN_HOE)){
                event.setCancelled(true);
                if(p.getScoreboardTags().contains("Golden_Gun_3")){
                    Damageable bd=(Damageable) item.getItemMeta();
                    bd.setDamage(bd.getDamage()+item.getType().getMaxDurability()/3+1);
                    item.setItemMeta((ItemMeta) bd);
                    p.setCooldown(Material.GOLDEN_HOE,15);
                    Shoot(p,true);
                }else if(p.getScoreboardTags().contains("Golden_Gun_6")){
                    Damageable bd=(Damageable) item.getItemMeta();
                    bd.setDamage(bd.getDamage()+item.getType().getMaxDurability()/6+1);
                    item.setItemMeta((ItemMeta) bd);
                    p.setCooldown(Material.GOLDEN_HOE,5);
                    Shoot(p,false);
                }
                ItemMeta meta=item.getItemMeta();

                int Dur=((Damageable)item.getItemMeta()).getDamage();
                if(Dur>=Material.GOLDEN_HOE.getMaxDurability()){
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    p.getScoreboardTags().remove("Golden_Gun_Using");
                    p.getScoreboardTags().remove("Golden_Gun_3");
                    p.getScoreboardTags().remove("Golden_Gun_6");
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
            }
        }
    }
    //被击中
    @EventHandler
    public void Got_Hit(EntityDamageByEntityEvent event){
        Entity d= event.getDamager();
        Entity e= event.getEntity();
        if(d.getScoreboardTags().contains("Golden_Gun_Ammo6")){
            if(e instanceof LivingEntity le){
                event.setDamage(24);
                le.setNoDamageTicks(0);
            }
        }
    }
    //爆头判定
    @EventHandler
    public void Ammo_Hit(ProjectileHitEvent ple){
        if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo3")){
            ple.getEntity().setGravity(true);
            LivingEntity le=(LivingEntity) ple.getHitEntity();
            if(le!=null){
                double bottom=le.getEyeLocation().getY()-0.2f;
                double top=bottom+0.25f;
                double hitY=ple.getEntity().getLocation().getY();
                if(hitY>=bottom&&hitY<=top){
                    le.damage(50);
                }else{
                    le.damage(25);
                }
                le.setNoDamageTicks(0);
            }
        }else if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo6")){
            LivingEntity le=(LivingEntity) ple.getHitEntity();
            if(le!=null){
                le.damage(20);
                le.setNoDamageTicks(0);
            }
        }
        if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo")){
            ple.getEntity().remove();
        }

    }
    //死亡爆炸(死亡射击专属?)
    @EventHandler
    public void DeathExplode(EntityDeathEvent e){
//        Player p=e.getEntity().getKiller();
//        if(p==null||e.getEntity().getLastDamageCause().getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
//            Bukkit.broadcastMessage(p.getName());
//            return;
//        }
//        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(),3.0f,false,false,p);
    }

    //寻找最近生物
    private LivingEntity NearlyTarget(Entity e,double x,double y,double z) {
        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
        Entity en = null;
        for (Entity entity : nearbyEntities) {
            if(e.getScoreboardTags().contains(entity.getUniqueId().toString())){
                continue;
            }
            if (entity.getType() == EntityType.PLAYER
                    || entity.getType() == EntityType.IRON_GOLEM) {
                en = entity;
            }
        }
        if (en == null)
            return null;
        LivingEntity mob = (LivingEntity)en;
        return mob;
    }

    //射击
    private void Shoot(Player p,boolean is3){
        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS,3,1);
        double[] Range={0.5f,0.25f,0.5f};
        Entity ammo=p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.ARROW);
        ammo.setVelocity(p.getLocation().getDirection().multiply(3));
        ammo.setGravity(false);
        AbstractArrow as=(AbstractArrow) ammo;
        as.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        as.setKnockbackStrength(0);
        as.setPierceLevel(0);

        //AA
        BukkitRunnable Track = new BukkitRunnable() {
            Entity Closely=null;
            int time=0;
            public void run() {
                if (Closely == null) {
                    Closely = NearlyTarget(as, Range[0], Range[1], Range[2]);
                } else {
                    as.setGravity(false);
                    Location loc = Closely.getLocation();
                    double speed = 0.3;
                    Vector Aim = loc.toVector().subtract(as.getLocation().toVector());
                    Vector Steering = Aim.subtract(as.getVelocity()).normalize().multiply(10);
                    Vector Velocity = as.getVelocity().normalize().multiply(speed).add(Steering);
                    as.setVelocity(Velocity);
                }
                if (time > 80) {
                    ammo.remove();
                    this.cancel();
                    return;
                }
                time++;
            }
        };
        if(is3){
            ammo.addScoreboardTag("Golden_Gun_Ammo");
            ammo.addScoreboardTag("Golden_Gun_Ammo3");
            ammo.addScoreboardTag(p.getUniqueId().toString());
            Range[0]=1;Range[1]=0.5f;Range[2]=1;
            Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),1,1);
            as.setShooter(p);
        }
        else{
            ammo.addScoreboardTag("Golden_Gun_Ammo");
            ammo.addScoreboardTag("Golden_Gun_Ammo6");
            ammo.addScoreboardTag(p.getUniqueId().toString());
            Track.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),1,1);
            as.setShooter(p);
        }
    }
}
