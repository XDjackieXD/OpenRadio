package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.container.LaserContainer;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
@SideOnly(Side.CLIENT)
public class LaserGui extends GuiContainer{

    private int xSize = 176, ySize = 183;

    private ResourceLocation backgroundimage = new ResourceLocation(OpenRadio.MODID + ":" + "textures/gui/LaserGui.png");   //Get the background Texture

    public LaserGui(InventoryPlayer inventoryPlayer, LaserTileEntity tileEntity){
        super(new LaserContainer(inventoryPlayer, tileEntity));
    }

    //Draw the background texture
    @Override
    public void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int x = (this.width - xSize)/2;
        int y = (this.height - ySize)/2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    //Draw the text
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y){
        //TODO find a way to get the text y-position without trail-and-error :P
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container." + OpenRadio.MODID + ":laser.name"), 8, -3, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 78, 4210752);
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
}
