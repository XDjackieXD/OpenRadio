package at.chaosfield.openradio

import at.chaosfield.openradio._
import at.chaosfield.openradio.common.BlocksInit
import cpw.mods.fml.common.{FMLLog, Mod}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLInitializationEvent}
import org.apache.logging.log4j.LogManager

@Mod(name = "Open Radio", modid = "openradio", version = "0.1", modLanguage = "scala", dependencies = "required-after:OpenComputers@[1.5.0,)")
object OpenRadio {

  final val Name = "OpenRadio"
  var log = LogManager.getLogger(Name)

  val energyBuffer = 100
  val energyMultiplier = 1

  @EventHandler
  def preInit(e: FMLPreInitializationEvent) {
    log = e.getModLog
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    BlocksInit.init()
    OpenRadio.log.info("successfully loaded!")
  }
}