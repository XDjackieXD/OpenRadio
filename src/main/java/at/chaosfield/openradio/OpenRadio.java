package at.chaosfield.openradio;

import at.chaosfield.openradio.common.entity.LaserEntity;
import at.chaosfield.openradio.common.init.Blocks;
import at.chaosfield.openradio.gui.GuiHandler;
import at.chaosfield.openradio.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

@Mod(name = "Open Radio", modid = OpenRadio.MODID, version = "0.1", modLanguage = "java", dependencies = "required-after:OpenComputers@[1.5.0,)")
public class OpenRadio{

    public static final String MODID = "openradio";

    public static int energyBuffer = 100;
    public static int energyMultiplier = 1;

    @Mod.Instance(MODID)
    public static OpenRadio instance;

    //Get the logger
    public static Logger logger = LogManager.getLogger(OpenRadio.MODID);

    //Get the right proxy (Client = ClientProxy, Server = CommonProxy)
    @SidedProxy(clientSide = "at.chaosfield.openradio.proxy.ClientProxy", serverSide = "at.chaosfield.openradio.CommonProxy")
    public static CommonProxy proxy;

    //FML PreInit
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger.info("Pre init complete.");
    }

    //FML Init
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        GuiHandler.init();              //Register the GUIs
        EntityRegistry.registerModEntity(LaserEntity.class, OpenRadio.MODID + ".laserentity", 1, this, 80, 3, true); //temp for testing :)
        proxy.registerTileEntities();   //Register all TileEntities
        proxy.registerRenders();
        proxy.registerSounds();
        Blocks.init();                  //Register all Blocks
        logger.info("Init complete.");
    }
}
