package at.chaosfield.openradio.proxy;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void registerRenders(){
        OBJLoader.instance.addDomain(OpenRadio.MODID);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(Blocks.laserBlock), 0, new ModelResourceLocation("openradio:laser", "inventory"));
    }

    @Override
    public void registerSounds(){

    }
}
