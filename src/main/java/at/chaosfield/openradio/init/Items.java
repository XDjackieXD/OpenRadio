package at.chaosfield.openradio.init;

import at.chaosfield.openradio.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Items{
    public static LaserSocketItem laserSocketItem;
    public static DSPItem dspItem;
    public static ADCItem adcItem;
    public static PhotoReceptorItem photoReceptorItem;
    public static MirrorItem mirrorItem;
    public static LaserItem laserItem;

    //Register all items (Has to be called during FML Init)
    public static void init(){
        GameRegistry.register(laserSocketItem = new LaserSocketItem());
        GameRegistry.register(dspItem = new DSPItem());
        GameRegistry.register(photoReceptorItem = new PhotoReceptorItem());
        GameRegistry.register(adcItem = new ADCItem());
        GameRegistry.register(mirrorItem = new MirrorItem());
        GameRegistry.register(laserItem = new LaserItem());
    }
}