package at.chaosfield.openradio.proxy;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.tileentity.AEEncoderTileEntity;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class CommonProxy{



    public void registerTileEntities(){
        GameRegistry.registerTileEntity(LaserTileEntity.class, OpenRadio.MODID + ":laser");
        if(Loader.isModLoaded("appliedenergistics2"))
            GameRegistry.registerTileEntity(AEEncoderTileEntity.class, OpenRadio.MODID + ":aeencoder");
    }

    public void registerRenders(){}
    public void registerSounds(){}
}
