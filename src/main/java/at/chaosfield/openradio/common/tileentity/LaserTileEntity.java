package at.chaosfield.openradio.common.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.Settings;
import at.chaosfield.openradio.common.entity.LaserEntity;
import at.chaosfield.openradio.util.Location;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Packet;
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
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment implements IInventory{
    private boolean powered;
    private int laserPower = 10;
    private double latency = 0;
    private boolean connected = false;
    private ItemStack[] inv;

    private Location otherLaser;
    private LaserTileEntity otherLaserTe;

    public LaserTileEntity(){
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create();
        inv = new ItemStack[5];
    }

    public String getName(){
        return OpenRadio.MODID + ".laser";
    }


    public void pingRequest(String uid){
        double posX, posY, posZ, accX, accY, accZ;
        switch(this.getBlockMetadata()){
            case 0:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5 -1;
                posZ = this.zCoord+0.5;
                accX = 0;
                accY = -Settings.EntitySpeed;
                accZ = 0;
                break;
            case 1:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5 +1;
                posZ = this.zCoord+0.5;
                accX = 0;
                accY = +Settings.EntitySpeed;
                accZ = 0;
                break;
            case 2:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5 -1;
                accX = 0;
                accY = 0;
                accZ = -Settings.EntitySpeed;
                break;
            case 3:
                posX = this.xCoord+0.5;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5 +1;
                accX = 0;
                accY = 0;
                accZ = Settings.EntitySpeed;
                break;
            case 4:
                posX = this.xCoord+0.5 -1;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5;
                accX = -Settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            case 5:
                posX = this.xCoord+0.5 +1;
                posY = this.yCoord+0.5;
                posZ = this.zCoord+0.5;
                accX = Settings.EntitySpeed;
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

        this.getWorldObj().spawnEntityInWorld(new LaserEntity(this.worldObj, posX, posY, posZ, accX, accY, accZ, uid, this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord));
    }

    public void hitByLaserEntity(String uid, int dim, int x, int y, int z){
        OpenRadio.logger.info("Im a Laser at X=" + this.xCoord + ", Y=" + this.yCoord + ", Z=" + this.zCoord + ". my uid is: " + node.address() + "the data is: X=" + x + ", Y=" + y + ", Z=" + z + ", UID: " + uid); //debugging!
        if(uid.equals(node.address()) && (x != this.xCoord || y != this.yCoord || z != this.zCoord)){
            //This is the response of the other laser
            tryConnect(dim, x, y, z);
        }else if(!uid.equals(node.address())){
            //this is the request of another laser
            pingRequest(uid);
            tryConnect(dim, x, y, z);
        }
    }

    private void tryConnect(int dimId, int x, int y, int z){
        if(!worldObj.isRemote) {
            TileEntity te = DimensionManager.getWorld(dimId).getTileEntity(x, y, z);
            if (te instanceof LaserTileEntity) {
                otherLaserTe = (LaserTileEntity) te;
                this.connected = true;
                this.otherLaser = new Location(dimId, x, y, z);
            }else{
                this.connected = false;
                otherLaserTe = null;
                otherLaser = null;
            }
        }

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

    @Callback(direct = true, doc = "function():{true} -- try to connect to other laser")
    public Object[] connect(Context context, Arguments args){
        pingRequest(node.address());
        return new Object[]{true};
    }

    @Callback(direct = true, doc = "function():{connected, dimId, x, y, z} -- Get the other Laser")
    public Object[] connected(Context context, Arguments args){
        if(otherLaser != null)
            return new Object[]{true, otherLaser.getDim(), otherLaser.getX(), otherLaser.getY(), otherLaser.getZ()};
        return new Object[]{false, 0, 0, 0, 0};
    }

    @Override
    public void onMessage(Message message){
        super.onMessage(message);
        if(message.name().equals("network.message")){
            if(otherLaserTe == null && otherLaser != null){
                tryConnect(otherLaser.getDim(), otherLaser.getX(), otherLaser.getY(), otherLaser.getZ());
            }
            if(otherLaserTe != null && connected){
                otherLaserTe.node.sendToReachable("network.message", message.data());
            }
        }
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

        if(tagCompound.getBoolean("otherLaserConnected")){
            otherLaser = new Location(tagCompound.getInteger("otherLaserDimId"), tagCompound.getInteger("otherLaserX"), tagCompound.getInteger("otherLaserY"), tagCompound.getInteger("otherLaserZ"));
        }else{
            otherLaser = null;
            this.connected = false;
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

        if(connected) {
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
}
