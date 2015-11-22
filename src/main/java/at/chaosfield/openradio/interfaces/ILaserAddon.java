package at.chaosfield.openradio.interfaces;

import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public interface ILaserAddon {

    public void connectToLaser(LaserTileEntity laser);
    public void disconnectFromLaser(LaserTileEntity laser);
    public TileEntity getTileEntity();
    public String getAddonName();

}
