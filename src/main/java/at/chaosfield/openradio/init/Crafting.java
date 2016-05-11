package at.chaosfield.openradio.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
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

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.laserSocketItem),        //Laser Socket
                "IDI", "I I", "IDI",
                'I', "ingotIron",
                'D', "gemDiamond"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 0),          //ADC Tier 1
                "III", "IMI", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.COMPARATOR),
                'M', li.cil.oc.api.Items.get("chip1").createItemStack(1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 1),          //ADC Tier 2
                "ICI", "IMI", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.COMPARATOR),
                'M', li.cil.oc.api.Items.get("chip2").createItemStack(2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.adcItem, 1, 2),          //ADC Tier 3
                "ICI", "CMC", "ICI",
                'I', "ingotIron",
                'C', new ItemStack(net.minecraft.init.Items.COMPARATOR),
                'M', li.cil.oc.api.Items.get("chip3").createItemStack(1)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 0),          //DSP Tier 1
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip1").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 1),          //DSP Tier 2
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip2").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dspItem, 1, 2),          //DSP Tier 3
                "IAI", "MCM", "IAI",
                'I', "ingotIron",
                'M', li.cil.oc.api.Items.get("chip3").createItemStack(1),
                'C', li.cil.oc.api.Items.get("cu").createItemStack(1),
                'A', new ItemStack(Items.adcItem, 1, 2)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.photoReceptorItem),      //Photo Receptor
                "IDI", "TGT", "IDI",
                'I', "ingotIron",
                'D', "gemDiamond",
                'G', "blockGlass",
                'T', li.cil.oc.api.Items.get("transistor").createItemStack(1)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.mirrorItem),             //Semi Reflective Mirror
                " IG", "IGD", "GD ",
                'I', "ingotIron",
                'D', "gemDiamond",
                'G', "blockGlass"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.laserItem, 1, 0),        //Laser tier 1
                "IGI", "INI", "ITI",
                'I', "ingotIron",
                'G', "blockGlass",
                'N', "nuggetGold",
                'T', li.cil.oc.api.Items.get("transistor").createItemStack(1)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.laserItem, 1, 1),        //Laser tier 2
                "GDG", "N N", "GDG",
                'D', "gemDiamond",
                'G', "blockGlass",
                'N', "ingotGold"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.laserItem, 1, 2),        //Laser tier 3
                "IGI", "IEI", "INI",
                'I', "ingotIron",
                'G', "blockGlass",
                'N', "nuggetGold",
                'E', "gemEmerald"));

        //**************************************************************************************************************
        //Register Blocks

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.laserBlock),            //Laser Block
                "III", "NSL", "III",
                'I', "ingotIron",
                'N', li.cil.oc.api.Items.get("cable").createItemStack(1),
                'S', li.cil.oc.api.Items.get("relay").createItemStack(1),
                'L', new ItemStack(Items.laserSocketItem)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.lensBlock1),         //Glass Lens
                "IGI", "GGG", "IGI",
                'I', "ingotIron",
                'G', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.lensBlock2),         //Quartz Infused Lens
                " Q ", "QLQ", " Q ",
                'L', new ItemStack(Blocks.lensBlock1),
                'Q', "gemQuartz"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.lensBlock3),         //Shaped Diamond Lens
                "GDG", "DLD", "GDG",
                'L', new ItemStack(Blocks.lensBlock2),
                'G', "ingotGold",
                'D', "gemDiamond"));

        /*if(Loader.isModLoaded("appliedenergistics2")){
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.aeencoderBlock),
                    "INI", "CMC", "INI",
                    'I', "ingotIron",
                    'N', li.cil.oc.api.Items.get("cable").createItemStack(1),
                    'M', Item.itemRegistry.getObject("appliedenergistics2:tile.BlockController"),
                    'C', appeng.api.AEApi.instance().definitions().parts().cableSmart().stack(appeng.api.util.AEColor.Transparent, 1)));
        }*/
    }
}
