package at.chaosfield.openradio.init;

import at.chaosfield.openradio.OpenRadio;
//import at.chaosfield.openradio.block.AEEncoderBlock;
import at.chaosfield.openradio.block.LaserBlock;
//import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Blocks {
    public static LaserBlock laserBlock = null;
    //public static AEEncoderBlock aeencoderBlock = null;

    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        laserBlock = new LaserBlock();
        GameRegistry.registerBlock(laserBlock, "laser");
        /*if(Loader.isModLoaded("appliedenergistics2")) {
            aeencoderBlock = new AEEncoderBlock();
            GameRegistry.registerBlock(aeencoderBlock, "aeencoder");
        }*/
    }
}
