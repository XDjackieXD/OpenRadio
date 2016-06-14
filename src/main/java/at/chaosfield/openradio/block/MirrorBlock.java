package at.chaosfield.openradio.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.interfaces.ILaserModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class MirrorBlock extends Block implements ILaserModifier{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public MirrorBlock(){
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(OpenRadio.MODID + ".blockmirror"); //Set unlocalized Block name (/src/main/resources/assets/openradio/lang/)
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
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        // no need to figure out the right orientation again when the piston block can do it for us
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer)), 2);
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
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void hitByLaser(LaserEntity laserEntity, BlockPos pos, World world, EnumFacing direction){
        laserEntity.setVelocity(direction.getDirectionVec().getX()*OpenRadio.instance.settings.EntitySpeed, direction.getDirectionVec().getY()*OpenRadio.instance.settings.EntitySpeed, direction.getDirectionVec().getZ()*OpenRadio.instance.settings.EntitySpeed);
    }
}
