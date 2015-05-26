package at.chaosfield.openradio.proxy;

import at.chaosfield.openradio.common.entity.LaserEntity;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemSnowball;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void registerRenders(){
        RenderingRegistry.registerEntityRenderingHandler(LaserEntity.class, new RenderSnowball(new ItemSnowball()));
    }

    @Override
    public void registerSounds(){

    }
}
