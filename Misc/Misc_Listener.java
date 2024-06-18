package Destiny2.Misc;

import Destiny2.DestinyMain;
import Destiny2.Super_Power.Well_Of_Radiance_Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Misc_Listener implements Listener {
    static String[] Tags={"Golden_Gun_Using","Shadow_Shot_Using","Gathering_Storm_Using","Spectral_Blades_Using",
            "Sentinel_Shield_Using"
    };
    //灭火
    @EventHandler
    public void No_Fire(BlockIgniteEvent e){
        if(e.getCause()==BlockIgniteEvent.IgniteCause.LIGHTNING||e.getCause()== BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL){
            Entity entity=e.getIgnitingEntity();
            if(entity.getScoreboardTags().contains("No_Fire")){
                e.setCancelled(true);
            }
        }
    }
    //不准切换主副手
    @EventHandler
    public void No_Change_Main_Or_Off_Hand(PlayerSwapHandItemsEvent e){
        Player p=e.getPlayer();
        for(String s:Tags){
            if(p.getScoreboardTags().contains(s)){
                e.setCancelled(true);
            }
        }
        if(DestinyMain.Super_Power_User.contains(p)){
            e.setCancelled(true);
        }
    }
    //不准切换格子
    @EventHandler
    public void No_Change_Slot(PlayerItemHeldEvent e){
        Player p=e.getPlayer();
        if(DestinyMain.Super_Power_User.contains(p)){
            e.setCancelled(true);
        }
    }
    //免疫潜影贝子弹漂浮->Damage_Listener中

    //玩家死亡清理道具
    @EventHandler
    public void Player_Dead(PlayerDeathEvent pde){
        Player p=pde.getEntity();
        for(String s: DestinyMain.Using_Tag){
            if(p.getScoreboardTags().contains(s)){
                DestinyMain.clean();
            }
        }
    }

    //不准玩家破坏
    @EventHandler
    public void Player_Break(BlockBreakEvent bbe){
        if(DestinyMain.Super_Power_User.contains(bbe.getPlayer())){
            bbe.setCancelled(true);
        }
    }



}
