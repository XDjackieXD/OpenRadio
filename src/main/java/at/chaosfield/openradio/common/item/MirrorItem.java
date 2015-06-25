package at.chaosfield.openradio.common.item;

import at.chaosfield.openradio.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.item.Item;

/**
 * Created by jakob on 25.06.15.
 */
public class MirrorItem extends Item{
    public MirrorItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ":mirror");
        setTextureName(OpenRadio.MODID + ":mirror");
        setCreativeTab(CreativeTab.instance);
    }
}
