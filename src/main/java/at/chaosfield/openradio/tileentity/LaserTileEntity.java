package at.chaosfield.openradio.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.init.Items;
import at.chaosfield.openradio.interfaces.ILaserAddon;
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
import net.minecraftforge.common.util.ForgeDirection;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment implements IInventory{

    public static final int SLOT_DSP = 0;
    public static final int SLOT_PHOTO_RECEPTOR = 1;
    public static final int SLOT_MIRROR = 2;
    public static final int SLOT_LENS = 3;
    public static final int SLOT_LASER = 4;

    private boolean powered;
    private double distance;
    private Location otherLaser;
    private int energyMultiplier = 1;

    private ILaserAddon connectedAddons[] = {null, null, null, null, null, null};
    private String connectedAddonsType[] = {null, null, null, null, null, null};

    private ItemStack[] inv;

    private int counter = 0;

    public LaserTileEntity(){
        super();
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.instance.settings.EnergyBuffer).create();
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
                accY = -OpenRadio.instance.settings.EntitySpeed;
                accZ = 0;
                break;
            case 1:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5 + 1;
                posZ = this.zCoord + 0.5;
                accX = 0;
                accY = OpenRadio.instance.settings.EntitySpeed;
                accZ = 0;
                break;
            case 2:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5 - 1;
                accX = 0;
                accY = 0;
                accZ = -OpenRadio.instance.settings.EntitySpeed;
                break;
            case 3:
                posX = this.xCoord + 0.5;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5 + 1;
                accX = 0;
                accY = 0;
                accZ = OpenRadio.instance.settings.EntitySpeed;
                break;
            case 4:
                posX = this.xCoord + 0.5 - 1;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5;
                accX = -OpenRadio.instance.settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            case 5:
                posX = this.xCoord + 0.5 + 1;
                posY = this.yCoord + 0.5;
                posZ = this.zCoord + 0.5;
                accX = OpenRadio.instance.settings.EntitySpeed;
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

        this.getWorldObj().spawnEntityInWorld(new LaserEntity(this.worldObj, posX, posY, posZ, accX, accY, accZ, this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, getMaxDistance()));
    }

    public void setDestination(int dim, int x, int y, int z, double distance){
        if(!this.getWorldObj().isRemote){
            this.otherLaser = new Location(dim, x, y, z);
            this.distance = distance;
            this.markDirty();
        }
    }

    public void disconnect(){
        this.otherLaser = null;
    }

    public Location getOtherLaser(){
        return this.otherLaser;
    }

    public boolean isConnected(){
        return (otherLaser != null) && isPowered() && hasNeededComponents();
    }

    public boolean hasNeededComponents(){
        return inv[SLOT_DSP] != null &&
                inv[SLOT_PHOTO_RECEPTOR] != null &&
                inv[SLOT_MIRROR] != null &&
                inv[SLOT_LENS] != null &&
                inv[SLOT_LASER] != null &&
                inv[SLOT_DSP].getItem() == Items.dspItem &&
                inv[SLOT_PHOTO_RECEPTOR].getItem() == Items.photoReceptorItem &&
                inv[SLOT_MIRROR].getItem() == Items.mirrorItem &&
                inv[SLOT_LENS].getItem() == Items.lensItem &&
                inv[SLOT_LASER].getItem() == Items.laserItem;
    }

    public void connectAddon(ILaserAddon addon, int side){
        if(this.connectedAddons[side] != addon){
            this.connectedAddons[side] = addon;
            this.connectedAddonsType[side] = addon.getAddonName();
            addon.connectToLaser(this);
            if(addon.getAddonName().equals("aeencoder"))
                energyMultiplier *= OpenRadio.instance.settings.AEEnergyMultiplier;
        }
    }

    public void disconnectAddon(int side){
        if(this.connectedAddons[side] != null){
            this.connectedAddons[side] = null;
            if(connectedAddonsType[side] != null)
                if(connectedAddonsType[side].equals("aeencoder"))
                    energyMultiplier /= OpenRadio.instance.settings.AEEnergyMultiplier;
            this.connectedAddonsType[side] = null;
        }
    }

    public ILaserAddon[] getAddons(){
        return connectedAddons;
    }

    public int getItemTier(int slot, Object item){
        if(inv[slot] != null)
            if(inv[slot].getItem() == item)
                if(inv[slot].getItemDamage() <= 2 && inv[slot].getItemDamage() >= 0)
                    return inv[slot].getItemDamage() + 1;
        return 0;
    }

    public double getMaxDistance(){
        if(hasNeededComponents()){
            return OpenRadio.instance.settings.LaserMaxDistanceTier[getItemTier(SLOT_LASER, Items.laserItem)-1] * OpenRadio.instance.settings.LensMultiplierTier[getItemTier(SLOT_LENS, Items.lensItem)-1];
        }else{
            return 0;
        }
    }

    public boolean isPowered(){
        return this.powered;
    }

    public void tryUsePower(int energy){
        this.powered = (node() != null) && ((Connector) node()).tryChangeBuffer(energy / -10f);
    }

    public double calculateBasicEnergyUsage(){
        int usage = 0;
        if(hasNeededComponents()){
            usage += OpenRadio.instance.settings.EnergyUseLaserTier[getItemTier(SLOT_LASER, Items.laserItem)-1];
        }
        return usage;
    }

    //------------------------------------------------------------------------------------------------------------------
    //Open Computers Integration
    public String getComponentName(){
        return "laser";
    }

    @Callback(direct = true, doc = "function():double -- Get the current latency")
    public Object[] getLatency(Context context, Arguments args){
        return new Object[]{distance};
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

            if(hasNeededComponents()){
                tryUsePower((int) (calculateBasicEnergyUsage() * energyMultiplier));
            }

            if(hasNeededComponents() && isPowered()){
                counter++;
                if(counter >= 20){
                    counter = 0;
                    sendEntity();
                }
            }else{
                disconnect();
            }

            if(this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord) instanceof ILaserAddon){ //east
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord), ForgeDirection.EAST.ordinal());
            }else{
                disconnectAddon(ForgeDirection.EAST.ordinal());
            }
            if(this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord) instanceof ILaserAddon){ //west
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord), ForgeDirection.WEST.ordinal());
            }else{
                disconnectAddon(ForgeDirection.WEST.ordinal());
            }
            if(this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) instanceof ILaserAddon){ //top
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord), ForgeDirection.UP.ordinal());
            }else{
                disconnectAddon(ForgeDirection.UP.ordinal());
            }
            if(this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord) instanceof ILaserAddon){ //bottom
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord), ForgeDirection.DOWN.ordinal());
            }else{
                disconnectAddon(ForgeDirection.DOWN.ordinal());
            }
            if(this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1) instanceof ILaserAddon){ //south
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1), ForgeDirection.SOUTH.ordinal());
            }else{
                disconnectAddon(ForgeDirection.SOUTH.ordinal());
            }
            if(this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1) instanceof ILaserAddon){ //north
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1), ForgeDirection.NORTH.ordinal());
            }else{
                disconnectAddon(ForgeDirection.NORTH.ordinal());
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
        this.markDirty();
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
            this.markDirty();
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
}
