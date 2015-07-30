package at.chaosfield.openradio.common.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.Settings;
import at.chaosfield.openradio.common.entity.LaserEntity;
import at.chaosfield.openradio.common.init.Items;
import at.chaosfield.openradio.util.Location;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment implements IInventory{
    private boolean powered;
    //private boolean turnedOn;
    private int laserPower = 10;
    private double distance;
    private Location otherLaser;

    private ItemStack[] inv;

    private int counter = 0;

    public LaserTileEntity(){
        super();
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create();
        inv = new ItemStack[5];
        if(otherLaser != null && !worldObj.isRemote){
            TileEntity otherLaserTe = DimensionManager.getWorld(otherLaser.getDim()).getTileEntity(otherLaser.getX(), otherLaser.getY(), otherLaser.getZ());
            if(otherLaserTe instanceof LaserTileEntity){
                ((LaserTileEntity) otherLaserTe).setDestination(this.getWorldObj().provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, this.distance);
            }else{
                disconnect();
            }
        }
    }

    public String getName(){
        return OpenRadio.MODID + ".laser";
    }

    public void sendEntity(){
        double posX, posY, posZ, accX, accY, accZ;
        switch(this.getBlockMetadata()){
            case 0:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5 - 1;
                posZ = this.zCoord + 0.5;
                accX = 0;
                accY = -Settings.EntitySpeed;
                accZ = 0;
                break;
            case 1:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5 + 1;
                posZ = this.zCoord + 0.5;
                accX = 0;
                accY = +Settings.EntitySpeed;
                accZ = 0;
                break;
            case 2:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5 - 1;
                accX = 0;
                accY = 0;
                accZ = -Settings.EntitySpeed;
                break;
            case 3:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5 + 1;
                accX = 0;
                accY = 0;
                accZ = Settings.EntitySpeed;
                break;
            case 4:
                posX = this.xCoord + 0.5 - 1;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5;
                accX = -Settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            case 5:
                posX = this.xCoord + 0.5 + 1;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5;
                accX = Settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            default:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5 + 1;
                posZ = this.zCoord + 0.5;
                accX = 0;
                accY = 0;
                accZ = 0;
        }

        this.getWorldObj().spawnEntityInWorld(new LaserEntity(this.worldObj, posX, posY, posZ, accX, accY, accZ, this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord));
    }

    public void setDestination(int dim, int x, int y, int z, double distance){
        if(!this.getWorldObj().isRemote){
            this.otherLaser = new Location(dim, x, y, z);
            this.distance = distance;
        }
    }

    public void disconnect(){
        this.otherLaser = null;
    }

    public Location getOtherLaser(){
        return this.otherLaser;
    }

    public boolean isConnected(){
        return (otherLaser != null);
    }

    public boolean hasNeededComponents(){
        return inv[0] != null && inv[1] != null && inv[2] != null && inv[3] != null && inv[4] != null && (inv[0].getItem() == Items.dspItem) && (inv[1].getItem() == Items.photoReceptorItem) && (inv[2].getItem() == Items.mirrorItem) && (inv[3].getItem() == Items.lensItem) && (inv[4].getItem() == Items.laserItem);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Open Computers Integration
    public String getComponentName(){
        return "Laser";
    }

    @Callback(direct = true, doc = "function():double -- Get the current latency")
    public Object[] getLatency(Context context, Arguments args){
        return new Object[]{distance};
    }

    /*@Callback(direct = true, doc = "function():{true} -- try to connect to other laser")
    public Object[] connect(Context context, Arguments args){
        if(hasNeededComponents()){
            sendEntity();
            return new Object[]{true};
        }else{
            return new Object[]{false};
        }
    }*/

    @Callback(direct = true, doc = "function():{connected, dimId, x, y, z} -- Get the other Laser")
    public Object[] connected(Context context, Arguments args){
        if(otherLaser != null) OpenRadio.logger.info("other Laser:" + otherLaser.toString());
        if(isConnected())
            return new Object[]{true, otherLaser.getDim(), otherLaser.getX(), otherLaser.getY(), otherLaser.getZ()};
        else
            return new Object[]{false, 0, 0, 0, 0};
    }

    @Override
    public void onMessage(Message message){
        super.onMessage(message);
        if(message.name().equals("network.message")){
            if(isConnected()){
                TileEntity tileEntity = DimensionManager.getWorld(otherLaser.getDim()).getTileEntity(otherLaser.getX(), otherLaser.getY(), otherLaser.getZ());
                if(tileEntity instanceof LaserTileEntity){
                    ((LaserTileEntity) tileEntity).node.sendToReachable("network.message", message.data());
                }else{
                    disconnect();
                }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote){
            if(isConnected()){
                powered = (node() != null) && ((Connector) node()).tryChangeBuffer(laserPower / 10f * OpenRadio.energyMultiplier);
                //if(!isPowered()) disconnect();

            }

            if(hasNeededComponents()){
                counter++;
                if(counter >= 20){
                    counter = 0;
                    sendEntity();
                }
            }else{
                disconnect();
            }
        }
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory(){

    }

    @Override
    public void closeInventory(){

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        switch(slot){
            case 3:
                return itemStack.getItem().getUnlocalizedName().equals(OpenRadio.MODID + ":lens");
            default:
                return false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);

        if(tagCompound.getBoolean("otherLaserConnected")){
            this.distance = tagCompound.getDouble("distance");
            otherLaser = new Location(tagCompound.getInteger("otherLaserDimId"), tagCompound.getInteger("otherLaserX"), tagCompound.getInteger("otherLaserY"), tagCompound.getInteger("otherLaserZ"));
        }else{
            disconnect();
        }

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

        if(isConnected()){
            tagCompound.setDouble("distance", this.distance);
            tagCompound.setBoolean("otherLaserConnected", true);
            tagCompound.setInteger("otherLaserDimId", otherLaser.getDim());
            tagCompound.setInteger("otherLaserX", otherLaser.getX());
            tagCompound.setInteger("otherLaserY", otherLaser.getY());
            tagCompound.setInteger("otherLaserZ", otherLaser.getZ());
        }else{
            tagCompound.setBoolean("otherLaserConnected", false);
        }

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
    public net.minecraft.network.Packet getDescriptionPacket(){
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        readFromNBT(pkt.func_148857_g());
    }

    public boolean isPowered(){
        return powered;
    }
}
