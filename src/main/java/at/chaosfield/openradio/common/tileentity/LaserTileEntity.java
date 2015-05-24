package at.chaosfield.openradio.common.tileentity;

import at.chaosfield.openradio.OpenRadio;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment{
    boolean powered;
    int laserPower = 10;
    double latency = 0;
    boolean connected = false;
    String uid = "";

    public LaserTileEntity(){
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create();
        uid = node().address();
    }

    public String getComponentName(){
        return "Laser";
    }

    @Callback(direct = true, doc = "function():double -- Get the current latency")
    public Object[] getLatency(Context context, Arguments args){
        return new Object[]{latency};
    }


    @Override
    public void updateEntity(){
        super.updateEntity();
        if(worldObj.isRemote) return;
        if(connected){
            if((node() != null) && ((Connector) node()).tryChangeBuffer(laserPower / 10f * OpenRadio.energyMultiplier)){
                powered = true;
            }else{
                powered = false;
            }
        }
    }

    public boolean isPowered(){
        return powered;
    }
}
