package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Super_Power_Chooser;
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

import java.util.LinkedList;
import java.util.List;

public class Golden_Gun_Listener implements Listener {
    public static LinkedList<Player> Golden_Gun_User=new LinkedList<>();
    public static LinkedList<Player> Ammo3=new LinkedList<>();
    public static LinkedList<Player> Ammo6=new LinkedList<>();
    public static LinkedList<Arrow> Ammo=new LinkedList<>();

    public final double Time6=25;
    public final double Time3=15;
    public final int Golden_Gun_6_Cool_Time=15;
    public final int Golden_Gun_3_Cool_Time=5;

    public void clean(Player p){
        Golden_Gun_User.remove(p);
        Super_Power_Chooser.remove_User_List(p);
    }
    public void clean(Arrow a){
        Ammo.remove(a);
    }

    public static void List_Clean(){
        Ammo3.clear();
        Ammo6.clear();
        Ammo.clear();
    }

    //射击互动
    @EventHandler
    public void Golden_Gun_Listener(PlayerInteractEvent event){
        if(!DestinyMain.Super_Power_User.contains(event.getPlayer())||!Golden_Gun_User.contains(event.getPlayer())){
            return;
        }
        event.setCancelled(true);
        if(event.hasItem()&&event.getAction()== Action.RIGHT_CLICK_AIR){
            ItemStack item=event.getItem();
            if(item.getType()!=Material.GOLDEN_HOE&&item.getEnchantmentLevel(Enchantment.BINDING_CURSE)!=5){
                return;
            }
            Player p=event.getPlayer();
            if(p.getInventory().getItemInOffHand().getType()==Material.GOLDEN_HOE){
                return;
            }
            if(!p.hasCooldown(Material.GOLDEN_HOE)){
                if(Ammo3.contains(p)){
                    p.setCooldown(Material.GOLDEN_HOE,Golden_Gun_6_Cool_Time);
                    Shoot(p,true);
                }
                if(Ammo6.contains(p)){
                    p.setCooldown(Material.GOLDEN_HOE,Golden_Gun_3_Cool_Time);
                    Shoot(p,false);
                }
                if(item.getAmount()==1){
                    p.getInventory().setItemInMainHand(Super_Power_Chooser.Player_Equipment.get(p).getMainHand());
                    p.getInventory().setItemInMainHand(Super_Power_Chooser.Player_Equipment.get(p).getOffHand());
                    clean(p);
                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }else{
                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                }
            }
        }
    }
//    //射击互动
//    @EventHandler
//    public void Golden_Gun_Listener(PlayerInteractEvent event){
//
//
//        if(event.hasItem()&&(event.getAction()== Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)){
//            ItemStack item=event.getItem();
//            if(item.getType()!=Material.GOLDEN_HOE&&item.getEnchantmentLevel(Enchantment.BINDING_CURSE)!=5){
//                return;
//            }
//            Player p=event.getPlayer();
//
//            if(p.getScoreboardTags().contains("Golden_Gun_Using")&&!p.hasCooldown(Material.GOLDEN_HOE)){
//                event.setCancelled(true);
//                if(p.getScoreboardTags().contains("Golden_Gun_3")){
//                    Damageable bd=(Damageable) item.getItemMeta();
//                    bd.setDamage(bd.getDamage()+item.getType().getMaxDurability()/3+1);
//                    item.setItemMeta((ItemMeta) bd);
//                    p.setCooldown(Material.GOLDEN_HOE,15);
//                    Shoot(p,true);
//                }else if(p.getScoreboardTags().contains("Golden_Gun_6")){
//                    Damageable bd=(Damageable) item.getItemMeta();
//                    bd.setDamage(bd.getDamage()+item.getType().getMaxDurability()/6+1);
//                    item.setItemMeta((ItemMeta) bd);
//                    p.setCooldown(Material.GOLDEN_HOE,5);
//                    Shoot(p,false);
//                }
//                ItemMeta meta=item.getItemMeta();
//
//                int Dur=((Damageable)item.getItemMeta()).getDamage();
//                if(Dur>=Material.GOLDEN_HOE.getMaxDurability()){
//                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
//                    p.getScoreboardTags().remove("Golden_Gun_Using");
//                    p.getScoreboardTags().remove("Golden_Gun_3");
//                    p.getScoreboardTags().remove("Golden_Gun_6");
//                    p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
//                }
//            }
//        }
//    }


