package at.chaosfield.openradio.block;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import at.chaosfield.openradio.gui.GUIs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserBlock extends BlockContainer implements ITileEntityProvider{

    //Textures for the block faces (Client side only)
    @SideOnly(Side.CLIENT)
    private IIcon[] Icons;

    public LaserBlock(){
        super(Material.iron);                       //Material is like Iron
        setBlockName(OpenRadio.MODID + ":laser");   //Set localized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                          //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new LaserTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata){
        return true;
    }

    //Register all Textures (client side only)
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        Icons = new IIcon[2];
        Icons[0] = register.registerIcon(OpenRadio.MODID + ":LaserFront");
        Icons[1] = register.registerIcon(OpenRadio.MODID + ":LaserSide");
    }

    //This method is only used when the block is rendered in an inventory
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta){
        if(side == 4) return Icons[0];
        else return Icons[1];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){ //side 0=bottom, 1=top, 2=north, 3=south, 4=west, 5=east
        return (side == world.getBlockMetadata(x, y, z)) ? this.Icons[0] : this.Icons[1];
    }


    //On right click open the GUI (only on the server side and if the player isn't sneaking)
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            if (world.getTileEntity(x, y, z) != null && !player.isSneaking())
                player.openGui(OpenRadio.instance, GUIs.LASER.ordinal(), world, x, y, z);
            return true;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        int direction = BlockPistonBase.determineOrientation(world, x, y, z, entity);
        world.setBlockMetadataWithNotify(x, y, z, direction, 2);
    }

    //If the block gets broken, drop all items on the floor
    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        dropItems(world, x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof LaserTileEntity)
            ((LaserTileEntity)tileEntity).disconnect();
        super.breakBlock(world, x, y, z, par5, par6);
    }

    //randomly drop the items around the block
    private void dropItems(World world, int x, int y, int z){
        Random rand = new Random();

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }
}