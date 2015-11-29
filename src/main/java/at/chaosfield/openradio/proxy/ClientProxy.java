package at.chaosfield.openradio.proxy;

import at.chaosfield.openradio.render.LaserInventoryRender;
import at.chaosfield.openradio.render.LaserTESR;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import at.chaosfield.openradio.util.RenderUtil;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void registerRenders(){
        RenderUtil.laserRenderID = RenderingRegistry.getNextAvailableRenderId();

        ClientRegistry.bindTileEntitySpecialRenderer(LaserTileEntity.class, new LaserTESR());
        RenderingRegistry.registerBlockHandler(new LaserInventoryRender(RenderUtil.laserRenderID));
    }

    @Override
    public void registerSounds(){

    }
}
