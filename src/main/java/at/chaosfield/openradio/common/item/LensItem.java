package at.chaosfield.openradio.common.item;

import at.chaosfield.openradio.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LensItem extends Item{


    public LensItem(){
        maxStackSize = 1;
        setUnlocalizedName(OpenRadio.MODID + ":lens");
        setCreativeTab(CreativeTab.instance);
    }
}
