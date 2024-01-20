package Destiny2.Variable_Construction;

import org.bukkit.entity.Player;

public class Exp {
    private int Lv;
    private float Exp;
    public Exp(int Lv, float Exp){
        this.Lv=Lv;
        this.Exp=Exp;
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
}
