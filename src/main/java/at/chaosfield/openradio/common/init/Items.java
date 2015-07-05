package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.item.*;
import cpw.mods.fml.common.registry.GameRegistry;

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
        GameRegistry.registerItem(lensItem = new LensItem(), OpenRadio.MODID + ".lens");
        GameRegistry.registerItem(laserSocketItem = new LaserSocketItem(), OpenRadio.MODID + ".lasersocket");
        GameRegistry.registerItem(dspItem = new DSPItem(), OpenRadio.MODID + ".dsp");
        GameRegistry.registerItem(photoReceptorItem = new PhotoReceptorItem(), OpenRadio.MODID + ".photoreceptor");
        GameRegistry.registerItem(adcItem = new ADCItem(), OpenRadio.MODID + ".adc");
        GameRegistry.registerItem(mirrorItem = new MirrorItem(), OpenRadio.MODID + ".mirror");
        GameRegistry.registerItem(laserItem = new LaserItem(), OpenRadio.MODID + ".laserItem");
    }
}