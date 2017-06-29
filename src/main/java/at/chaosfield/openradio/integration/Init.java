package at.chaosfield.openradio.integration;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.integration.actuallyAdditions.BookletEntry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Init{
    public static int minActAddVersion = 33;
    public static int loadedActAddVersion = 0;
    public static String[] actAddLaserRelayEnergy = {"actuallyadditions:block_laser_relay", "actuallyadditions:block_laser_relay_advanced", "actuallyadditions:block_laser_relay_extreme"};
    public static String[] actAddLaserRelayItem = {"actuallyadditions:block_laser_relay_item", "actuallyadditions:block_laser_relay_item_whitelist"};
    public static String[] actAddLaserRelayFluid = {"actuallyadditions:block_laser_relay_fluids"};

    public static void preInitIntegration(FMLPreInitializationEvent event){

    }

    public static void initIntegration(FMLInitializationEvent event){

    }

    public static void postInitIntegration(FMLPostInitializationEvent event){
        if(Init.loadedActAddVersion >= Init.minActAddVersion){
            OpenRadio.logger.info("actually adding some features... (loaded Actually Additions integration with api level " + Init.loadedActAddVersion + ")");
            BookletEntry.postInit();
        } else {
            OpenRadio.logger.info("Actually Additions API version " + Init.loadedActAddVersion + " found but minimum required version for integration is " + Init.minActAddVersion + ".");
        }
    }
}
