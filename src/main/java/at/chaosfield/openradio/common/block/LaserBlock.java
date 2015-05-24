package at.chaosfield.openradio.common.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserBlock extends BlockContainer{
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        Icons = new IIcon[6];
        Icons[0] = register.registerIcon(OpenRadio.MODID + ":laser0");
        Icons[1] = register.registerIcon(OpenRadio.MODID + ":laser1");
        Icons[2] = register.registerIcon(OpenRadio.MODID + ":laser2");
        Icons[3] = register.registerIcon(OpenRadio.MODID + ":laser3");
        Icons[4] = register.registerIcon(OpenRadio.MODID + ":laser4");
        Icons[5] = register.registerIcon(OpenRadio.MODID + ":laser5");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata){
        switch(side){
            case 0: //bottom
                return Icons[0];
            case 1: //top
                return Icons[1];
            case 2: //north
                return Icons[2];
            case 3: //south
                return Icons[3];
            case 4: //west
                return Icons[4];
            case 5: //east
                return Icons[5];
            default:
                return Icons[0];
        }
    }

    public static <T> T getTileEntity(IBlockAccess world, int x, int y, int z, Class<T> T){
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null && T.isAssignableFrom(tile.getClass())){
            return (T) tile;
        }
        return null;
    }
}