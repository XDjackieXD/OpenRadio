package at.chaosfield.openradio.common.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import at.chaosfield.openradio.gui.GUIs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
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
    @SideOnly(Side.CLIENT)
    private IIcon[] Icons;

    public LaserBlock(){
        super(Material.iron);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockName(OpenRadio.MODID + ":laser");
        setHardness(3.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new LaserTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata){
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        Icons = new IIcon[6];
        Icons[0] = register.registerIcon(OpenRadio.MODID + ":laser0"); //bottom
        Icons[1] = register.registerIcon(OpenRadio.MODID + ":laser1"); //top
        Icons[2] = register.registerIcon(OpenRadio.MODID + ":laser2"); //north
        Icons[3] = register.registerIcon(OpenRadio.MODID + ":laser3"); //south
        Icons[4] = register.registerIcon(OpenRadio.MODID + ":laser4"); //west
        Icons[5] = register.registerIcon(OpenRadio.MODID + ":laser5"); //east
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata){
        return Icons[side];
    }

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
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

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