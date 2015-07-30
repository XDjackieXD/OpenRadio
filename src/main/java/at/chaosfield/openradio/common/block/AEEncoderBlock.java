package at.chaosfield.openradio.common.block;

import at.chaosfield.openradio.CreativeTab;
import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.AEEncoderTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class AEEncoderBlock extends Block implements ITileEntityProvider {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public AEEncoderBlock() {
        super(Material.iron);
        setBlockName(OpenRadio.MODID + ":aeencoder");   //Set localized Block name (/src/main/resources/assets/openradio/lang/)
        setHardness(3.0F);                              //Set hardness to 3
        setCreativeTab(CreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new AEEncoderTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata){
        return true;
    }

    //Register all Textures (client side only)
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        icon = register.registerIcon(OpenRadio.MODID + ":AEEncoder");
    }

    //This method is only used when the block is rendered in an inventory
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta){
        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){ //side 0=bottom, 1=top, 2=north, 3=south, 4=west, 5=east
        return icon;
    }
}
