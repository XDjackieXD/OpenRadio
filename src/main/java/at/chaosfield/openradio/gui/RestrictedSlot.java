package at.chaosfield.openradio.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Jakob Riepler (XDjackieXD)
 * A slot which can be restricted to only allow a special Item
 */
public class RestrictedSlot extends Slot{

    private Item restriction;
    private int limit;

    public RestrictedSlot(Item restriction, int limit, IInventory inventory, int par2, int par3, int par4){
        super(inventory, par2, par3, par4);
        this.restriction = restriction;
        this.limit = limit;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack){
        return itemStack.getItem().getUnlocalizedName().equals(restriction.getUnlocalizedName());
    }

    @Override
    public int getSlotStackLimit(){
        return limit;
    }

}
