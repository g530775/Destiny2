package Destiny2.Super_Power;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerAnimationEvent;


public class Sentinel_Shield_Listener implements Listener {
    @EventHandler
    public void Blocking(EntityDamageByEntityEvent event){
        Entity e=event.getEntity();
        Entity d=event.getDamager();
        if(e.getScoreboardTags().contains("Sentinel_Shield_Using")&& e instanceof Player p){
            ItemStack Shield=p.getInventory().getItemInOffHand();
            if(Shield.getType()!=Material.SHIELD||Shield.getEnchantmentLevel(Enchantment.BINDING_CURSE)!=5){
                return;
            }
            if (event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) > 0.0) {
                event.setCancelled(true);
                return;
            }
        }
        LivingEntity le;
        if(d instanceof LivingEntity&&d.getScoreboardTags().contains("Sentinel_Shield_Using")) {
            le = (LivingEntity) d;
            Destiny2.Misc.Misc.Reduce_DAMAGE_RESISTANCE_PotionEffect(le,20);
            if (!le.hasPotionEffect(PotionEffectType.SPEED)) {
                le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0, false));
                event.setDamage(20);
            }
        }
    }
    //挥击向前冲刺
    @EventHandler
    public void Push(PlayerAnimationEvent event){
        Player p = event.getPlayer();
        if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)
                &&p.isSneaking()
                &&p.getScoreboardTags().contains("Sentinel_Shield_Using")
                &&!p.hasPotionEffect(PotionEffectType.SLOW)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,0,false));
            p.setVelocity(p.getLocation().getDirection().multiply(0.8f));
        }
    }
}
