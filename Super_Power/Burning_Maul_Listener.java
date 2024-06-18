package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Misc;
import Destiny2.Misc.Summon;
import net.minecraft.server.v1_16_R3.EntityBlaze;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftBlaze;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Burning_Maul_Listener implements Listener {
    public static LinkedList<Player> Burning_Maul_User=new LinkedList<>();
    public static Map<Blaze,Player> Burning_Maul_Blaze=new HashMap<>();
    public static Map<ArmorStand,Player> Burning_Maul_Armors=new HashMap<>();
    public static LinkedList<Player> Attacker=new LinkedList<>();
    public static double[] Burning_Maul_Range={1.50,2.00,1.50};
    public static double[] Burning_Maul_Normal_Range={2.50,2.00,2.50};
    public static double[] Burning_Maul_Track_Range={3.00,2.00,3.00};
    public static final int Burning_Maul_Fire_Max_Time=100;
    public static final float Burning_Maul_Explode_Range=2.50f;
    public static final int Burning_Maul_Normal_Attack_Time=24;
    public static final double Fall_Damage=25;
    public static final double Normal_Damage=9.00;

    public static boolean add(Player p){
        if(!Burning_Maul_User.contains(p)){
            Burning_Maul_User.add(p);
            return false;
        }else{
            return true;
        }
    }

    public static void List_Clean(){
        Burning_Maul_Blaze.clear();
        Burning_Maul_Armors.clear();
        Attacker.clear();
    }


    @EventHandler
    public void Explode(EntityDamageByEntityEvent edbee){
        if(edbee.getDamager() instanceof Blaze b&&b.getScoreboardTags().contains("Burning_Maul")
                &&edbee.getEntity() instanceof LivingEntity le){
            if(b.getScoreboardTags().contains(le.getUniqueId().toString())){
                edbee.setCancelled(true);
            }else{
                Attacker.add(Burning_Maul_Blaze.get(b));
                Misc.Damage(le,Fall_Damage,Burning_Maul_Blaze.get(b));
                le.setFireTicks(100);
                return;
            }
        }
        if(edbee.getDamager() instanceof ArmorStand ar&&ar.getScoreboardTags().contains("Burning_Maul")
                &&edbee.getEntity() instanceof LivingEntity le){
            if(ar.getScoreboardTags().contains(le.getUniqueId().toString())){
                edbee.setCancelled(true);
            }else{
                Attacker.add(Burning_Maul_Armors.get(ar));
                Misc.Damage(le,Normal_Damage,Burning_Maul_Armors.get(ar));
                return;
            }
        }
        if(edbee.getDamager() instanceof Player p&&!Attacker.contains(p)&&Burning_Maul_User.contains(p)){
            edbee.setCancelled(true);
        }
        if(edbee.getDamager() instanceof Player p&&Attacker.contains(p)&&Burning_Maul_User.contains(p)){
            Attacker.remove(p);
        }
    }

    //摔落无伤
    @EventHandler
    public void No_Fall(EntityDamageEvent ede){
        if(ede.getEntity() instanceof Player p&&ede.getCause() == EntityDamageEvent.DamageCause.FALL
                &&Burning_Maul_User.contains(p)){
            ede.setCancelled(true);
        }
    }

    @EventHandler
    public void listener(PlayerInteractEvent pie){
        Player player=pie.getPlayer();
        if(Burning_Maul_User.contains(player)){
            if(pie.getAction()==Action.RIGHT_CLICK_BLOCK||pie.getAction()==Action.RIGHT_CLICK_AIR){
                if(player.hasCooldown(Material.GOLDEN_AXE)){
                    return;
                }
                player.setCooldown(Material.GOLDEN_AXE,72000);
                FallDamage(player);
                player.setVelocity(player.getVelocity().zero().setY(-1));
            }
            if(pie.getAction()==Action.LEFT_CLICK_BLOCK||pie.getAction()==Action.LEFT_CLICK_AIR){
                if(player.hasCooldown(Material.GOLDEN_AXE)){
                    return;
                }
                Normal_Attack(player);
            }
            pie.setCancelled(true);
        }
    }

    public void FallDamage(Player player){
        BukkitRunnable Fall=new BukkitRunnable() {
            int time=0;
            final int[] rgb={255,165,0};
            EntityBlaze b=null;
            Location location=null;
            Blaze blaze=null;
            Vector v=null;
            boolean Created=false;
            @Override
            public void run() {
                if(player.isOnGround()&&!Created){
                    player.setCooldown(Material.GOLDEN_AXE,10);
                    location = player.getLocation().add(player.getLocation().getDirection().setY(0).multiply(10));
                    v=player.getEyeLocation().getDirection().setY(0);
                    blaze=(Blaze) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLAZE);
                    blaze.setCollidable(false);
                    Burning_Maul_Blaze.put(blaze,player);
                    blaze.teleport(player);
                    b=((CraftBlaze)blaze).getHandle();
                    b.setGoalTarget(null);
                    blaze.addScoreboardTag(player.getUniqueId().toString());
                    blaze.addScoreboardTag("No_Track");
                    blaze.addScoreboardTag("Burning_Maul");
                    blaze.addScoreboardTag("Clean");
                    blaze.setInvulnerable(true);
                    blaze.setNoDamageTicks(500);
                    Created=true;
                }
                if(Created) {
                    b.setGoalTarget(null);
                    for (LivingEntity le : Misc.NearlyTargets(blaze, Burning_Maul_Track_Range[0], Burning_Maul_Track_Range[1], Burning_Maul_Track_Range[2])) {
                        if (blaze.getScoreboardTags().contains(le.getUniqueId().toString())) {
                            continue;
                        }
                        if (le.getLocation().distance(blaze.getLocation()) <= Burning_Maul_Range[0]) {
                            Misc.Make_Explode(blaze, Burning_Maul_Explode_Range);
                            blaze.remove();
                            this.cancel();
                            return;
                        }

                        Vector vs = (new Vector(le.getLocation().getX() - blaze.getLocation().getX(), 0, le.getLocation().getZ() - blaze.getLocation().getZ())).normalize();
                        if (vs.getX() * v.getX() + vs.getZ() * v.getZ() > 0) {
                            blaze.teleport(blaze.getLocation().setDirection(vs));
                            v = vs;
                            location = le.getLocation();
//                            Misc.navigateToo(blaze,le.getLocation(),2.0f);
                        }
                    }

                    Misc.navigateToo(blaze, location, 2.0f);
                    if(time>20&&(blaze.getVelocity().getZ()==0||blaze.getVelocity().getX()==0)){
                        Misc.Make_Explode(blaze, Burning_Maul_Explode_Range);
                        blaze.remove();
                        this.cancel();
                        return;
                    }

                    if (Created && time > Burning_Maul_Fire_Max_Time) {
                        blaze.remove();
                        this.cancel();
                        return;
                    }
                }
                time++;
                //防止目标锁定
                if(Created){
                    ((CraftBlaze) blaze).getHandle().setGoalTarget(null);
                    Summon.summon_Redstone_Particle(blaze.getLocation(),Burning_Maul_Range[0], 18,1,false,rgb);
                }

            }
        };
        Fall.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }

    public void Normal_Attack(Player player){
        BukkitRunnable Normal=new BukkitRunnable() {
            int time=0;
            final int[] rgb={255,165,0};
            Location location=null;
            ArmorStand as=null;
            final EulerAngle ea=new EulerAngle(0,3.14,1.57);
            boolean Created=false;
            float rotate=-45;
            @Override
            public void run() {
                if(!Created){
                    player.setCooldown(Material.GOLDEN_AXE,23);
                    location = player.getLocation().add(player.getEyeLocation().getDirection().multiply(10));
                    as=(ArmorStand) player.getWorld().spawnEntity(player.getLocation(),EntityType.ARMOR_STAND);
                    as.setSilent(true);
                    as.setCollidable(false);
                    as.setCustomName(player.getCustomName());
                    as.teleport(player);
                    as.setRightArmPose(ea);
                    as.setBasePlate(false);
                    as.setGravity(false);
                    as.setVisible(false);
                    as.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_AXE));
                    as.addScoreboardTag(player.getUniqueId().toString());
                    as.addScoreboardTag("No_Track");
                    as.addScoreboardTag("Burning_Maul");
                    as.addScoreboardTag("Clean");
                    as.setInvulnerable(true);
                    as.setNoDamageTicks(500);
                    Created=true;
                }
                as.teleport(player.getLocation().setDirection(as.getLocation().getDirection()));
                as.setRotation(rotate,0);
                rotate-=45;
                if(time%8==0&&time!=24){
                    for(LivingEntity le:Misc.NearlyTargets(as,Burning_Maul_Normal_Range[0],Burning_Maul_Normal_Range[1],Burning_Maul_Normal_Range[2])){
                        if(as.getScoreboardTags().contains(le.getUniqueId().toString())){
                            continue;
                        }
                        if(le.getLocation().distance(as.getLocation())<=Burning_Maul_Normal_Range[0]){
                            Misc.Damage(le,8.33,as);
                        }
                    }
                }

                if(Created&&time>Burning_Maul_Normal_Attack_Time){
                    as.remove();
                    this.cancel();
                    player.setGravity(true);
                    return;
                }

                time++;
                Summon.summon_Redstone_Particle(player.getLocation(),Burning_Maul_Normal_Range[0], 18,1,false,rgb);
            }
        };
        Normal.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
    }
}
