package at.chaosfield.openradio.render;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.init.Blocks;
import at.chaosfield.openradio.init.Items;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jakob Riepler (XDjackieXD)
 * Special render for the laser tileentity
 */
public class LaserTESR extends TileEntitySpecialRenderer{

    private IModelCustom laserModel, lensModel;
    private ResourceLocation laserTexture, lensTexture1;

    public LaserTESR(){
        laserModel = AdvancedModelLoader.loadModel(new ResourceLocation(OpenRadio.MODID + ":" + "models/blocks/laser.obj"));
        laserTexture = new ResourceLocation(OpenRadio.MODID + ":" + "textures/blocks/Laser.png");
        lensModel = AdvancedModelLoader.loadModel(new ResourceLocation(OpenRadio.MODID + ":" + "models/blocks/lens.obj"));
        lensTexture1 = new ResourceLocation(OpenRadio.MODID + ":" + "textures/blocks/LensT1.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f){
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        if(tileEntity.hasWorldObj())
            switch(tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord)){
                case 0:
                    GL11.glTranslated(0, 0.5, -0.5);
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case 1:
                    GL11.glTranslated(0, 0.5, 0.5);
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case 2:
                    GL11.glRotated(180, 0, 1, 0);
                    break;
                case 3:
                    //GL11.glRotated(0, 0, 1, 0); //no rotation is useless ^^
                    break;
                case 4:
                    GL11.glRotated(270, 0, 1, 0);
                    break;
                case 5:
                    GL11.glRotated(90, 0, 1, 0);
                    break;
                default:
                    break;
            }

        GL11.glScalef(0.03125F, 0.03125F, 0.03125F);

        bindTexture(laserTexture);
        laserModel.renderAll();

        switch(((LaserTileEntity) tileEntity).getItemTier(LaserTileEntity.SLOT_LENS, Items.lensItem)){
            case 1:
                bindTexture(lensTexture1);
                lensModel.renderAll();
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }

        GL11.glPopMatrix();
    }
}
