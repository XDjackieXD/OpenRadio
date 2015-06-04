package at.chaosfield.openradio.common.events;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.util.Location;
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
        GL11.glPushMatrix();
        GL11.glTranslated(0.5 - Minecraft.getMinecraft().thePlayer.posX, 0.5 - Minecraft.getMinecraft().thePlayer.posY, 0.5 - Minecraft.getMinecraft().thePlayer.posZ);
        GL11.glColor3ub((byte) 255, (byte) 0, (byte) 0);

        GL11.glBegin(GL11.GL_LINE);
        for(LocationPair loc: lasers){
            GL11.glVertex3f(loc.getLoc1().getX(), loc.getLoc1().getY(), loc.getLoc1().getZ());
            GL11.glVertex3f(loc.getLoc2().getX(), loc.getLoc2().getY(), loc.getLoc2().getZ());
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}