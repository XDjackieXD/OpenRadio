package at.chaosfield.openradio.tileentity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.init.Items;
import at.chaosfield.openradio.integration.Init;
import at.chaosfield.openradio.integration.actuallyAdditions.LaserRelay;
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
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.items.ItemStackHandler;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserTileEntity extends TileEntityEnvironment implements IInventory, ITickable{

    public static final int SLOT_DSP = 0;
    public static final int SLOT_PHOTO_RECEPTOR = 1;
    public static final int SLOT_MIRROR = 2;
    public static final int SLOT_LASER = 3;

    private boolean powered;
    private double distance;
    private Location otherLaser;
    private int addonEnergyUsage = 0;

    private ILaserAddon connectedAddons[] = {null, null, null, null, null, null};
    private String connectedAddonsType[] = {null, null, null, null, null, null};

    private boolean first = true;

    private ItemStack[] inv;

    private int counter = 0;

    public final ItemStackHandler inventory;

    public LaserTileEntity(){
        super();
        node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.instance.settings.EnergyBuffer).create();

        inv = new ItemStack[4];
        for(int count=0; count<4; count++)
            inv[count] = ItemStack.EMPTY;

        inventory = new ItemStackHandler(4);
        if(otherLaser != null && !this.getWorld().isRemote){
            TileEntity otherLaserTe = DimensionManager.getWorld(otherLaser.getDim()).getTileEntity(otherLaser.getPos());
            if(otherLaserTe instanceof LaserTileEntity){
                ((LaserTileEntity) otherLaserTe).setDestination(this.getWorld().provider.getDimension(), this.getPos(), this.distance);
            }else{
                disconnect();
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
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
    public ITextComponent getDisplayName(){
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

        LaserEntity laserEntity = new LaserEntity(this.getWorld(), posX, posY, posZ, accX, accY, accZ,
                this.getWorld().provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), getMaxDistance(),
                getItemTier(SLOT_LASER, Items.laserItem));

        this.getWorld().spawnEntity(laserEntity);
    }

    public void setDestination(int dim, BlockPos pos, double distance){
        if(!this.getWorld().isRemote){
            this.otherLaser = new Location(dim, pos);
            this.distance = distance;
            for(ILaserAddon addon : getAddons())
                if(addon != null)
                    addon.laserConnectionStatusChanged(true);
            this.markDirty();
        }
    }

    public void disconnect(){
        for(ILaserAddon addon : getAddons())
            if(addon != null)
                addon.laserConnectionStatusChanged(false);
        this.otherLaser = null;
    }

    public void breakLaser(){
        for(EnumFacing side : EnumFacing.VALUES){
            disconnectAddon(side);
        }
    }

    public Location getOtherLaser(){
        return this.otherLaser;
    }

    public boolean isConnected(){
        return (otherLaser != null) && isPowered() && hasNeededComponents();
    }

    public boolean hasNeededComponents(){
        return inv[SLOT_DSP] != ItemStack.EMPTY &&
                inv[SLOT_PHOTO_RECEPTOR] != ItemStack.EMPTY &&
                inv[SLOT_MIRROR] != ItemStack.EMPTY &&
                inv[SLOT_LASER] != ItemStack.EMPTY &&
                inv[SLOT_DSP].getItem() == Items.dspItem &&
                inv[SLOT_PHOTO_RECEPTOR].getItem() == Items.photoReceptorItem &&
                inv[SLOT_MIRROR].getItem() == Items.mirrorItem &&
                inv[SLOT_LASER].getItem() == Items.laserItem;
    }

    public void connectAddon(String name, ILaserAddon addon, EnumFacing side){
        if(this.connectedAddonsType[side.getIndex()] == null || !this.connectedAddonsType[side.getIndex()].equals(name)){
            this.connectedAddons[side.getIndex()] = addon;
            this.connectedAddonsType[side.getIndex()] = addon.getAddonName();
            addon.connectToLaser(this);
        }
    }

    public void disconnectAddon(EnumFacing side){
        if(this.connectedAddons[side.getIndex()] != null){
            this.connectedAddons[side.getIndex()].disconnectFromLaser(this);
            this.connectedAddons[side.getIndex()] = null;
            this.connectedAddonsType[side.getIndex()] = null;
        }
    }

    public ILaserAddon[] getAddons(){
        return connectedAddons;
    }

    public int getItemTier(int slot, Object item){
        if(inv[slot] != ItemStack.EMPTY)
            if(inv[slot].getItem() == item)
                if(inv[slot].getItemDamage() <= 2 && inv[slot].getItemDamage() >= 0)
                    return inv[slot].getItemDamage() + 1;
        return 0;
    }

    public double getMaxDistance(){
        if(hasNeededComponents()){
            return OpenRadio.instance.settings.LaserMaxDistanceTier[getItemTier(SLOT_LASER, Items.laserItem) - 1];
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
            usage += OpenRadio.instance.settings.EnergyUseLaserTier[getItemTier(SLOT_LASER, Items.laserItem) - 1];
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


    public void onNeighbourChanged(BlockPos neighbour){
        TileEntity tile;
        String name;

        tile = this.getWorld().getTileEntity(neighbour);

        EnumFacing facing = EnumFacing.DOWN;
        if(neighbour.getX() == this.pos.getX()){
            if(neighbour.getY() == this.pos.getY()) {
                if(neighbour.getZ() > this.pos.getZ()) {
                    facing = EnumFacing.SOUTH;
                } else {
                    facing = EnumFacing.NORTH;
                }
            } else if(neighbour.getZ() == this.pos.getZ()) {
                if(neighbour.getY() > this.pos.getY()) {
                    facing = EnumFacing.UP;
                } else {
                    facing = EnumFacing.DOWN;
                }
            }
        } else if(neighbour.getY() == this.pos.getY() && neighbour.getZ() == this.pos.getZ()) {
            if(neighbour.getX() > this.pos.getX()) {
                    facing = EnumFacing.EAST;
                } else {
                    facing = EnumFacing.WEST;
                }
        }

        name = checkAddon(tile, facing);
        if(name != null){
            connectAddon(name, getAddon(tile, facing), facing);
        }else{
            disconnectAddon(facing);
        }

        addonEnergyUsage = 0;
        for(ILaserAddon addon : connectedAddons){
            if(addon != null)
                addonEnergyUsage += addon.getEnergyUsage();
        }

        for(ILaserAddon addon : getAddons())
            if(addon != null)
                addon.laserConnectionStatusChanged(isConnected());
    }

    @Override
    public void update(){
        if(!getWorld().isRemote){
            if(hasNeededComponents()){
                tryUsePower((int) (calculateBasicEnergyUsage() + addonEnergyUsage));
            }

            if(first){
                onNeighbourChanged(pos.north());
                onNeighbourChanged(pos.south());
                onNeighbourChanged(pos.east());
                onNeighbourChanged(pos.west());
                onNeighbourChanged(pos.up());
                onNeighbourChanged(pos.down());
                first = false;
            }

            if(hasNeededComponents() && isPowered()){
                counter++;
                if(counter >= 20){
                    counter = 0;
                    sendEntity();
                }
            }else{
                for(ILaserAddon addon : getAddons())
                    if(addon != null)
                        addon.laserConnectionStatusChanged(false);
                disconnect();
            }
        }

    }

    private String checkAddon(TileEntity tile, EnumFacing side){

        if(tile instanceof ILaserAddon)
            return ((ILaserAddon) tile).getAddonName();

        if(tile != null && (Init.loadedActAddVersion >= Init.minActAddVersion) && side == EnumFacing.UP){
            String tileName = tile.getBlockType().getRegistryName().toString();

            for(String name : Init.actAddLaserRelayEnergy)
                if(name.equals(tileName))
                    return "LaserRelayEnergy";
            for(String name : Init.actAddLaserRelayItem)
                if(name.equals(tileName))
                    return "LaserRelayItem";
            for(String name : Init.actAddLaserRelayFluid)
                if(name.equals(tileName))
                    return "LaserRelayFluid";
        }

        return null;
    }

    private ILaserAddon getAddon(TileEntity tile, EnumFacing side){
        if(tile instanceof ILaserAddon)
            return (ILaserAddon) tile;

        if(tile != null && (Init.loadedActAddVersion >= Init.minActAddVersion) && side == EnumFacing.UP){
            String tileName = tile.getBlockType().getRegistryName().toString();

            for(String name : Init.actAddLaserRelayEnergy)
                if(name.equals(tileName))
                    return new LaserRelay(tile);
            for(String name : Init.actAddLaserRelayItem)
                if(name.equals(tileName))
                    return new LaserRelay(tile);
            for(String name : Init.actAddLaserRelayFluid)
                if(name.equals(tileName))
                    return new LaserRelay(tile);
        }
        return null;
    }

    @Override
    public int getSizeInventory(){
        return inv.length;
    }

    @Override
    public boolean isEmpty(){
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        inv[slot] = stack;
        if(stack != ItemStack.EMPTY && stack.getCount() > getInventoryStackLimit()){
            stack.setCount(getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt){
        ItemStack stack = getStackInSlot(slot);
        if(stack != ItemStack.EMPTY){
            if(stack.getCount() <= amt){
                setInventorySlotContents(slot, ItemStack.EMPTY);
            }else{
                stack = stack.splitStack(amt);
                if(stack.getCount() == 0){
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
            this.markDirty();
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index){
        return ItemStack.EMPTY;
    }

    @Override
    public int getInventoryStackLimit(){
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player){
        return player.getDistanceSq(this.getPos().getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64;
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
        for(int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, ItemStack.EMPTY);
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
                inv[slot] = new ItemStack(tag);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound){
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
            if(stack != ItemStack.EMPTY){
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
        return tagCompound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.getPos(), 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        readFromNBT(pkt.getNbtCompound());
    }
}
