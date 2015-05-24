package at.chaosfield.openradio;

import at.chaosfield.openradio.common.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

@Mod(name = "Open Radio", modid = OpenRadio.MODID, version = "0.1", modLanguage = "java", dependencies = "required-after:OpenComputers@[1.5.0,)")
public class OpenRadio {

    public static final String MODID = "openradio";

    public static int energyBuffer = 100;
    public static int energyMultiplier = 1;

    public static Logger logger = LogManager.getLogger(OpenRadio.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger.info("Pre init complete.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        Blocks.init();
        logger.info("Init complete.");
    }
}
