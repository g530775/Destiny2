package Destiny2.Variable_Construction;

import Destiny2.DestinyMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Exp {
    public final static boolean Timer_Enable=true;
    private int Lv;
    private float Exp;

    public Exp(Player p){
        Lv=p.getLevel();
        Exp=p.getExp();
    }

    public Exp(){
        Lv=0;
        Exp=0;
    }

    public void setLv(int Lv){
        this.Lv=Lv;
    }
    public void setExp(float Exp){
        this.Exp=Exp;
    }

    public int getLv(){
        return Lv;
    }
    public float getExp(){
        return Exp;
    }

    public static void CoolTime(int Time,Player Player){
        Player.setLevel(Time+1);
        Player.setExp(1.0f);
        boolean initial=false;
        if(Timer_Enable){
            BukkitRunnable timer=new BukkitRunnable() {
                float time=Time*20;
                final Player player=Player;
                @Override
                public void run() {
                    if(time<=0||this.isCancelled()){
                        CoolTimeDone(player);
                        this.cancel();
                    }
                    if(player.getExp()>=0.00){
                        player.setExp(time/Time/20);
                    }
                    if(time%20==0&&player.getLevel()>=1){
                        player.setLevel(player.getLevel()-1);
                    }
                    time--;
                }
            };
            timer.runTaskTimer(DestinyMain.getPlugin(DestinyMain.class),0,1);
        }

    }

    public static void CoolTimeDone(Player p){
        if(DestinyMain.Player_Exp.containsKey(p)){
            p.setExp(DestinyMain.Player_Exp.get(p).getExp());
            p.setLevel(DestinyMain.Player_Exp.get(p).getLv());
            DestinyMain.Player_Exp.remove(p);
        }
    }
}
