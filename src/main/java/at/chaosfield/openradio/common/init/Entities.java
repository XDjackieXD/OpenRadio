package at.chaosfield.openradio.common.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.entity.LaserEntity;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Entities{
    public static void init(){
        EntityRegistry.registerModEntity(LaserEntity.class, OpenRadio.MODID + ".laserentity", 1, OpenRadio.instance, 80, 3, true);
    }
}