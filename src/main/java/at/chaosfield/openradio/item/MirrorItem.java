package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.item.Item;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class MirrorItem extends Item{
    public MirrorItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ".mirror");
        setRegistryName("mirror");
        setCreativeTab(CreativeTab.instance);
    }
}
