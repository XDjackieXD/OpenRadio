package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.item.LaserSocketItem;
import at.chaosfield.openradio.common.item.LensItem;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Items{
    public static LensItem lensItem;
    public static LaserSocketItem laserSocketItem;

    //Register all items (Has to be called during FML Init)
    public static void init(){
        GameRegistry.registerItem(lensItem = new LensItem(), OpenRadio.MODID + ".lens");
        GameRegistry.registerItem(laserSocketItem = new LaserSocketItem(), OpenRadio.MODID + ".lasersocket");
    }
}