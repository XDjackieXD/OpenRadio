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
