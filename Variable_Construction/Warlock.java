package Destiny2.Variable_Construction;

import Destiny2.DestinyMain;

public class Warlock {
    private final String Arc="Super_Power.Arc.";
    private final String Void="Super_Power.Void.";
    private final String Solar="Super_Power.Solar.";
    private final String Well_Of_Radiance=Solar+"Warlock.Well_Of_Radiance.";
    private final String Nova_Bomb=Void+"Warlock.Nova_Bomb.";

    public boolean getOnly_Recovery_Self(){
        return DestinyMain.Config.getBoolean(Solar+Well_Of_Radiance+"Only_Recovery_Self");
    }
    public double getHealth_Value(){
        return DestinyMain.Config.getDouble(Solar+Well_Of_Radiance+"Recovery_Health.Value");
    }
    public int getStand_Time(){
        return DestinyMain.Config.getInt(Solar+Well_Of_Radiance+"Stand_Time");
    }
    public double getNova_Bomb_Cataclysm_Explode_Radius(){
        return DestinyMain.Config.getDouble(Void+Nova_Bomb+"Cataclysm.Explode_Radius");
    }
    public double getNova_Bomb_Vortex_Explode_Radius(){
        return DestinyMain.Config.getDouble(Void+Nova_Bomb+"Vortex.Explode_Radius");
    }
}
