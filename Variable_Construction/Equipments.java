package Destiny2.Variable_Construction;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Equipments {
    private ItemStack Helmet,Chest,Legging,Boost,MainHand,OffHand;

    public ItemStack getHelmet() {
        return Helmet;
    }

    public ItemStack getChest() {
        return Chest;
    }

    public ItemStack getLegging() {
        return Legging;
    }

    public ItemStack getBoost() {
        return Boost;
    }

    public ItemStack getMainHand() {
        return MainHand;
    }

    public ItemStack getOffHand() {
        return OffHand;
    }

    public Equipments(Player p) {
        Helmet=p.getInventory().getHelmet();
        Chest=p.getInventory().getChestplate();
        Legging=p.getInventory().getLeggings();
        Boost=p.getInventory().getBoots();
        OffHand=p.getInventory().getItemInOffHand();
        MainHand=p.getInventory().getItemInMainHand();
        if(MainHand.getType()==Material.END_CRYSTAL){
            MainHand=new ItemStack(Material.AIR);
        }

    }
}
