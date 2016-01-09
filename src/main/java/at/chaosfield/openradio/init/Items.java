package at.chaosfield.openradio.init;

import at.chaosfield.openradio.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Items{
    public static LensItem lensItem;
    public static LaserSocketItem laserSocketItem;
    public static DSPItem dspItem;
    public static ADCItem adcItem;
    public static PhotoReceptorItem photoReceptorItem;
    public static MirrorItem mirrorItem;
    public static LaserItem laserItem;

    //Register all items (Has to be called during FML Init)
    public static void init(){
        GameRegistry.registerItem(lensItem = new LensItem(), "lens");
        GameRegistry.registerItem(laserSocketItem = new LaserSocketItem(), "lasersocket");
        GameRegistry.registerItem(dspItem = new DSPItem(), "dsp");
        GameRegistry.registerItem(photoReceptorItem = new PhotoReceptorItem(), "photoreceptor");
        GameRegistry.registerItem(adcItem = new ADCItem(), "adc");
        GameRegistry.registerItem(mirrorItem = new MirrorItem(), "mirror");
        GameRegistry.registerItem(laserItem = new LaserItem(), "laserItem");
    }
}