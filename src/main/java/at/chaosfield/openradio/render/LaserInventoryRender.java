package at.chaosfield.openradio.render;

import at.chaosfield.openradio.OpenRadio;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserInventoryRender implements ISimpleBlockRenderingHandler{

    private int renderID;
    private IModelCustom laserModel;
    private ResourceLocation laserTexture;

    public LaserInventoryRender(int renderID){
        this.renderID = renderID;
        laserModel = AdvancedModelLoader.loadModel(new ResourceLocation(OpenRadio.MODID + ":" + "models/blocks/laser.obj"));
        laserTexture = new ResourceLocation(OpenRadio.MODID + ":" + "textures/blocks/Laser.png");
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        GL11.glPushMatrix();
        GL11.glTranslated(0, -0.5, 0);

        GL11.glScalef(0.03125F, 0.03125F, 0.03125F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(laserTexture);
        laserModel.renderAll();

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId){
        return true;
    }

    @Override
    public int getRenderId(){
        return renderID;
    }
}
