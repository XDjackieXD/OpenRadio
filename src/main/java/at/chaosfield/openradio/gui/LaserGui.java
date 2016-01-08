package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.container.LaserContainer;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
@SideOnly(Side.CLIENT)
public class LaserGui extends GuiContainer{

    private ResourceLocation backgroundimage = new ResourceLocation(OpenRadio.MODID + ":" + "textures/gui/LaserGui.png");   //Get the background Texture

    public LaserGui(InventoryPlayer inventoryPlayer, LaserTileEntity tileEntity){
        super(new LaserContainer(inventoryPlayer, tileEntity));
        this.xSize = 176;
        this.ySize = 183;
    }

    //Draw the background texture
    @Override
    public void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(backgroundimage);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 183);
    }

    //Draw the text
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y){
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container." + OpenRadio.MODID + ":laser.name"), 8, 8, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 89, 4210752);
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
}
