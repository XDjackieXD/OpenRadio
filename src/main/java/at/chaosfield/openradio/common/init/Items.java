package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.item.LensItem;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Items{
    public static LensItem lensItem;

    //Register all items (Has to be called during FML Init)
    public static void init(){
        lensItem = new LensItem();
        GameRegistry.registerItem(lensItem, OpenRadio.MODID + ".lens");
    }
}