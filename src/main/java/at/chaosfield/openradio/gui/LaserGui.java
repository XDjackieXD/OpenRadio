package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.container.LaserContainer;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
@SideOnly(Side.CLIENT)
public class LaserGui extends GuiContainer{

    private ResourceLocation backgroundImage = new ResourceLocation(OpenRadio.MODID + ":" + "textures/gui/lasergui.png");   //Get the background Texture

    public LaserGui(IInventory inventoryPlayer, LaserTileEntity tileEntity){
        super(new LaserContainer(inventoryPlayer, tileEntity));
        this.xSize = 176;
        this.ySize = 183;
    }

    //Draw the background texture
    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(backgroundImage);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 183);
        this.fontRendererObj.drawString(I18n.translateToLocal("container." + OpenRadio.MODID + ".laser.name"), this.guiLeft + 8, this.guiTop + 8, 4210752);
        this.fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), this.guiLeft + 8, this.guiTop + 89, 4210752);
    }
}
