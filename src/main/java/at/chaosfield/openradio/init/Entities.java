package at.chaosfield.openradio.init;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class Entities{
    public static void init(){
        EntityRegistry.registerModEntity(new ResourceLocation(OpenRadio.MODID, "laserentity"), LaserEntity.class, OpenRadio.MODID + ".laserentity", 1, OpenRadio.instance, 80, 3, true);
    }
}