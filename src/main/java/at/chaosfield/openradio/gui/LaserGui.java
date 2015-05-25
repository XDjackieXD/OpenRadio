package at.chaosfield.openradio.gui;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.container.LaserContainer;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
@SideOnly(Side.CLIENT)
public class LaserGui extends GuiContainer{

    private int x, y, z;
    private EntityPlayer player;
    private World world;
    private int xSize = 176, ySize = 183;

    private ResourceLocation backgroundimage = new ResourceLocation(OpenRadio.MODID + ":" + "textures/gui/LaserGui.png");

    public LaserGui(InventoryPlayer inventoryPlayer, LaserTileEntity tileEntity){
        super(new LaserContainer(inventoryPlayer, tileEntity));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int x = (this.width - xSize)/2;
        int y = (this.height - ySize)/2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
}
