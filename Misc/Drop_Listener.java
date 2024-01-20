package Destiny2.Misc;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDropItemEvent;

public class Drop_Listener implements Listener {
    //禁止丢弃
    @EventHandler
    public void No_Drop(PlayerDropItemEvent pdie){
        ItemStack i=pdie.getItemDrop().getItemStack();
        Player p= pdie.getPlayer();
        if(i.getEnchantmentLevel(Enchantment.BINDING_CURSE)==5){
            String name=i.getItemMeta().getDisplayName();
            switch (name) {
                case "雷霆冲击" -> {
                    pdie.setCancelled(true);
                    p.getInventory().setChestplate(i);
                    p.setGliding(true);
                }
                default -> pdie.setCancelled(true);
            }
        }
        p.updateInventory();
    }
    @EventHandler
    public void Armor_No_Drop(EntityDropItemEvent e){
        Item item=e.getItemDrop();
        ItemStack i=item.getItemStack();
        if(i.getItemMeta().getEnchantLevel(Enchantment.BINDING_CURSE)==5){
            if ("Well_Of_Radiance_Sword".equals(i.getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                item.remove();
            }
        }
    }
}
