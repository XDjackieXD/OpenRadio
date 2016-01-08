package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.container.LaserContainer;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class GuiHandler implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if(tileEntity instanceof LaserTileEntity){  //If The TileEntity is a Laser, open the Laser GUI (Server side)
            return new LaserContainer(player.inventory, (LaserTileEntity) tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if(tileEntity instanceof LaserTileEntity){  //If The TileEntity is a Laser, open the Laser GUI (Client side)
            return new LaserGui(player.inventory, (LaserTileEntity) tileEntity);
        }
        return null;
    }

    //Register all GUIs (has to be called during FML Init)
    public static void init(){
        NetworkRegistry.INSTANCE.registerGuiHandler(OpenRadio.instance, new GuiHandler());
    }
}
