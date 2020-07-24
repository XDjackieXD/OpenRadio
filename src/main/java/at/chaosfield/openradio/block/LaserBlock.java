package at.chaosfield.openradio.block;

import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import at.chaosfield.openradio.gui.GUIs;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class LaserBlock extends BlockContainer implements ITileEntityProvider{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public LaserBlock(){
        super(Material.IRON);                           //Material is like Iron
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(OpenRadio.MODID + ".laser"); //Set unlocalized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                              //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new LaserTileEntity();
    }

    @Override
    public boolean hasTileEntity(IBlockState state){
        return true;
    }

    @Override protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    //On right click open the GUI (only on the server side and if the player isn't sneaking)
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        //OpenRadio.logger.info("state: FACING: " + state.getValue(FACING).getName() + " LENS: " + state.getValue(LENS));
        if(!world.isRemote) {
            if (world.getTileEntity(pos) != null && !player.isSneaking())
                player.openGui(OpenRadio.instance, GUIs.LASER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    //If the block gets broken, drop all items on the floor
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        dropItems(world, pos);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof LaserTileEntity){
            ((LaserTileEntity) tileEntity).disconnect();
            ((LaserTileEntity) tileEntity).breakLaser();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        if (!((World)world).isRemote)
        {
            LaserTileEntity tile = (LaserTileEntity)world.getTileEntity(pos);
            if(tile != null){
                tile.onNeighbourChanged(neighbor);
            }
        }
    }

    //randomly drop the items around the block
    private void dropItems(World world, BlockPos pos){
        Random rand = new Random();

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != ItemStack.EMPTY && item.getCount() > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
                        new ItemStack(item.getItem(), item.getCount(), item.getItemDamage()));

                if (item.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntity(entityItem);
                item.setCount(0);
            }
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }
}