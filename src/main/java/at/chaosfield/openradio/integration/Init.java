package at.chaosfield.openradio.integration;

import at.chaosfield.openradio.integration.actuallyAdditions.BookletEntry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Init{
    public static int minActAddVersion = 28;
    public static int loadedActAddVersion = 0;
    public static String[] actAddLaserRelayEnergy = {"actuallyadditions:blocklaserrelay", "actuallyadditions:blocklaserrelayadvanced", "actuallyadditions:blocklaserrelayextreme"};
    public static String[] actAddLaserRelayItem = {"actuallyadditions:blocklaserrelayitem", "actuallyadditions:blocklaserrelayitemwhitelist"};
    public static String[] actAddLaserRelayFluid = {"actuallyadditions:blocklaserrelayfluids"};

    public static void preInitIntegration(FMLPreInitializationEvent event){

    }

    public static void initIntegration(FMLInitializationEvent event){

    }

    public static void postInitIntegration(FMLPostInitializationEvent event){
        if(Init.loadedActAddVersion >= Init.minActAddVersion)
            BookletEntry.postInit();
    }
}
