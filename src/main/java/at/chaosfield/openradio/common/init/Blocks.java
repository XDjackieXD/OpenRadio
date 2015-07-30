package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.block.AEEncoderBlock;
import at.chaosfield.openradio.common.block.LaserBlock;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Blocks {
    public static LaserBlock laserBlock = null;
    public static AEEncoderBlock aeencoderBlock = null;

    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        laserBlock = new LaserBlock();
        GameRegistry.registerBlock(laserBlock, OpenRadio.MODID + ".laser");
        if(Loader.isModLoaded("appliedenergistics2")) {
            aeencoderBlock = new AEEncoderBlock();
            GameRegistry.registerBlock(aeencoderBlock, OpenRadio.MODID + ".aeencoder");
        }
    }
}
