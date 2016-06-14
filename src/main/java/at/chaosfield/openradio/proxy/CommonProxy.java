package at.chaosfield.openradio.proxy;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class CommonProxy{

    public void init(FMLInitializationEvent event){
        GameRegistry.registerTileEntityWithAlternatives(LaserTileEntity.class,  OpenRadio.MODID + ":laser");
        //if(Loader.isModLoaded("appliedenergistics2"))
        //    GameRegistry.registerTileEntity(AEEncoderTileEntity.class, OpenRadio.MODID + ":aeencoder");
    }
    public void preInit(FMLPreInitializationEvent event){}

}
