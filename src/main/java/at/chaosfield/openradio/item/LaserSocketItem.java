package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.item.Item;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class LaserSocketItem extends Item{
    public LaserSocketItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ".lasersocket");
        setRegistryName("lasersocket");
        setCreativeTab(CreativeTab.instance);
    }
}
