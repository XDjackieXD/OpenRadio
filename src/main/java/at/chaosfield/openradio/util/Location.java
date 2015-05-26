package at.chaosfield.openradio.util;

import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD
 */
public class Location{
    private int x, y, z;
    private World world;

    public Location(World world, int x, int y, int z){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }

    public World getWorld(){
        return world;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setZ(int z){
        this.z = z;
    }

    public void setWorld(World world){
        this.world = world;
    }
}
