package at.chaosfield.openradio;

import at.chaosfield.openradio.common.init.Blocks;
import at.chaosfield.openradio.common.init.Crafting;
import at.chaosfield.openradio.common.init.Entities;
import at.chaosfield.openradio.common.init.Items;
import at.chaosfield.openradio.gui.GuiHandler;
import at.chaosfield.openradio.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

@Mod(name = "Open Radio", modid = OpenRadio.MODID, version = "0.4.0", modLanguage = "java", dependencies = "required-after:OpenComputers@[1.5.0,)")
public class OpenRadio{

    public static final String MODID = "openradio";

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
        Blocks.init();                  //Register all Blocks
        Items.init();                   //Register all Items
        logger.info("Pre init complete.");
    }

    //FML Init
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        GuiHandler.init();              //Register the GUIs
        Entities.init();                //Register the Entities
        Crafting.init();                //Register the crafting recipes
        proxy.registerTileEntities();   //Register the TileEntities
        proxy.registerRenders();
        proxy.registerSounds();

        logger.info("Init complete.");
    }
}
