package at.chaosfield.openradio.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by XDjackieXD
 */
public class BaseItemBlock extends ItemBlock{
    public BaseItemBlock(Block block){
        super(block);
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return this.getUnlocalizedName();
    }

    @Override
    public int getMetadata(int meta){
        return meta;
    }
}
