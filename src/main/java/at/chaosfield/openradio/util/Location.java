package at.chaosfield.openradio.util;

import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD
 */
public class Location{
    private int x, y, z, dim;

    public Location(int dim, int x, int y, int z){
        this.dim = dim;
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

    public int getDim(){
        return dim;
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

    public void setDim(int dim){
        this.dim = dim;
    }
}
