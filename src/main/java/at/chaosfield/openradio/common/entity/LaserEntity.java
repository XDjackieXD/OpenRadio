package at.chaosfield.openradio.common.entity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserEntity extends EntityThrowable{

    public LaserEntity(World world, double x, double y, double z){
        super(world, x, y, z);
    }

    public LaserEntity(World world, double x, double y, double z, double accX, double accY, double accZ){
        super(world, x, y, z);
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
    }

    public LaserEntity(World world, EntityLivingBase entityLivingBase){
        super(world, entityLivingBase);
    }

    public LaserEntity(World world){
        super(world);
    }

    @Override
    protected void onImpact(MovingObjectPosition mop){
        OpenRadio.logger.info("Impact at X=" + this.posX + ", Y=" + this.posY + ", Z="+ this.posZ); //debugging!
        if(!worldObj.isRemote)
            this.setDead();
        if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
            TileEntity te = this.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if(te instanceof LaserTileEntity){
                OpenRadio.logger.info("Hit Laser at X=" + mop.blockX + ", Y=" + mop.blockY + ", Z=" + mop.blockZ); //debugging!
            }
        }
    }

    @Override
    protected float getGravityVelocity(){
        return 0;
    }
}
