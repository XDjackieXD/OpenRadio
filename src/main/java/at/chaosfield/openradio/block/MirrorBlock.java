package at.chaosfield.openradio.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.entity.LaserEntity;
import at.chaosfield.openradio.gui.CreativeTab;
import at.chaosfield.openradio.interfaces.ILaserModifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class MirrorBlock extends Block implements ILaserModifier{

    public static final PropertyDirection FACING_HORIZONTAL = PropertyDirection.create("facingh", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger FACING_VERTICAL = PropertyInteger.create("facingv", 0, 3);
    public MirrorBlock(){
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_HORIZONTAL, EnumFacing.NORTH).withProperty(FACING_VERTICAL, 0));
        setUnlocalizedName(OpenRadio.MODID + ".blockmirror"); //Set unlocalized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                             //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
    }

    @Override protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_HORIZONTAL, FACING_VERTICAL);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing f6, float f7, float f8, float f9){
        if(player.isSneaking()){
            int rot = state.getValue(FACING_VERTICAL)+1;
            if(rot >= 4) rot=0;
            world.setBlockState(pos, state.withProperty(FACING_VERTICAL, rot));
            return true;
        }
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     * being storage-space efficient often results in pretty ugly code >_>
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        int facingv = ((meta&12)>>>2);

        return getDefaultState().withProperty(FACING_HORIZONTAL, EnumFacing.getHorizontal(meta&3)).withProperty(FACING_VERTICAL, facingv);
    }

    /**
     * Convert the BlockState into the correct metadata value
     * being storage-space efficient often results in pretty ugly code >_>
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int facingv = (state.getValue(FACING_VERTICAL) << 2) & 12;
        int metah = (state.getValue(FACING_HORIZONTAL).getHorizontalIndex()&3);

        /*switch(metah){
            case 0: //S
                switch(facingv){
                    case 0: //D
                        metav = 0;
                        break;
                    case 4: //W
                        metav = 1;
                        break;
                    case 1: //U
                        metav = 2;
                        break;
                    case 5: //E
                        metav = 3;
                        break;
                    default:
                        OpenRadio.logger.warn("wtf did just happen?! this should not be able to happen.");
                }
                break;
            case 1: //W
                switch(facingv){
                    case 0: //D
                        metav = 0;
                        break;
                    case 2: //N
                        metav = 1;
                        break;
                    case 1: //U
                        metav = 2;
                        break;
                    case 3: //S
                        metav = 3;
                        break;
                    default:
                        OpenRadio.logger.warn("wtf did just happen?! this should not be able to happen.");
                }
                break;
            case 2: //N
                switch(facingv){
                    case 0: //D
                        metav = 0;
                        break;
                    case 5: //E
                        metav = 1;
                        break;
                    case 1: //U
                        metav = 2;
                        break;
                    case 4: //W
                        metav = 3;
                        break;
                    default:
                        OpenRadio.logger.warn("wtf did just happen?! this should not be able to happen.");
                }
                break;
            case 3: //E
                switch(facingv){
                    case 0: //D
                        metav = 0;
                        break;
                    case 3: //S
                        metav = 1;
                        break;
                    case 1: //U
                        metav = 2;
                        break;
                    case 2: //N
                        metav = 3;
                        break;
                    default:
                        OpenRadio.logger.warn("wtf did just happen?! this should not be able to happen.");
                }
                break;
            default:
                OpenRadio.logger.warn("wtf did just happen?! this should not be able to happen.");
        }*/

        return metah + facingv;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // no need to figure out the right orientation again when the piston block can do it for us
        world.setBlockState(pos, state.withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite()).withProperty(FACING_VERTICAL, 0), 2);
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

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer){
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumFacing facingvFromMetav(int metav, EnumFacing facingh){
        if(facingh == EnumFacing.NORTH){
            switch(metav){
                case 0:
                    return EnumFacing.UP;
                case 1:
                    return EnumFacing.WEST;
                case 2:
                    return EnumFacing.DOWN;
                case 3:
                    return EnumFacing.EAST;
                default:
                    OpenRadio.logger.warn("I think someone is tempering with the universe... o.O");
            }
        }else if(facingh == EnumFacing.EAST){
            switch(metav){
                case 0:
                    return EnumFacing.UP;
                case 1:
                    return EnumFacing.NORTH;
                case 2:
                    return EnumFacing.DOWN;
                case 3:
                    return EnumFacing.SOUTH;
                default:
                    OpenRadio.logger.warn("I think someone is tempering with the universe... o.O");
            }
        }else if(facingh == EnumFacing.SOUTH){
            switch(metav){
                case 0:
                    return EnumFacing.UP;
                case 1:
                    return EnumFacing.EAST;
                case 2:
                    return EnumFacing.DOWN;
                case 3:
                    return EnumFacing.WEST;
                default:
                    OpenRadio.logger.warn("I think someone is tempering with the universe... o.O");
            }
        }else if(facingh == EnumFacing.WEST){
            switch(metav){
                case 0:
                    return EnumFacing.UP;
                case 1:
                    return EnumFacing.SOUTH;
                case 2:
                    return EnumFacing.DOWN;
                case 3:
                    return EnumFacing.NORTH;
                default:
                    OpenRadio.logger.warn("I think someone is tempering with the universe... o.O");
            }
        }else{
            OpenRadio.logger.warn("I think someone is tempering with the universe... o.O");
        }

        return EnumFacing.UP;
    }

    @Override
    public void hitByLaser(LaserEntity laserEntity, BlockPos pos, World world, EnumFacing direction){
        IBlockState state = world.getBlockState(pos);
        EnumFacing facingv = facingvFromMetav(state.getValue(FACING_VERTICAL), state.getValue(FACING_HORIZONTAL));

        if(direction.equals(facingv)){ //laser is entering the facingv-side
            laserEntity.posX = pos.getX() + 0.5;
            laserEntity.posY = pos.getY() + 0.5;
            laserEntity.posZ = pos.getZ() + 0.5;
            laserEntity.setVelocity(
                    state.getValue(FACING_HORIZONTAL).getDirectionVec().getX() * OpenRadio.instance.settings.EntitySpeed,
                    state.getValue(FACING_HORIZONTAL).getDirectionVec().getY() * OpenRadio.instance.settings.EntitySpeed,
                    state.getValue(FACING_HORIZONTAL).getDirectionVec().getZ() * OpenRadio.instance.settings.EntitySpeed
            );
            laserEntity.addDistance(OpenRadio.instance.settings.MirrorDistancePenalty);
        }else if(direction.equals(state.getValue(FACING_HORIZONTAL))){ //laser is entering the facingh-side
            laserEntity.posX = pos.getX() + 0.5;
            laserEntity.posY = pos.getY() + 0.5;
            laserEntity.posZ = pos.getZ() + 0.5;
            laserEntity.setVelocity(
                    facingv.getDirectionVec().getX() * OpenRadio.instance.settings.EntitySpeed,
                    facingv.getDirectionVec().getY() * OpenRadio.instance.settings.EntitySpeed,
                    facingv.getDirectionVec().getZ() * OpenRadio.instance.settings.EntitySpeed
            );
            laserEntity.addDistance(OpenRadio.instance.settings.MirrorDistancePenalty);
        }else{
            laserEntity.setDead();
        }
    }
}
