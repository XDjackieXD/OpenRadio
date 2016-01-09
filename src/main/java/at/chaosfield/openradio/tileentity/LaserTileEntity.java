package at.chaosfield.openradio.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.block.LaserBlock;
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;


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
            TileEntity otherLaserTe = DimensionManager.getWorld(otherLaser.getDim()).getTileEntity(otherLaser.getPos());
            if(otherLaserTe instanceof LaserTileEntity){
                ((LaserTileEntity) otherLaserTe).setDestination(this.getWorld().provider.getDimensionId(), this.getPos(), this.distance);
            }else{
                disconnect();
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return (oldState.getBlock() != newSate.getBlock());
    }

    public String getName(){
        return OpenRadio.MODID + ".laser";
    }

    @Override
    public boolean hasCustomName(){
        return false;
    }

    @Override
    public IChatComponent getDisplayName(){
        return null;
    }

    public void sendEntity(){
        double posX, posY, posZ, accX, accY, accZ;
        switch(this.getBlockMetadata()){
            case 0:
                posX = this.getPos().getX() + 0.5;
                posY = this.getPos().getY() + 0.5 - 1;
                posZ = this.getPos().getZ() + 0.5;
                accX = 0;
                accY = -OpenRadio.instance.settings.EntitySpeed;
                accZ = 0;
                break;
            case 1:
                posX = this.getPos().getX() + 0.5;
                posY = this.getPos().getY() + 0.5 + 1;
                posZ = this.getPos().getZ() + 0.5;
                accX = 0;
                accY = OpenRadio.instance.settings.EntitySpeed;
                accZ = 0;
                break;
            case 2:
                posX = this.getPos().getX() + 0.5;
                posY = this.getPos().getY() + 0.5;
                posZ = this.getPos().getZ() + 0.5 - 1;
                accX = 0;
                accY = 0;
                accZ = -OpenRadio.instance.settings.EntitySpeed;
                break;
            case 3:
                posX = this.getPos().getX() + 0.5;
                posY = this.getPos().getY() + 0.5;
                posZ = this.getPos().getZ() + 0.5 + 1;
                accX = 0;
                accY = 0;
                accZ = OpenRadio.instance.settings.EntitySpeed;
                break;
            case 4:
                posX = this.getPos().getX() + 0.5 - 1;
                posY = this.getPos().getY() + 0.5;
                posZ = this.getPos().getZ() + 0.5;
                accX = -OpenRadio.instance.settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            case 5:
                posX = this.getPos().getX() + 0.5 + 1;
                posY = this.getPos().getY() + 0.5;
                posZ = this.getPos().getZ() + 0.5;
                accX = OpenRadio.instance.settings.EntitySpeed;
                accY = 0;
                accZ = 0;
                break;
            default:
                posX = this.getPos().getX() + 0.5;
                posY = this.getPos().getY() + 0.5 + 1;
                posZ = this.getPos().getZ() + 0.5;
                accX = 0;
                accY = 0;
                accZ = 0;
        }

        this.getWorld().spawnEntityInWorld(new LaserEntity(this.worldObj, posX, posY, posZ, accX, accY, accZ, this.getWorld().provider.getDimensionId(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), getMaxDistance()));
    }

    public void setDestination(int dim, BlockPos pos, double distance){
        if(!this.getWorld().isRemote){
            this.otherLaser = new Location(dim, pos);
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
                TileEntity tileEntity = DimensionManager.getWorld(otherLaser.getDim()).getTileEntity(otherLaser.getPos());
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
    public void update(){
        super.update();
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


            //TODO don't do this every tick...
            if(this.worldObj.getTileEntity(this.getPos().add(1,0,0)) instanceof ILaserAddon){ //east
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(1,0,0)), EnumFacing.EAST.getIndex());
            }else{
                disconnectAddon(EnumFacing.EAST.getIndex());
            }
            if(this.worldObj.getTileEntity(this.getPos().add(-1,0,0)) instanceof ILaserAddon){ //west
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(-1,0,0)), EnumFacing.WEST.getIndex());
            }else{
                disconnectAddon(EnumFacing.WEST.getIndex());
            }
            if(this.worldObj.getTileEntity(this.getPos().add(0,1,0)) instanceof ILaserAddon){ //top
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(0,1,0)), EnumFacing.UP.getIndex());
            }else{
                disconnectAddon(EnumFacing.UP.getIndex());
            }
            if(this.worldObj.getTileEntity(this.getPos().add(0,-1,0)) instanceof ILaserAddon){ //bottom
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(0,-1,0)), EnumFacing.DOWN.getIndex());
            }else{
                disconnectAddon(EnumFacing.DOWN.getIndex());
            }
            if(this.worldObj.getTileEntity(this.getPos().add(0,0,1)) instanceof ILaserAddon){ //south
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(0,0,1)), EnumFacing.SOUTH.getIndex());
            }else{
                disconnectAddon(EnumFacing.SOUTH.getIndex());
            }
            if(this.worldObj.getTileEntity(this.getPos().add(0,0,-1)) instanceof ILaserAddon){ //north
                connectAddon((ILaserAddon) this.worldObj.getTileEntity(this.getPos().add(0,0,-1)), EnumFacing.NORTH.getIndex());
            }else{
                disconnectAddon(EnumFacing.NORTH.getIndex());
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
        if(slot == SLOT_LENS){
            if(stack != null)
                this.worldObj.setBlockState(pos, this.worldObj.getBlockState(this.pos).withProperty(LaserBlock.LENS, getItemTier(SLOT_LENS, Items.lensItem)));
            else
                this.worldObj.setBlockState(pos, this.worldObj.getBlockState(this.pos).withProperty(LaserBlock.LENS, 0));
        }
        this.markDirty();
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
    public ItemStack removeStackFromSlot(int index){
        return null;
    }

    /*@Override
    public ItemStack getStackInSlotOnClosing(int slot){
        ItemStack stack = getStackInSlot(slot);
        if(stack != null){
            setInventorySlotContents(slot, null);e
        }
        return stack;
    }*/

    @Override
    public int getInventoryStackLimit(){
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
        return player.getDistanceSq(this.getPos().getX()+0.5D, this.pos.getY()+0.5D, this.pos.getZ()+0.5D) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player){

    }

    @Override
    public void closeInventory(EntityPlayer player){

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        switch(slot){
            case 0:
                return itemStack.getItem() == Items.dspItem;
            case 1:
                return itemStack.getItem() == Items.photoReceptorItem;
            case 2:
                return itemStack.getItem() == Items.mirrorItem;
            case 3:
                return itemStack.getItem() == Items.lensItem;
            case 4:
                return itemStack.getItem() == Items.laserItem;
            default:
                return false;
        }
    }

    @Override
    public int getField(int id){
        return 0;
    }

    @Override
    public void setField(int id, int value){

    }

    @Override
    public int getFieldCount(){
        return 0;
    }

    @Override
    public void clear(){
        for(int i=0; i<this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
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
        return new S35PacketUpdateTileEntity(this.getPos(), 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        readFromNBT(pkt.getNbtCompound());
    }
}
