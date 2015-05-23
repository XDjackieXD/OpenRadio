package at.chaosfield.openradio

import cpw.mods.fml.common.{FMLLog, Mod}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent

@Mod(name = "Open Radio", modid = "OpenRadio", version = "0.1", modLanguage = "scala")
object OpenRadio {
  @EventHandler
  def init(event: FMLInitializationEvent) {
    FMLLog.info("OpenRadio has been loaded!")
  }
}