package at.chaosfield.openradio.item;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ADCItem extends Item{
    public ADCItem(){
        maxStackSize = 64;
        setUnlocalizedName(OpenRadio.MODID + ":adc");
        setHasSubtypes(true);
        setCreativeTab(CreativeTab.instance);
    }

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        icons = new IIcon[3];
        icons[0] = par1IconRegister.registerIcon(OpenRadio.MODID + ":adct1");
        icons[1] = par1IconRegister.registerIcon(OpenRadio.MODID + ":adct2");
        icons[2] = par1IconRegister.registerIcon(OpenRadio.MODID + ":adct3");
    }

    public static final String[] names = new String[] { "t1", "t2", "t3" };

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 2);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return icons[par1];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {

        for (int i = 0; i < 3; ++i) {
            itemList.add(new ItemStack(item, 1, i));
        }
    }
}
