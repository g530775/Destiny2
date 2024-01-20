package Destiny2;

import Destiny2.Misc.*;
import Destiny2.Super_Power.*;
import Destiny2.Variable_Construction.Hunter;
import Destiny2.Variable_Construction.Test;
import Destiny2.Variable_Construction.Warlock;
import Destiny2.Variable_Construction.Titan;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.LinkedList;


public class DestinyMain extends JavaPlugin {

    public static String[] All_Tags={
            "No_Fall_Damage","No_Fire","Thunder_Crash_Lightning_Damage","Well_Of_Radiance_ArmorStand",
    "Shadow_Shot_Using","Shadow_Shot_Arrow_3","Shadow_Shot_Arrow_2","Shadow_Shot_Arrow_1","Gathering_Storm",
    "Gathering_Storm_Using","Nova_Bomb_Using","Nova_Bomb_Vortex","Nova_Bomb_Cataclysm","Golden_Gun_Using","Golden_Gun_3",
    "Golden_Gun_6","Golden_Gun_Ammo","Golden_Gun_Ammo3","Golden_Gun_Ammo6","Spectral_Blades_Using","Gathering_Storm",
    "Gathering_Storm_Trident", "Gathering_Storm_Lightning_Damage","Shadow_Shot_Weak","Arrow_1",
    "Arrow_2","Head_Shot","Sentinel_Shield_Using","Hammer_Of_Sol_Using","Shadow_Shot_DeadFall","Shadow_Shot_Mobius",
    "Arrow_DeadFall","Arrow_Mobius","Nova_Bomb_Shulker_Bullet","No_Float","Gathering_Storm_Hit","Daybreak_Using",
    "Daybreak_Ball"
    };
    public static String[] Using_Tag={
            "Shadow_Shot_Using","Gathering_Storm_Using","Nova_Bomb_Using","Spectral_Blades_Using","Sentinel_Shield_Using",
            "Daybreak_Using"

    };
    public static LinkedList<String> SpecialTag=new LinkedList<>();
    private static String[] Special_Tag={
            "Well_Of_Radiance_ArmorStand","Nova_Bomb","Gathering_Storm_Trident","Nova_Bomb_Shulker_Bullet",
            "Hammer_Of_Sol_ArmorStand","Daybreak_Ball"
    };
    public static Titan T = new Titan();
    public static Warlock W = new Warlock();
    public static Hunter H = new Hunter();
    public static Test Te=new Test();
    public static Summon P=new Summon();
    public static String PluginHead;
    public static FileConfiguration Config;
    @Override
    public void onEnable() {
        getLogger().info("插件已载入!");
        getLogger().info("本插件仅供娱乐学习,禁止用于其他用途.");
        getLogger().info("插件会修改玩家盔甲,装备等道具并且无法恢复,因此强烈建议用于娱乐服.");
        getLogger().info("本插件所用代码部分来自其他开源或闭源(已授权)作品");
        saveDefaultConfig();
        Config = getConfig();
        PluginHead=Config.getString("Plugin_Setting.Head").replace("&","§")+"§f";

        SpecialTag.addAll(Arrays.asList(Special_Tag));

        Bukkit.getPluginManager().registerEvents(new Super_Power_Chooser(), this);
        Bukkit.getPluginManager().registerEvents(new Damage_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Drop_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Shadow_Shot_Fix_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Golden_Gun_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Well_Of_Radiance_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Gathering_Storm_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Pick_Up_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Nova_Bomb_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Misc_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Spectral_Blades(),this);
        Bukkit.getPluginManager().registerEvents(new Sentinel_Shield_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Daybreak_Listener(),this);
        Bukkit.getPluginManager().registerEvents(new Thunder_Crash_Listener(),this);

        //Bukkit.getPluginManager().registerEvents(new Test_Listener(),this);
    }
    @Override
    public void onDisable(){
        clean();
        getLogger().info("插件已卸载!");
    }
    public static void clean(){
        LinkedList<Player> playerList=new LinkedList<>(Bukkit.getOnlinePlayers());
        for(Player player:playerList){
            for(String s:DestinyMain.All_Tags){
                player.getScoreboardTags().remove(s);
            }
            for(ItemStack item:player.getInventory().getContents()){
                if(item!=null&&item.getEnchantmentLevel(Enchantment.BINDING_CURSE)==5){
                    player.getInventory().remove(item);
                }
            }
            if(player.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.BINDING_CURSE)==5){
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            }
        }
    }

    private void loadConfig() {
        getLogger().info(PluginHead+"Loading config...");
        Config = getConfig();
        saveDefaultConfig();
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length!=0){
            if (sender.isOp()) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    loadConfig();
                    sender.sendMessage(PluginHead+"配置重载成功");
                } else {
                    sender.sendMessage(PluginHead+"未知的指令");
                }
            } else {
                sender.sendMessage(PluginHead+"您无权限");
            }
        } else {
            sender.sendMessage(PluginHead+"当我打出?时,不是我有问题,而是您有问题");
        }
        return true;
    }


}