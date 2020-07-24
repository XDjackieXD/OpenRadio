package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.init.Blocks;
import at.chaosfield.openradio.init.Items;
//import net.minecraftforge.fml.common.Loader;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class CreativeTab extends CreativeTabs{
    public static CreativeTab instance = new CreativeTab();
    private NonNullList list;

    public CreativeTab(){
        super(OpenRadio.MODID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> list){
        this.list = list;

        this.addBlock(Blocks.laserBlock);
        this.addBlock(Blocks.lensBlock1);
        this.addBlock(Blocks.lensBlock2);
        this.addBlock(Blocks.lensBlock3);
        this.addBlock(Blocks.mirrorBlock);

        //if(Loader.isModLoaded("appliedenergistics2"))
        //    this.addBlock(Blocks.aeencoderBlock);

        this.addItem(Items.laserSocketItem);
        this.addItem(Items.dspItem);
        this.addItem(Items.adcItem);
        this.addItem(Items.laserItem);
        this.addItem(Items.photoReceptorItem);
        this.addItem(Items.mirrorItem);
    }

    @Override
    public ItemStack getTabIconItem(){
        return new ItemStack(Item.getItemFromBlock(Blocks.laserBlock));
    }

    @Override
    public ItemStack getIconItemStack(){
        return this.getTabIconItem();
    }

    private void addItem(Item item){
        item.getSubItems(item, this, list);
    }

    private void addBlock(Block block){
        block.getSubBlocks(new ItemStack(block).getItem(), this, list);
    }
}
