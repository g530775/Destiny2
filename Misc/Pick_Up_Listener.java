package Destiny2.Misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class Pick_Up_Listener implements Listener {
    final static String[] tags={"Gathering_Storm_Trident","Golden_Gun_Ammo"};
    //禁止拾取
    @EventHandler
    public void PickUp(PlayerPickupArrowEvent e){
        for(String s:tags){
            if(e.getItem().getScoreboardTags().contains(s)){
                e.setCancelled(true);
            }
        }
    }
}
