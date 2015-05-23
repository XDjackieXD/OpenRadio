package at.chaosfield.openradio.common

import at.chaosfield.openradio.Settings
import at.chaosfield.openradio.common.block.LaserBlock
import at.chaosfield.openradio.common.tileentity.Laser
import cpw.mods.fml.common.registry
import cpw.mods.fml.common.registry.GameRegistry

object BlocksInit {
  def init() {
    GameRegistry.registerTileEntity(classOf[Laser], Settings.namespace + "laser")
    val laser = new LaserBlock()
    GameRegistry.registerBlock(laser, Settings.namespace + "laser")
  }
}
