package at.chaosfield.openradio.integration.actuallyAdditions;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.integration.Init;
import at.chaosfield.openradio.interfaces.ILaserAddon;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.laser.IConnectionPair;
import de.ellpeck.actuallyadditions.api.laser.LaserType;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserRelay implements ILaserAddon{

    private TileEntity laserRelayTile;
    private LaserTileEntity laserTileEntity;
    private boolean connected = false;
    private String addonName;

    public LaserRelay(TileEntity laserRelayTile){
        this.laserRelayTile = laserRelayTile;
        String tileName = laserRelayTile.getBlockType().getRegistryName().toString();

        for(String name: Init.actAddLaserRelayEnergy)
            if(name.equals(tileName))
                this.addonName = "LaserRelayEnergy";
        for(String name: Init.actAddLaserRelayItem)
            if(name.equals(tileName))
                this.addonName = "LaserRelayItem";
    }

    @Override
    public void connectToLaser(LaserTileEntity laser){
        this.laserTileEntity = laser;
        if(laser != null)
            if(laser.isConnected())
                connectLaserRelays(laser);
            else
                disconnectLaserRelays(laser);
    }

    @Override
    public void disconnectFromLaser(LaserTileEntity laser){
        if(laserTileEntity != null)
            disconnectLaserRelays(laserTileEntity);
    }

    @Override
    public void laserConnectionStatusChanged(boolean connected){
        if(connected != this.connected && laserTileEntity != null){
            if(connected){
                connectLaserRelays(this.laserTileEntity);
            }else{
                disconnectLaserRelays(this.laserTileEntity);
            }
            this.connected = connected;
        }
    }

    private void connectLaserRelays(LaserTileEntity laser){
        if(this.laserRelayTile != null && laser != null){
            TileEntity tile = laser.getWorld().getTileEntity(laser.getOtherLaser().getPos());
            if(tile instanceof LaserTileEntity){
                if(((LaserTileEntity) tile).isConnected())
                    for(ILaserAddon addon : ((LaserTileEntity) tile).getAddons())
                        if(addon != null && addon.getAddonName().equals(this.addonName) && addon.getTile() != null)
                            ActuallyAdditionsAPI.connectionHandler.addConnection(this.laserRelayTile.getPos(), addon.getTile().getPos(), laser.getWorld(), true);
            }
        }
    }

    private void disconnectLaserRelays(LaserTileEntity laser){
        if(this.laserRelayTile != null && laser != null){
            List<BlockPos> otherRelays = new ArrayList<BlockPos>();

            if(laser.getOtherLaser() != null){
                TileEntity tile = laser.getWorld().getTileEntity(laser.getOtherLaser().getPos());
                if(tile != null){
                    if(tile instanceof LaserTileEntity){
                        for(ILaserAddon addon : ((LaserTileEntity) tile).getAddons())
                            if(addon != null && addon.getAddonName().equals(this.addonName) && addon.getTile() != null)
                                otherRelays.add(addon.getTile().getPos());
                    }

                    ConcurrentSet<IConnectionPair> connections = ActuallyAdditionsAPI.connectionHandler.getConnectionsFor(this.laserRelayTile.getPos(), this.laserRelayTile.getWorld());
                    List<IConnectionPair> newConnections = new ArrayList<IConnectionPair>();

                    for(IConnectionPair connectionPair : connections){
                        for(BlockPos otherRelay : otherRelays)
                            if(!connectionPair.contains(otherRelay))
                                newConnections.add(connectionPair);
                    }

                    ActuallyAdditionsAPI.connectionHandler.removeRelayFromNetwork(this.laserRelayTile.getPos(), this.laserRelayTile.getWorld());
                    for(IConnectionPair connectionPair : newConnections){
                        ActuallyAdditionsAPI.connectionHandler.addConnection(connectionPair.getPositions()[0], connectionPair.getPositions()[1], LaserType.ENERGY, this.laserRelayTile.getWorld());
                    }
                }
            }
        }
    }

    @Override
    public int getEnergyUsage(){
        return OpenRadio.instance.settings.ActAddLaserRelayUsage;
    }

    @Override
    public TileEntity getTile(){
        return laserRelayTile;
    }

    @Override
    public String getAddonName(){
        return this.addonName;
    }
}
