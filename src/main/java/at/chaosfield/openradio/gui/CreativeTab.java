package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.init.Blocks;
import at.chaosfield.openradio.init.Items;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class CreativeTab extends CreativeTabs{
    public static CreativeTab instance = new CreativeTab();
    private List list;

    public CreativeTab(){
        super(OpenRadio.MODID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List list){
        this.list = list;

        this.addBlock(Blocks.laserBlock);

        if(Loader.isModLoaded("appliedenergistics2"))
            this.addBlock(Blocks.aeencoderBlock);

        this.addItem(Items.laserSocketItem);
        this.addItem(Items.lensItem);
        this.addItem(Items.dspItem);
        this.addItem(Items.adcItem);
        this.addItem(Items.laserItem);
        this.addItem(Items.photoReceptorItem);
        this.addItem(Items.mirrorItem);
    }

    @Override
    public Item getTabIconItem(){
        return Item.getItemFromBlock(Blocks.laserBlock);
    }

    @Override
    public ItemStack getIconItemStack(){
        return new ItemStack(this.getTabIconItem());
    }

    private void addItem(Item item){
        item.getSubItems(item, this, list);
    }

    private void addBlock(Block block){
        block.getSubBlocks(new ItemStack(block).getItem(), this, list);
    }
}
