package Destiny2.Misc;

import Destiny2.DestinyMain;
import Destiny2.Super_Power.Nova_Bomb_Listener;
import Destiny2.Super_Power.Thunder_Crash_Listener;
import Destiny2.Variable_Construction.Equipments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.Map;


public class Damage_Listener implements Listener {
    LinkedList<Player> Thunder_Crash_User= Thunder_Crash_Listener.Thunder_Crash_User;
    LinkedList<Player> Super_Power_User=DestinyMain.Super_Power_User;
    //FLY_INTO_WALL撞击伤害
    //FALL摔落伤害
    final private EntityDamageEvent.DamageCause FLY_INTO_WALL= EntityDamageEvent.DamageCause.FLY_INTO_WALL;
    final private EntityDamageEvent.DamageCause FALL= EntityDamageEvent.DamageCause.FALL;
    final private EntityDamageEvent.DamageCause EEXPLODE= EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    final private EntityDamageEvent.DamageCause BEXPLODE= EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
    final private EntityDamageEvent.DamageCause PROJECTILE= EntityDamageEvent.DamageCause.PROJECTILE;
    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbe){
        Entity e=edbe.getEntity();
        Entity damager=edbe.getDamager();

        if(edbe.getDamager() instanceof Blaze b&&b.getScoreboardTags().contains("No_Track")){
            edbe.setCancelled(true);
            return;
        }
        if(damager.getScoreboardTags().contains("No_Fire")&&e.getFireTicks()!=0){
            e.setFireTicks(0);
        }
        if(damager instanceof ShulkerBullet&& e instanceof LivingEntity
                &&damager.getScoreboardTags().contains("Nova_Bomb")){
            Misc.Reduce_PotionEffect((LivingEntity) e,PotionEffectType.LEVITATION,1,200);
        }
    }
    //无摔落伤害,自爆炸伤害
    @EventHandler
    public void No_Damage(EntityDamageEvent e){
        Entity E= e.getEntity();
        //鞘翅撞墙,摔落伤害
        if(E instanceof Player p&&Thunder_Crash_User.contains(p)){
            if(e.getCause()== FLY_INTO_WALL||e.getCause()==FALL){
                e.setCancelled(true);
                //p.setGliding(false);
                p.getInventory().setChestplate(new ItemStack(Material.AIR));
                return;
            }
        }
        //禁止摧毁末影水晶
        if(E instanceof EnderCrystal ec&&Nova_Bomb_Listener.Ender_Crystals.contains(ec)){
            e.setCancelled(true);
        }
        //禁止摧毁雪球
        if(E instanceof Snowball sb&&sb.getScoreboardTags().contains("No_Break")){
            e.setCancelled(true);
        }

    }
    //爆炸不产生破坏
    @EventHandler
    public void No_Explode(EntityExplodeEvent e){
        Entity E= e.getEntity();
        if(Nova_Bomb_Listener.Ender_Crystals.contains(E)||E.getScoreboardTags().contains("No_Explode")){
            e.blockList().clear();
        }
    }
}
