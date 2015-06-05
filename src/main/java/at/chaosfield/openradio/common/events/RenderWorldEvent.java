package at.chaosfield.openradio.common.events;


import at.chaosfield.openradio.util.LocationPair;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class RenderWorldEvent{

    public List<LocationPair> lasers = new ArrayList<LocationPair>();

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt){
        double playerX = Minecraft.getMinecraft().thePlayer.prevPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * evt.partialTicks;
        double playerY = Minecraft.getMinecraft().thePlayer.prevPosY + (Minecraft.getMinecraft().thePlayer.posY - Minecraft.getMinecraft().thePlayer.prevPosY) * evt.partialTicks;
        double playerZ = Minecraft.getMinecraft().thePlayer.prevPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * evt.partialTicks;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glLineWidth(6);
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        GL11.glColor3ub((byte)253,(byte)0,(byte)0);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        for(LocationPair loc: lasers){
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3f(loc.getLoc1().getX() + 0.5f, loc.getLoc1().getY() + 0.5f, loc.getLoc1().getZ() + 0.5f);
            GL11.glVertex3f(loc.getLoc2().getX() + 0.5f, loc.getLoc2().getY() + 0.5f, loc.getLoc2().getZ() + 0.5f);
            GL11.glEnd();
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}