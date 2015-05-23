package at.chaosfield.openradio.common.tileentity


import at.chaosfield.openradio.OpenRadio
import cpw.mods.fml.common.Optional
import li.cil.oc.api.API
import li.cil.oc.api.network.{Connector, Visibility}
import li.cil.oc.api.prefab.TileEntityEnvironment

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
class Laser extends TileEntityEnvironment {
  var otherSide = null
  var powered = false
  var laserPower = 10

  def Laser(){
    node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(OpenRadio.energyBuffer).create()
  }

  def getComponentName(): String = {
    "Laser"
  }

  override def updateEntity() ={
    super.updateEntity()
    if(!worldObj.isRemote){
      val lastStatus: Boolean = powered
      if((node()!=null) && node().asInstanceOf[Connector].tryChangeBuffer(laserPower*OpenRadio.energyMultiplier)){
        powered = true
      }else{
        powered = false
      }
    }
  }

  def isPowered(): Boolean = {
    powered
  }
}
