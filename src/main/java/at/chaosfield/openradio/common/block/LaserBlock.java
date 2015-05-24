package at.chaosfield.openradio.common.block;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
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
    IIcon sideIcon;
    IIcon topIcon;

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
    public void registerBlockIcons(IIconRegister register){
        blockIcon = register.registerIcon(OpenRadio.MODID + ":laserBottom");
        sideIcon = register.registerIcon(OpenRadio.MODID + ":laserSide");
        topIcon = register.registerIcon(OpenRadio.MODID + ":laserTop");
    }

    @Override
    public IIcon getIcon(int side, int metadata){
        switch (side){
            case 0: return blockIcon;
            case 1: return topIcon;
            default: return sideIcon;
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