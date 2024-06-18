package Destiny2.Variable_Construction;

import Destiny2.DestinyMain;
import Destiny2.Super_Power.Shadow_Shot_Fix_Listener;
import Destiny2.Super_Power.Well_Of_Radiance_Listener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class Items {
    public static LinkedList<ItemStack> itemStacks=new LinkedList<>();

    public static void clean(){
        LinkedList<Player> playerList=new LinkedList<>(Bukkit.getOnlinePlayers());
        for(Player player:playerList){
            for(ItemStack item:player.getInventory().getContents()){
                if(itemStacks.contains(item)){
                    player.getInventory().remove(item);
                }
            }

        }
        for(ArmorStand as: Well_Of_Radiance_Listener.ArmorStands){
            as.remove();
        }
    }

    public static ItemStack getThunder_Crash_ELYTRA(){
        ItemStack i=new ItemStack(Material.ELYTRA);
        ItemMeta im=i.getItemMeta();
        im.setDisplayName("¡ìfÀ×öª³å»÷");
        i.setItemMeta(im);
        i.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(i);
        return i;
    }

    public static ItemStack getWell_Of_Radiance_Sword(){
        ItemStack sword=new ItemStack(Material.GOLDEN_SWORD);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta swordMeta= sword.getItemMeta();
        swordMeta.setDisplayName("Well_Of_Radiance");
        sword.setItemMeta(swordMeta);
        itemStacks.add(sword);
        return sword;
    }

    public static ItemStack getShadow_Shot_Bow(boolean isMobius,int Amount){
        ItemStack Bow=new ItemStack(Material.BOW);
        ItemMeta BowMeta=Bow.getItemMeta();
        if(isMobius){
            BowMeta.setDisplayName("Äª±ÈÎÚË¹¼ý´ü");
        }else{
            BowMeta.setDisplayName("á÷ÁÔÏÝÚå");
        }
        Bow.setItemMeta(BowMeta);
        //ÉèÖÃ¹­ÄÍ¾Ã
        Damageable bd=(Damageable) Bow.getItemMeta();
        bd.setDamage(Bow.getType().getMaxDurability()-Amount);
        Bow.setItemMeta((ItemMeta) bd);
        //¸ø¹­ºÍ¹­¼ý¸½Ä§,°ó5
        Bow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(Bow);
        return Bow;
    }

    public static ItemStack getShadow_Shot_Arrow(boolean isMobius){
        ItemStack Arrow=new ItemStack(Material.ARROW);
        ItemMeta ArrowMeta=Arrow.getItemMeta();
        if(isMobius){
            ArrowMeta.setDisplayName("Äª±ÈÎÚË¹¹­¼ý");
            Arrow.setItemMeta(ArrowMeta);
            Arrow.setAmount(2);
            Arrow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        }else{
            ArrowMeta.setDisplayName("á÷ÁÔÏÝÚå¹­¼ý");
            Arrow.setItemMeta(ArrowMeta);
        }
        itemStacks.add(Arrow);
        return Arrow;
    }
    public static ItemStack getShadow_Shot_Arrow(boolean isMobius,int Amount){
        ItemStack Arrow=new ItemStack(Material.ARROW);
        ItemMeta ArrowMeta=Arrow.getItemMeta();
        ArrowMeta.setDisplayName("Äª±ÈÎÚË¹¹­¼ý");
        Arrow.setItemMeta(ArrowMeta);
        Arrow.setAmount(Amount);
        Arrow.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(Arrow);
        return Arrow;
    }

    public static ItemStack getGathering_Storm_Trident(){
        ItemStack Trident=new ItemStack(Material.TRIDENT);
        Trident.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta TridentMeta=Trident.getItemMeta();
        TridentMeta.setDisplayName("²æ×Ó");
        Trident.setItemMeta(TridentMeta);
        itemStacks.add(Trident);
        return Trident;
    }

    public static ItemStack getNova_Bomb(){
        ItemStack sb=new ItemStack(Material.SNOWBALL);
        sb.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta sbMate=sb.getItemMeta();
        sbMate.setDisplayName("ÐÂÐÇÕ¨µ¯-ÔÖ±ä");
        sb.setItemMeta(sbMate);
        itemStacks.add(sb);
        return sb;
    }
    public static ItemStack getVortex_Bomb(){
        ItemStack sb=new ItemStack(Material.SNOWBALL);
        sb.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta sbMate=sb.getItemMeta();
        sbMate.setDisplayName("ÐÂÐÇÕ¨µ¯-ÎÐÁ÷");
        sb.setItemMeta(sbMate);
        itemStacks.add(sb);
        return sb;
    }

    public static ItemStack getGolden_Gun(boolean is3){
        ItemStack Gun=new ItemStack(Material.GOLDEN_HOE);
        Gun.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta GunMeta=Gun.getItemMeta();
        if(is3){
            GunMeta.setDisplayName("¾«×¼»Æ½ðÇ¹");
            Gun.setAmount(3);
        }else{
            GunMeta.setDisplayName("ÁÒÑæ»Æ½ðÇ¹");
            Gun.setAmount(6);
        }
        Gun.setItemMeta(GunMeta);
        itemStacks.add(Gun);
        return Gun;
    }

    public static ItemStack getSpectral_Blades_Sword(){
        ItemStack sword=new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta=sword.getItemMeta();
        swordMeta.setDisplayName("¡ìf¹íÁéÀûÈÐ");
        sword.setItemMeta(swordMeta);
        sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(sword);
        return sword;
    }

    public static ItemStack getSentinel_Shield(){
        ItemStack Shield=new ItemStack(Material.SHIELD);
        Shield.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        Shield.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,5);
        Shield.addUnsafeEnchantment(Enchantment.DURABILITY,5);
        ItemMeta ShieldMate=Shield.getItemMeta();
        ShieldMate.setDisplayName("¡ìfÊ¥¶Ü");
        Shield.setItemMeta(ShieldMate);
        itemStacks.add(Shield);
        return Shield;
    }
    public static ItemStack getSentinel_Sword(){
        ItemStack Sword=new ItemStack(Material.DIAMOND_SWORD);
        Sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        ItemMeta SwordMate=Sword.getItemMeta();
        SwordMate.setDisplayName("¡ìfÊ¥½£");
        Sword.setItemMeta(SwordMate);
        itemStacks.add(Sword);
        return Sword;
    }

    public static ItemStack getDaybreak_Sword(){
        ItemStack Sword=new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SwordMata=Sword.getItemMeta();
        SwordMata.setDisplayName("ÀèÃ÷");
        List<String> lore=new LinkedList<>();
        lore.add("¡ìeÎÒ½«»áÊÇÀèÃ÷ÖÐµÄ·ï»Ë£¡");
        lore.add("¡ìeÊÀ½ç½«»á±»ÎÒÕÕÁÁ£¡");
        lore.add("¡ìeÀèÃ÷Ö®½£½«ÊØ»¤ÊÀ½ç£¡");
        SwordMata.setLore(lore);
        Sword.setItemMeta(SwordMata);
        Sword.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(Sword);
        return Sword;
    }

    public static ItemStack getBurning_Big_Axe(){
        ItemStack axe=new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta=axe.getItemMeta();
        meta.setDisplayName("¡ì6È¼ÉÕ¶Í´¸");
        axe.setItemMeta(meta);
        axe.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(axe);
        return axe;
    }
    public static ItemStack getHammer_of_SolAxe(){
        ItemStack axe=new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta=axe.getItemMeta();
        meta.setDisplayName("¡ì6Ñ×ÑôÖ®´¸");
        axe.setItemMeta(meta);
        axe.addUnsafeEnchantment(Enchantment.BINDING_CURSE,5);
        itemStacks.add(axe);
        return axe;
    }
}
