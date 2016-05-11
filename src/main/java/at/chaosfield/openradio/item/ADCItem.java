package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ADCItem extends Item{
    public ADCItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ".adc");
        setHasSubtypes(true);
        setCreativeTab(CreativeTab.instance);
    }

    public static final String[] names = new String[] { "t1", "t2", "t3" };

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 2);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {

        for (int i = 0; i < 3; ++i) {
            itemList.add(new ItemStack(item, 1, i));
        }
    }
}
