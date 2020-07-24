package at.chaosfield.openradio.util;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class LocationPair{
    private Location loc1, loc2;

    public LocationPair(Location loc1, Location loc2){
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public Location getLoc1(){
        return loc1;
    }

    public Location getLoc2(){
        return loc2;
    }

    public void setLoc1(Location loc1){
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2){
        this.loc2 = loc2;
    }
}
