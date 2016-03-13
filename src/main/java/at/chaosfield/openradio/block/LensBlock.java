package at.chaosfield.openradio.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.interfaces.ILaserModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LensBlock extends Block implements ILaserModifier{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public LensBlock(int tier){
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(OpenRadio.MODID + ".lenst" + tier); //Set unlocalized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                             //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
    }

    @Override protected BlockState createBlockState() {
        return new BlockState(this, FACING);
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

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        // no need to figure out the right orientation again when the piston block can do it for us
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, pos, placer)), 2);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube(){
        return false;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(FACING).getAxis();

        if (axis == EnumFacing.Axis.X)
            this.setBlockBounds(0.375F, 0, 0, 0.625F, 1, 1);
        else if (axis == EnumFacing.Axis.Z)
            this.setBlockBounds(0, 0, 0.375F, 1, 1, 0.625F);
        else if (axis == EnumFacing.Axis.Y)
            this.setBlockBounds(0, 0.375F, 0, 1, 0.625F, 1);
    }

    @Override
    public void hitByLaser(LaserEntity laserEntity, BlockPos pos, World world, EnumFacing direction){
        if(!world.isRemote){
            if(world.getBlockState(pos).getValue(FACING).getAxis() == direction.getAxis()){
                if(laserEntity.getMaxDistance() * (laserEntity.getMultiplier() + OpenRadio.instance.settings.LensMultiplierTier[getTier() - 1]) <= (OpenRadio.instance.settings.LensMultiplierTier[2] + 1) * 2 * OpenRadio.instance.settings.LaserMaxDistanceTier[2])
                    laserEntity.setMultiplier(laserEntity.getMultiplier() + OpenRadio.instance.settings.LensMultiplierTier[getTier() - 1]);
            }else
                laserEntity.setDead();
        }
    }

    private int getTier(){
        if(this.getUnlocalizedName().equals("tile." + OpenRadio.MODID + ".lenst1"))
            return 1;
        else if(this.getUnlocalizedName().equals("tile." + OpenRadio.MODID + ".lenst2"))
            return 2;
        else if(this.getUnlocalizedName().equals("tile." + OpenRadio.MODID + ".lenst3"))
            return 3;
        return 1;
    }
}
