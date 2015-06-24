package at.chaosfield.openradio.common.item;

import at.chaosfield.openradio.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.item.Item;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class PhotoReceptorItem extends Item{
    public PhotoReceptorItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ":photoreceptor");
        setTextureName(OpenRadio.MODID + ":photoreceptor");
        setCreativeTab(CreativeTab.instance);
    }
}
