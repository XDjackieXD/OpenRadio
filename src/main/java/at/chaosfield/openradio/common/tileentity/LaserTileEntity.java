package at.chaosfield.openradio.common.tileentity;

import at.chaosfield.openradio.OpenRadio;
import cpw.mods.fml.common.Optional;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import javax.naming.Context;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class LaserTileEntity extends TileEntityEnvironment{
    boolean powered;
    int laserPower = 10;
    double latency = 0;

    public LaserTileEntity(){
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create();
    }

    public String getComponentName(){
        return "Laser";
    }

    @Callback(direct = true, doc = "function():double -- Get the current power amount")
    public Object[] power(Context context, Arguments args){
        return new Object[]{((Connector) node()).localBuffer()};
    }

    @Callback(direct = true, doc = "function():double -- Get the current latency")
    public Object[] latency(Context context, Arguments args){
        return new Object[]{latency};
    }


    @Override
    public void updateEntity(){
        super.updateEntity();
        if(worldObj.isRemote) return;
        boolean lastStatus = powered;
        if((node() != null) && ((Connector) node()).tryChangeBuffer(laserPower / 10f * OpenRadio.energyMultiplier)){
            powered = true;
        }else{
            powered = false;
        }
    }

    public boolean isPowered(){
        return powered;
    }
}
