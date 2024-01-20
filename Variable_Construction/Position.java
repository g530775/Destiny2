package Destiny2.Variable_Construction;

import org.bukkit.Location;

public class Position {
    private Location Old;
    private Location New;

    public void setOld(Location l){
        Old=l;
    }
    public void setNew(Location n){
        New=n;
    }
    public Location getOld(){
        return Old;
    }
    public Location getNew(){
        return New;
    }
}
