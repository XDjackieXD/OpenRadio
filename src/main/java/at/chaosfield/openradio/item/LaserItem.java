package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class LaserItem extends Item{
    public LaserItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ".laser");
        setRegistryName("laserItem");
        setHasSubtypes(true);
        setCreativeTab(CreativeTab.instance);
    }

    public static final String[] names = new String[] { "t1", "t2", "t3" };

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp(par1ItemStack.getItemDamage(), 0, 2);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {

        for (int i = 0; i < 3; ++i) {
            itemList.add(new ItemStack(item, 1, i));
        }
    }
}
