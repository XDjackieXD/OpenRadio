package at.chaosfield.openradio;

import at.chaosfield.openradio.init.Blocks;
import at.chaosfield.openradio.init.Crafting;
import at.chaosfield.openradio.init.Entities;
import at.chaosfield.openradio.init.Items;
import at.chaosfield.openradio.gui.GuiHandler;
import at.chaosfield.openradio.proxy.CommonProxy;
import at.chaosfield.openradio.util.Settings;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

@Mod(name = "Open Radio", modid = OpenRadio.MODID, version = "0.6.1", modLanguage = "java", dependencies = "required-after:OpenComputers@[1.5.0,)")
public class OpenRadio{

    public static final String MODID = "openradio";

    @Mod.Instance(MODID)
    public static OpenRadio instance;

    //Get the logger
    public static Logger logger = LogManager.getLogger(OpenRadio.MODID);

    //Get the right proxy (Client = ClientProxy, Server = CommonProxy)
    @SidedProxy(clientSide = "at.chaosfield.openradio.proxy.ClientProxy", serverSide = "at.chaosfield.openradio.proxy.CommonProxy")
    public static CommonProxy proxy;

    public Settings settings;

    //FML PreInit
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        settings = new Settings(event.getSuggestedConfigurationFile());

        Blocks.init();                  //Register all Blocks
        Items.init();                   //Register all Items
        proxy.preInit(event);           //Register Variants
        logger.info("Pre init complete.");
    }

    //FML Init
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        GuiHandler.init();              //Register the GUIs
        Entities.init();                //Register the Entities
        Crafting.init();                //Register the crafting recipes
        proxy.init(event);              //Register TileEntities, Renders and other things

        logger.info("Init complete.");
    }
}
