package at.chaosfield.openradio.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.interfaces.ILaserModifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class LensBlock extends Block implements ILaserModifier{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public LensBlock(int tier){
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(OpenRadio.MODID + ".lenst" + tier); //Set unlocalized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                             //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
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

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    private static final AxisAlignedBB AABB_X = new AxisAlignedBB(0.375F, 0, 0, 0.625F, 1, 1);
    private static final AxisAlignedBB AABB_Y = new AxisAlignedBB(0, 0, 0.375F, 1, 1, 0.625F);
    private static final AxisAlignedBB AABB_Z = new AxisAlignedBB(0, 0.375F, 0, 1, 0.625F, 1);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        EnumFacing.Axis axis = state.getValue(FACING).getAxis();

        if (axis == EnumFacing.Axis.X)
            return AABB_X;
        else if (axis == EnumFacing.Axis.Z)
            return AABB_Y;
        else if (axis == EnumFacing.Axis.Y)
            return AABB_Z;

        return AABB_X;
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
