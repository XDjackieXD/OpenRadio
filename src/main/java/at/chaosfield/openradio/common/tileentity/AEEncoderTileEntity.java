package at.chaosfield.openradio.common.tileentity;

import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import at.chaosfield.openradio.interfaces.ILaserAddon;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class AEEncoderTileEntity extends AENetworkTile implements ILaserAddon{

    LaserTileEntity laser = null;

    @TileEvent(TileEventType.TICK)
    public void onTick(){
        if(!worldObj.isRemote) {
            if(this.laser != null)
                if (this.laser.isInvalid())
                    this.laser = null;
        }
    }

    @Override
    public void connectToLaser(LaserTileEntity laser) {
        this.laser = laser;
    }

    @Override
    public void disconnectFromLaser(LaserTileEntity laser) {
        this.laser = null;
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }
}
