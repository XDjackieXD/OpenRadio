package at.chaosfield.openradio.init;

import at.chaosfield.openradio.OpenRadio;
//import at.chaosfield.openradio.block.AEEncoderBlock;
import at.chaosfield.openradio.block.LaserBlock;
//import net.minecraftforge.fml.common.Loader;
import at.chaosfield.openradio.block.LensBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Blocks {
    public static LaserBlock laserBlock = null;
    public static LensBlock lensBlock1 = null;
    public static LensBlock lensBlock2 = null;
    public static LensBlock lensBlock3 = null;
    //public static AEEncoderBlock aeencoderBlock = null;

    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        laserBlock = new LaserBlock();
        lensBlock1 = new LensBlock(1);
        lensBlock2 = new LensBlock(2);
        lensBlock3 = new LensBlock(3);
        GameRegistry.registerBlock(laserBlock, "laser");
        GameRegistry.registerBlock(lensBlock1, "lenst1");
        GameRegistry.registerBlock(lensBlock2, "lenst2");
        GameRegistry.registerBlock(lensBlock3, "lenst3");
        /*if(Loader.isModLoaded("appliedenergistics2")) {
            aeencoderBlock = new AEEncoderBlock();
            GameRegistry.registerBlock(aeencoderBlock, "aeencoder");
        }*/
    }
}
