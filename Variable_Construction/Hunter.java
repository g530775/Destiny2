package Destiny2.Variable_Construction;

import Destiny2.DestinyMain;

public class Hunter {
    private final String Arc="Super_Power.Arc.";
    private final String Void="Super_Power.Void.";
    private final String Gathering_Storm=Arc+"Hunter.Gathering_Storm.";
    private final String Shadow_Shot=Void+"Hunter.Shadow_Shot.";
    //Arc
    public int getGathering_Storm_Lightning_Interval_Time(){
        return DestinyMain.Config.getInt(Gathering_Storm+"Lightning.Interval_Time");
    }
    public int getGathering_Storm_Lightning_Times(){
        return DestinyMain.Config.getInt(Gathering_Storm+"Lightning.Times");
    }
    public boolean getWhether_Play_Sound(){
        return DestinyMain.Config.getBoolean(Gathering_Storm+"Play_Sound");
    }
    public boolean getAuto_Aim_Enable(){
        return DestinyMain.Config.getBoolean(Gathering_Storm+"Auto_Aim.Enable");
    }
    public boolean getAuto_Aim_Self(){
        return DestinyMain.Config.getBoolean(Gathering_Storm+"Auto_Aim.Aim_Self");
    }
    public int getSuper_Power_Activate_Time(){
        return DestinyMain.Config.getInt(Gathering_Storm+"Super_Power_Activate_Time");
    }
    //Void
    public boolean getMobius_Quiver_Whether_Solo_Arrow(){
        return DestinyMain.Config.getBoolean(Shadow_Shot+"Mobius_Quiver.Solo_Arrow");
    }
}
