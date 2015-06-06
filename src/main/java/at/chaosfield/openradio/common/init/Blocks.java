package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.block.LaserBlock;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Blocks {
    public static LaserBlock laserBlock;

    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        laserBlock = new LaserBlock();
        GameRegistry.registerBlock(laserBlock, OpenRadio.MODID + ".laser");
    }
}
