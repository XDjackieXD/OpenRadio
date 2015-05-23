package at.chaosfield.openradio.common.block

import at.chaosfield.openradio.{OpenRadio, Settings}
import at.chaosfield.openradio.common.tileentity.Laser
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World

class LaserBlock(material: Material = Material.iron) extends BlockContainer(material){
  setBlockName(Settings.namespace + "laser")
  setHardness(3.0F)
  setCreativeTab(CreativeTabs.tabBlock)

  var side: IIcon = null
  var top: IIcon = null

  override def createNewTileEntity(world: World, metadata: Int): TileEntity = {
    new Laser().asInstanceOf[TileEntity]
  }

  override def registerBlockIcons(register: IIconRegister) {
    blockIcon = register.registerIcon(Settings.namespace + "laserBottom")
    side = register.registerIcon(Settings.namespace + "laserSide")
    top = register.registerIcon(Settings.namespace + "laserTop")
  }

  override def getIcon(side: Int, metadata: Int): IIcon = {
    side match {
      case 0 => blockIcon
      case 1 => top
      case _ => this.side
    }
  }
}
