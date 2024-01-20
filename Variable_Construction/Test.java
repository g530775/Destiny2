package Destiny2.Variable_Construction;

import Destiny2.DestinyMain;
public class Test {

    public double getX(){
        return DestinyMain.Config.getDouble("Test.X");
    }
    public double getY(){
        return DestinyMain.Config.getDouble("Test.Y");
    }
    public double getZ(){
        return DestinyMain.Config.getDouble("Test.Z");
    }
    public float getv1(){
        return (float)DestinyMain.Config.getDouble("Test.v1");
    }
    public float getv2(){
        return (float)DestinyMain.Config.getDouble("Test.v2");
    }
}
