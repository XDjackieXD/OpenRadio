package at.chaosfield.openradio.interfaces;

import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public interface ILaserAddon {
    void connectToLaser(LaserTileEntity laser);
    void disconnectFromLaser(LaserTileEntity laser);
    TileEntity getTileEntity();
    String getAddonName();
}
