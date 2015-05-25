package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.block.LaserBlock;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import scala.tools.cmd.gen.AnyValReps;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Blocks {
    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        GameRegistry.registerBlock(new LaserBlock(), OpenRadio.MODID + ".laser");
    }
}