    //爆头判定
    @EventHandler
    public void Ammo_Hit(ProjectileHitEvent ple){
        if(ple.getEntity() instanceof Arrow ar&&ple.getHitEntity() instanceof LivingEntity le
                &&Ammo.contains(ar)){
            if(ar.getShooter() instanceof Player p&&p.getUniqueId().equals(le.getUniqueId())){
                ple.setCancelled(true);
                return;
            }
            if(ar.getScoreboardTags().contains("3")){
                double bottom=le.getEyeLocation().getY()-0.2f;
                double top=bottom+0.25f;
                double hitY=ple.getEntity().getLocation().getY();
                if(hitY>=bottom&&hitY<=top){
                    le.damage(50);
                }else{
                    le.damage(25);
                }
                le.setNoDamageTicks(0);
            }else if(ar.getScoreboardTags().contains("6")){
                le.damage(20);
                le.setNoDamageTicks(0);
            }
            clean(ar);
            ar.remove();
        }

        if(ple.getEntity() instanceof Arrow ar &&Ammo.contains(ar)){
            clean(ar);
            ar.remove();
        }


    }
//    //爆头判定
//    @EventHandler
//    public void Ammo_Hit(ProjectileHitEvent ple){
//        if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo3")){
//            ple.getEntity().setGravity(true);
//            LivingEntity le=(LivingEntity) ple.getHitEntity();
//            if(le!=null){
//                double bottom=le.getEyeLocation().getY()-0.2f;
//                double top=bottom+0.25f;
//                double hitY=ple.getEntity().getLocation().getY();
//                if(hitY>=bottom&&hitY<=top){
//                    le.damage(50);
//                }else{
//                    le.damage(25);
//                }
//                le.setNoDamageTicks(0);
//            }
//        }else if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo6")){
//            LivingEntity le=(LivingEntity) ple.getHitEntity();
//            if(le!=null){
//                le.damage(20);
//                le.setNoDamageTicks(0);
//            }
//        }
//        if(ple.getEntity().getScoreboardTags().contains("Golden_Gun_Ammo")){
//            ple.getEntity().remove();
//        }
//
//    }


    //死亡爆炸(死亡射击专属?)
//    @EventHandler
    public void DeathExplode(EntityDeathEvent e){
        Player p=e.getEntity().getKiller();
        LivingEntity le=e.getEntity();
        EntityDamageEvent.DamageCause Cause=le.getLastDamageCause().getCause();
        if(Cause== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            return;
        }
        if(e.getEntity().getLastDamageCause().getEntity().getScoreboardTags().contains("6")){
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(),3.0f,false,false,e.getEntity().getLastDamageCause().getEntity());
        }

    }

//    //寻找最近生物
//    private LivingEntity NearlyTarget(Entity e,double x,double y,double z) {
//        List<Entity> nearbyEntities = e.getNearbyEntities(x,y,z);
//        Entity en = null;
//        for (Entity entity : nearbyEntities) {
//            if(e.getScoreboardTags().contains(entity.getUniqueId().toString())){
//                continue;
//            }
//            if (entity.getType() == EntityType.PLAYER
//                    || entity.getType() == EntityType.IRON_GOLEM) {
//                en = entity;
//            }
//        }
//        if (en == null)
//            return null;
//        LivingEntity mob = (LivingEntity)en;
//        return mob;
//    }

    //射击
    private void Shoot(Player p,boolean is3){
        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS,3,1);
        double[] Range={0.5f,0.25f,0.5f};
        Arrow as=(Arrow)p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.ARROW);
        as.teleport(p.getEyeLocation());
        as.setVelocity(p.getLocation().getDirection().multiply(3));
        as.setGravity(false);
        as.setBounce(false);
        Ammo.add(as);
        as.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        as.setKnockbackStrength(0);
        as.setPierceLevel(0);

        if(is3){
            Range[0]=1.00;Range[1]=0.50f;Range[2]=1.00;
            as.addScoreboardTag("3");
        }else{
            as.addScoreboardTag("6");
        }
        as.setShooter(p);
        Misc.aTrack(as,Range,40,1,0,0,false,false,0);
    }
}
