package at.chaosfield.openradio.common.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by Jakob Riepler (XDjackieXD)
 * Register all crafting recipes
 */
public class Crafting{
    public static void init(){

        //**************************************************************************************************************
        //Register Items
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.lensItem, 1, 0),     //Glass Lens
                " G ", "GGG", " G ",
                'G', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.lensItem, 1, 1),     //Quartz Infused Lens
                " Q ", "QLQ", " Q ",
                'L', new ItemStack(Items.lensItem, 1, 0),
                'Q', "gemQuartz"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.lensItem, 1, 2),     //Shaped Diamond Lens
                "GDG", "DLD", "GDG",
                'L', new ItemStack(Items.lensItem, 1, 1),
                'G', "ingotGold",
                'D', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.laserSocketItem),    //Laser Socket
                "IDI", "I I", "IDI",
                'I', "ingotIron",
                'D', "gemDiamond"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 0),      //ADC Tier 1
                "III", "IMI", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.comparator),
                'M', li.cil.oc.api.Items.get("chip1").createItemStack(1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 1),      //ADC Tier 2
                "ICI", "IMI", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.comparator),
                'M', li.cil.oc.api.Items.get("chip2").createItemStack(2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 2),      //ADC Tier 3
                "ICI", "CMC", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.comparator),
                'M', li.cil.oc.api.Items.get("chip3").createItemStack(1)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 0),      //DSP Tier 1
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip1").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 1),      //DSP Tier 2
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip2").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 2),      //DSP Tier 3
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip3").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 2)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.photoReceptorItem),  //Laser Socket
                "IDI", "TGT", "IDI",
                'I', "ingotIron",
                'D', "gemDiamond",
                'G', "blockGlass",
                'T', li.cil.oc.api.Items.get("transistor").createItemStack(1)));

        //**************************************************************************************************************
        //Register Blocks
        //TODO Use steel instead of iron if available
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.laserBlock),
                "III", "NSL", "III",
                'I', "ingotIron",
                'N', li.cil.oc.api.Items.get("cable").createItemStack(1),
                'S', li.cil.oc.api.Items.get("switch").createItemStack(1),
                'L', new ItemStack(Items.laserSocketItem)));
    }
}
