package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBlaze;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Hammer_of_Sol implements Listener {
    public static LinkedList<Player> Hammer_of_Sol_User=new LinkedList<>();

    public static final float HoS_Explode_Range=3.50f;
    public static final double HoS_Explode_Damage=25.00;
    public static Map<ArmorStand,Player> HoS_Armor_Stands=new HashMap<>();
    public static final double[] HoS_Fire_Range={3.50,2.00,3.50};

    public static boolean add(Player p){
        if(!Hammer_of_Sol_User.contains(p)){
            Hammer_of_Sol_User.add(p);
            return false;
        }else{
            return true;
        }
    }

    public static void List_Clean(){
        HoS_Armor_Stands.clear();
    }

    @EventHandler
    public void Damage(EntityDamageByEntityEvent edbee){
        if(edbee.getCause()== EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            if (edbee.getDamager() instanceof ArmorStand as && as.getScoreboardTags().contains("Hammer_of_Sol")) {
                edbee.setCancelled(true);
                if (edbee.getDamager() instanceof LivingEntity le) {
                    Misc.Damage(le, HoS_Explode_Damage, HoS_Armor_Stands.get(as));
                }
            }
        }
    }

    @EventHandler
    public void Check(PlayerInteractEvent pie){
        Player p=pie.getPlayer();
        if(Hammer_of_Sol_User.contains(p)){
            pie.setCancelled(true);
            ArmorStand as=(ArmorStand)p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.ARMOR_STAND);
            as.addScoreboardTag("Clean");
            as.setInvulnerable(true);
            as.setVisible(false);
            as.setBasePlate(false);
            as.setCustomName(p.getCustomName());
            as.addScoreboardTag("No_Break");
            as.addScoreboardTag(p.getUniqueId().toString());
            as.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_AXE));
            as.setCollidable(false);
            as.setVelocity(p.getLocation().getDirection().multiply(1.5f));
            as.addScoreboardTag("Hammer_of_Sol");
            as.setRotation(p.getLocation().getPitch(),0);
            as.teleport(as.getLocation().add(0,p.getEyeHeight()/2,0).setDirection(p.getLocation().getDirection()));
            as.setRightArmPose(new EulerAngle(3.65,0,0));
            Throw(as,p);
        }
    }

    public static void Throw(ArmorStand as, Player player){
        BukkitRunnable rotate=new BukkitRunnable() {
            float rotation=0;
            int time=0;
            @Override
            public void run() {
                as.setRightArmPose(as.getRightArmPose().add(0.25,0,0));
                if(as.isOnGround()){
                    HoS_Armor_Stands.put(as,player);
                    Misc.Make_Explode(as,HoS_Explode_Range);
//                    LinkedList<LivingEntity> mobs=Misc.NearlyTargets(as,HoS_Fire_Range[0],HoS_Fire_Range[1],HoS_Fire_Range[2]);
                    this.cancel();
                    as.remove();
                    return;
                }
                as.setVelocity(as.getVelocity().setY(-0.2));
                if(time>300||as.isDead()){
                    as.remove();
                    this.cancel();
                }
                time++;
            }
        };
        rotate.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }
}
