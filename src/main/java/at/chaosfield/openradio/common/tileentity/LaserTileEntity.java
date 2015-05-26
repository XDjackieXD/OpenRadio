package at.chaosfield.openradio.common.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.entity.LaserEntity;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment implements IInventory{
    private boolean powered;
    private int laserPower = 10;
    private double latency = 0;
    private boolean connected = false;
    private ItemStack[] inv;

    public LaserTileEntity(){
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create();
        inv = new ItemStack[5];
    }

    public String getName(){
        return OpenRadio.MODID + ".laser";
    }


    //TODO instead of searching through all TEs in a world just shoot an entity containing the own position which gives this position to a hit laser
    public Object[] searchOpponent(){
        double posX, posY, posZ, accX, accY, accZ;
        switch(this.getBlockMetadata()){
            case 0:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5 -1;
                posZ = this.zCoord+0.5;
                accX = 0;
                accY = -0.1;
                accZ = 0;
                break;
            case 1:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5 +1;
                posZ = this.zCoord+0.5;
                accX = 0;
                accY = +0.1;
                accZ = 0;
                break;
            case 2:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5 -1;
                accX = 0;
                accY = 0;
                accZ = -0.1;
                break;
            case 3:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5 +1;
                accX = 0;
                accY = 0;
                accZ = 0.1;
                break;
            case 4:
                posX = this.xCoord+0.5 -1;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5;
                accX = -0.1;
                accY = 0;
                accZ = 0;
                break;
            case 5:
                posX = this.xCoord+0.5 +1;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5;
                accX = 0.1;
                accY = 0;
                accZ = 0;
                break;
            default:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5 +1;
                posZ = this.zCoord+0.5;
                accX = 0;
                accY = 0;
                accZ = 0;
        }

        OpenRadio.logger.info("Fired!"); //debugging!
        this.getWorldObj().spawnEntityInWorld(new LaserEntity(this.getWorldObj(), posX, posY, posZ, accX, accY, accZ));
        return new Object[]{false, 0, 0, 0, 0};
    }

    //------------------------------------------------------------------------------------------------------------------
    //Open Computers Integration
    public String getComponentName(){
        return "Laser";
    }

    @Callback(direct = true, doc = "function():double -- Get the current latency")
    public Object[] getLatency(Context context, Arguments args){
        return new Object[]{latency};
    }

    @Callback(direct = true, doc = "function():{successful, World Name, x, y, z} -- Get the current latency")
    public Object[] connect(Context context, Arguments args){
        return searchOpponent();
    }
    //------------------------------------------------------------------------------------------------------------------

    //check, if the energy buffer isn't empty
    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote){
            if(connected){
                powered = (node() != null) && ((Connector) node()).tryChangeBuffer(laserPower / 10f * OpenRadio.energyMultiplier);
            }
        }
    }

    public boolean isPowered(){
        return powered;
    }

    @Override
    public int getSizeInventory(){
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        inv[slot] = stack;
        if(stack != null && stack.stackSize > getInventoryStackLimit()){
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName(){
        return OpenRadio.MODID + ".laser";
    }

    @Override
    public boolean hasCustomInventoryName(){
        return false;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt){
        ItemStack stack = getStackInSlot(slot);
        if(stack != null){
            if(stack.stackSize <= amt){
                setInventorySlotContents(slot, null);
            }else{
                stack = stack.splitStack(amt);
                if(stack.stackSize == 0){
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot){
        ItemStack stack = getStackInSlot(slot);
        if(stack != null){
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit(){
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory(){

    }

    @Override
    public void closeInventory(){

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_){
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Inventory", tagCompound.getId());
        for(int i = 0; i < tagList.tagCount(); i++){
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if(slot >= 0 && slot < inv.length){
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        super.writeToNBT(tagCompound);

        NBTTagList itemList = new NBTTagList();
        for(int i = 0; i < inv.length; i++){
            ItemStack stack = inv[i];
            if(stack != null){
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        readFromNBT(pkt.func_148857_g());
    }
}
