package at.chaosfield.openradio.common.tileentity;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.IGridNode;
import appeng.me.cache.helpers.ConnectionWrapper;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.interfaces.ILaserAddon;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class AEEncoderTileEntity extends AENetworkTile implements ILaserAddon {

    LaserTileEntity laser = null;

    ConnectionWrapper connection;

    AEEncoderTileEntity otherAEEncoder = null;

    @TileEvent(TileEventType.TICK)
    public void onTick() {
        if (!worldObj.isRemote) {
            boolean foundOther = false;
            if (this.laser != null)
                if (!this.laser.isInvalid()) {
                    if (laser.isConnected()) {
                        TileEntity other = DimensionManager.getWorld(laser.getOtherLaser().getDim()).getTileEntity(laser.getOtherLaser().getX(), laser.getOtherLaser().getY(), laser.getOtherLaser().getZ());
                        if (other instanceof LaserTileEntity) {
                            if (((LaserTileEntity) other).isConnected()) {
                                for (ILaserAddon addon : ((LaserTileEntity) other).getAddons()) {
                                    if (addon != null) {
                                        if (addon.getTileEntity() instanceof AEEncoderTileEntity) {
                                            connectToAEEncoder((AEEncoderTileEntity) addon.getTileEntity());
                                            foundOther = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            if (!foundOther)
                disconnectFromAEEncoder();
        }
    }

    private void connectToAEEncoder(AEEncoderTileEntity encoder) {
        this.otherAEEncoder = encoder;

        if (otherAEEncoder != null) {
            if (!otherAEEncoder.isInvalid()) {
                if (this.connection != null && this.connection.connection != null) {
                    IGridNode a = this.connection.connection.a();
                    IGridNode b = this.connection.connection.b();
                    IGridNode sa = this.getActionableNode();
                    IGridNode sb = otherAEEncoder.getActionableNode();
                    if ((a == sa || b == sa) && (a == sb || b == sb)) {
                        return;
                    }
                }

                try {
                    if (this.connection != null) {
                        if (this.connection.connection != null) {
                            this.connection.connection.destroy();
                            this.connection = new ConnectionWrapper(null);
                        }
                    }

                    if (otherAEEncoder.connection != null) {
                        if (otherAEEncoder.connection.connection != null) {
                            otherAEEncoder.connection.connection.destroy();
                            otherAEEncoder.connection = new ConnectionWrapper(null);
                        }
                    }

                    this.connection = otherAEEncoder.connection = new ConnectionWrapper(AEApi.instance().createGridConnection(this.getActionableNode(), otherAEEncoder.getActionableNode()));
                } catch (FailedConnection e) {
                }
            } else {
                disconnectFromAEEncoder();
            }
        } else {
            disconnectFromAEEncoder();
        }
    }

    private void disconnectFromAEEncoder() {
        this.otherAEEncoder = null;
        if (this.connection != null)
            if (this.connection.connection != null) {
                this.connection.connection.destroy();
                this.connection.connection = null;
            }
        this.connection = null;
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
