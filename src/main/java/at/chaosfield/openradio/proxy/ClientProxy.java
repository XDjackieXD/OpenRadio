package at.chaosfield.openradio.proxy;

import at.chaosfield.openradio.render.LaserTESR;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import cpw.mods.fml.client.registry.ClientRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void registerRenders(){
        ClientRegistry.bindTileEntitySpecialRenderer(LaserTileEntity.class, new LaserTESR());
    }

    @Override
    public void registerSounds(){

    }
}
