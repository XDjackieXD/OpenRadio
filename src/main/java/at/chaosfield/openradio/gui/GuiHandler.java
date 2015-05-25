package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.container.LaserContainer;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class GuiHandler implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof LaserTileEntity){
            return new LaserContainer(player.inventory, (LaserTileEntity) tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof LaserTileEntity){
            return new LaserGui(player.inventory, (LaserTileEntity) tileEntity);
        }
        return null;
    }

    public static void init(){
        NetworkRegistry.INSTANCE.registerGuiHandler(OpenRadio.instance, new GuiHandler());
    }
}
