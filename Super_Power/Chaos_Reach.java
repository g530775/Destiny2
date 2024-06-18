package Destiny2.Super_Power;

import Destiny2.DestinyMain;
import Destiny2.Misc.Laser;
import Destiny2.Super_Power_Chooser;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Chaos_Reach {
    public static Map<Player, LaserRunnable> lasers =new HashMap<>();

    public static class LaserRunnable extends BukkitRunnable {
        public static final byte LOADING_TIME = 30;
        public static final byte RANGE = 10;

        public final Laser.GuardianLaser laser;
        public final Player p;

        public byte loading = 0;

        public LaserRunnable(Player p) throws ReflectiveOperationException{
            this.p = p;
            this.laser = new Laser.GuardianLaser(p.getLocation(), p.getLocation().add(0, 1, 0), -1, 50);
            this.laser.start(DestinyMain.getPlugin(DestinyMain.class));
        }

        @Override
        public void run(){
            if (loading != LOADING_TIME){
                loading++;
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.7f, loading == LOADING_TIME ? 1.5f : 0.2f);
            }
            try{
                laser.moveStart(p.getLocation().add(0, 0.8, 0));
                laser.moveEnd(p.getLocation().add(0, 1.2, 0).add(p.getLocation().getDirection().multiply(loading == LOADING_TIME ? RANGE : loading / (LOADING_TIME / RANGE * 1.3))));
            }catch (ReflectiveOperationException e){
                e.printStackTrace();
            }
        }

        @Override
        public synchronized void cancel() throws IllegalStateException{
            laser.stop();
            lasers.remove(p);
            Super_Power_Chooser.remove_User_List(p);
            p.setGravity(true);
            super.cancel();
        }
    }

    public static void Chaos_Reach_Start(Player p){
        if(Super_Power_Chooser.add_User_List(p)){
            return;
        }
//        if (lasers.containsKey(p)){
//            lasers.get(p).cancel();
//        }else {
//            try{
//                lasers.put(p, new Chaos_Reach.LaserRunnable(p));
//                lasers.get(p).runTaskTimer(DestinyMain.getPlugin(DestinyMain.class), 5, 1);
//            }catch (ReflectiveOperationException e){
//                e.printStackTrace();
//            }
//        }
        if (!lasers.containsKey(p)) return;

        Chaos_Reach.LaserRunnable run = lasers.get(p);
        if (run.loading != Chaos_Reach.LaserRunnable.LOADING_TIME) return;
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
        run.loading = 0;

        for (Block blc : p.getLineOfSight(null, Chaos_Reach.LaserRunnable.RANGE / 2)){
            for (Entity en : p.getWorld().getNearbyEntities(blc.getLocation(), 1, 1, 1)){
                if (en instanceof Player) continue;
                if (en instanceof LivingEntity){
                    ((LivingEntity) en).damage(20, p);
                    en.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, en.getLocation(), 4, 1, 1, 1, 0.1);
                }
            }
        }
        p.getWorld().spawnParticle(Particle.SMOKE_LARGE, run.laser.getEnd(), 5);
        try {
            run.laser.callColorChange();
        }catch (ReflectiveOperationException e1) {
            e1.printStackTrace();
        }
        p.setGravity(false);
        p.setVelocity(p.getVelocity().zero());

    }
}
