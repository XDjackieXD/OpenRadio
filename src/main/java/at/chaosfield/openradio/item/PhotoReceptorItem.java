package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.item.Item;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class PhotoReceptorItem extends Item{
    public PhotoReceptorItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ".photoreceptor");
        setRegistryName("photoreceptor");
        setCreativeTab(CreativeTab.instance);
    }
}
