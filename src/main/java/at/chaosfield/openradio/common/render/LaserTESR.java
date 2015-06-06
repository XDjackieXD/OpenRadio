package at.chaosfield.openradio.common.render;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.init.Blocks;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import at.chaosfield.openradio.util.Location;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jakob Riepler (XDjackieXD)
 * Special render for the laser tileentity
 */
public class LaserTESR extends TileEntitySpecialRenderer{

    public LaserTESR(){

    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f){

        tileEntity.getWorldObj().scheduleBlockUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Blocks.laserBlock, 10);

        /*if(((LaserTileEntity) tileEntity).isConnected()){
            Location otherLaser = ((LaserTileEntity) tileEntity).getOtherLaser();
            OpenRadio.logger.info("render!");

            this.bindTexture(TextureMap.locationBlocksTexture);

            Tessellator tessellator = Tessellator.instance;
            GL11.glPushMatrix();
            GL11.glTranslated(x, y + 1, z); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
            tessellator.startDrawingQuads();

            tessellator.addVertexWithUV(0.4, 0.4, 0.4, 0, 0);
            tessellator.addVertexWithUV(0.4, 0.6, 0.4, 0, 1);
            tessellator.addVertexWithUV(0.4 + (otherLaser.getX() - tileEntity.xCoord), 0.6  + (otherLaser.getY() - tileEntity.yCoord), 0.4 + (otherLaser.getZ() - tileEntity.yCoord), 1, 1);
            tessellator.addVertexWithUV(0.4 + (otherLaser.getX() - tileEntity.xCoord), 0.4  + (otherLaser.getY() - tileEntity.yCoord), 0.4 + (otherLaser.getZ() - tileEntity.yCoord), 1, 0);

            tessellator.addVertexWithUV(0, 0, 0, 0, 0);
            tessellator.addVertexWithUV(1, 0, 0, 1, 0);
            tessellator.addVertexWithUV(1, 1, 0, 1, 1);
            tessellator.addVertexWithUV(0, 1, 0, 0, 1);

            tessellator.draw();
            GL11.glPopMatrix();
        }*/


        /*Location otherLaser = ((LaserTileEntity)tileEntity).getOtherLaser();
        if(otherLaser != null && ((LaserTileEntity)tileEntity).isConnected()){

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glLineWidth(6);
            GL11.glTranslated(x, y, z);
            GL11.glColor3ub((byte) 253, (byte) 0, (byte) 0);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3f(0.5f,0.5f,0.5f);
            GL11.glVertex3f(3.5f,0.5f,0.5f);
            //GL11.glVertex3f(otherLaser.getX() - tileEntity.xCoord + 0.5f, otherLaser.getY() - tileEntity.yCoord + 0.5f, otherLaser.getZ() - tileEntity.zCoord + 0.5f);
            GL11.glEnd();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glPopMatrix();
        }*/
    }
}
