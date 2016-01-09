package at.chaosfield.openradio.container;

import at.chaosfield.openradio.init.Items;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import at.chaosfield.openradio.gui.RestrictedSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserContainer extends Container{
    protected LaserTileEntity tileEntity;

    public LaserContainer(IInventory inventoryPlayer, LaserTileEntity te){
        tileEntity = te;

        addSlotToContainer(new RestrictedSlot(Items.dspItem, 1, tileEntity, LaserTileEntity.SLOT_DSP, 33, 27));     //DSP
        addSlotToContainer(new RestrictedSlot(Items.photoReceptorItem, 1, tileEntity, LaserTileEntity.SLOT_PHOTO_RECEPTOR, 56, 27));    //PhotoReceptor
        addSlotToContainer(new RestrictedSlot(Items.mirrorItem, 1, tileEntity, LaserTileEntity.SLOT_MIRROR, 89, 27));    //SemiReflectiveMirror
        addSlotToContainer(new RestrictedSlot(Items.lensItem, 1, tileEntity, LaserTileEntity.SLOT_LENS, 127, 27));   //Lens
        addSlotToContainer(new RestrictedSlot(Items.laserItem, 1, tileEntity, LaserTileEntity.SLOT_LASER, 89, 60));    //Laser

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }


    protected void bindPlayerInventory(IInventory inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 101 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 159));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack currentStack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            currentStack = stackInSlot.copy();

            //Item is in Container. Transfer to Player inventory
            if (slot < tileEntity.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36+tileEntity.getSizeInventory(), true)) {
                    return null;
                }
            //Item is in Player inventory. Transfer into container
            }else if(slot >= tileEntity.getSizeInventory()){
                if(stackInSlot.getItem() == Items.dspItem){
                    if(!this.mergeItemStack(stackInSlot, LaserTileEntity.SLOT_DSP, LaserTileEntity.SLOT_DSP+1, false)){
                        return null;
                    }
                }else if(stackInSlot.getItem() == Items.photoReceptorItem){
                    if(!this.mergeItemStack(stackInSlot, LaserTileEntity.SLOT_PHOTO_RECEPTOR, LaserTileEntity.SLOT_PHOTO_RECEPTOR+1, false)){
                        return null;
                    }
                }else if(stackInSlot.getItem() == Items.mirrorItem){
                    if(!this.mergeItemStack(stackInSlot, LaserTileEntity.SLOT_MIRROR, LaserTileEntity.SLOT_MIRROR+1, false)){
                        return null;
                    }
                }else if(stackInSlot.getItem() == Items.lensItem){
                    if(!this.mergeItemStack(stackInSlot, LaserTileEntity.SLOT_LENS, LaserTileEntity.SLOT_LENS+1, false)){
                        return null;
                    }
                }else if(stackInSlot.getItem() == Items.laserItem){
                    if(!this.mergeItemStack(stackInSlot, LaserTileEntity.SLOT_LASER, LaserTileEntity.SLOT_LASER+1, false)){
                        return null;
                    }
                }
            }


            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == currentStack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return currentStack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
        boolean success = false;
        int index = startIndex;

        if (useEndIndex)
            index = endIndex - 1;

        Slot slot;
        ItemStack stackinslot;

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex)) {
                slot = (Slot) this.inventorySlots.get(index);
                stackinslot = slot.getStack();

                if (stackinslot != null && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == stackinslot.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
                    int l = stackinslot.stackSize + stack.stackSize;
                    int maxsize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());

                    if (l <= maxsize) {
                        stack.stackSize = 0;
                        stackinslot.stackSize = l;
                        slot.onSlotChanged();
                        success = true;
                    } else if (stackinslot.stackSize < maxsize) {
                        stack.stackSize -= stack.getMaxStackSize() - stackinslot.stackSize;
                        stackinslot.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        success = true;
                    }
                }

                if (useEndIndex) {
                    --index;
                } else {
                    ++index;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (useEndIndex) {
                index = endIndex - 1;
            } else {
                index = startIndex;
            }

            while (!useEndIndex && index < endIndex || useEndIndex && index >= startIndex && stack.stackSize > 0) {
                slot = (Slot) this.inventorySlots.get(index);
                stackinslot = slot.getStack();

                // Forge: Make sure to respect isItemValid in the slot.
                if (stackinslot == null && slot.isItemValid(stack)) {
                    if (stack.stackSize < slot.getSlotStackLimit()) {
                        slot.putStack(stack.copy());
                        stack.stackSize = 0;
                        success = true;
                        break;
                    } else {
                        ItemStack newstack = stack.copy();
                        newstack.stackSize = slot.getSlotStackLimit();
                        slot.putStack(newstack);
                        stack.stackSize -= slot.getSlotStackLimit();
                        success = true;
                    }
                }

                if (useEndIndex) {
                    --index;
                } else {
                    ++index;
                }
            }
        }

        return success;
    }
}
