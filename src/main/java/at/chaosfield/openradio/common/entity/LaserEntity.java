package at.chaosfield.openradio.common.entity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserEntity extends Entity implements IProjectile{

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block block;
    protected boolean inGround;
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;

    private String uid = "";
    private int laserX;
    private int laserY;
    private int laserZ;
    private int laserDim;

    public LaserEntity(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double renderDistance) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return renderDistance < d1 * d1;
    }

    public LaserEntity(World world, double x, double y, double z, double accX, double accY, double accZ, String uid, int laserDim, int laserX, int laserY, int laserZ){
        super(world);
        this.ticksInGround = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
        this.uid = uid;
        this.laserDim = laserDim;
        this.laserX = laserX;
        this.laserY = laserY;
        this.laserZ = laserZ;
    }

    public void setThrowableHeading(double accX, double accY, double accZ, float accMult, float accRand) {
        float f2 = MathHelper.sqrt_double(accX * accX + accY * accY + accZ * accZ);
        accX /= (double)f2;
        accY /= (double)f2;
        accZ /= (double)f2;
        accX += this.rand.nextGaussian() * 0.007499999832361937D * (double)accRand;
        accY += this.rand.nextGaussian() * 0.007499999832361937D * (double)accRand;
        accZ += this.rand.nextGaussian() * 0.007499999832361937D * (double)accRand;
        accX *= (double)accMult;
        accY *= (double)accMult;
        accZ *= (double)accMult;
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
        float f3 = MathHelper.sqrt_double(accX * accX + accZ * accZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(accX, accZ) * 180.0D / 3.141592653589793D);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(accY, (double)f3) * 180.0D / 3.141592653589793D);
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double accX, double accY, double accZ) {
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
    }

    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if(this.inGround) {
            if(this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.block) {
                ++this.ticksInGround;
                if(this.ticksInGround == 1200) {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
            ++this.ticksInAir;
        }

        Vec3 posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(posVec, nextPosVec);
        posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if(movingobjectposition != null) {
            nextPosVec = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if(!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double f3 = 0.0D;

            for(int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity)list.get(j);
                if(entity1.canBeCollidedWith() && this.ticksInAir >= 5) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(posVec, nextPosVec);
                    if(movingobjectposition1 != null) {
                        double d1 = posVec.distanceTo(movingobjectposition1.hitVec);
                        if(d1 < f3 || f3 == 0.0D) {
                            entity = entity1;
                            f3 = d1;
                        }
                    }
                }
            }

            if(entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if(movingobjectposition != null) {
            if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
                this.setInPortal();
            } else {
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("laserX", this.laserX);
        nbtTagCompound.setInteger("laserY", this.laserY);
        nbtTagCompound.setInteger("laserZ", this.laserZ);
        nbtTagCompound.setInteger("laserDim", this.laserDim);
        nbtTagCompound.setShort("xTile", (short) this.xTile);
        nbtTagCompound.setShort("yTile", (short) this.yTile);
        nbtTagCompound.setShort("zTile", (short) this.zTile);
        nbtTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(this.block));
        nbtTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        if((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getCommandSenderName();
        }

        nbtTagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
        this.laserX = nbtTagCompound.getInteger("laserX");
        this.laserY = nbtTagCompound.getInteger("laserY");
        this.laserZ = nbtTagCompound.getInteger("laserZ");
        this.laserDim = nbtTagCompound.getInteger("laserDim");
        this.xTile = nbtTagCompound.getShort("xTile");
        this.yTile = nbtTagCompound.getShort("yTile");
        this.zTile = nbtTagCompound.getShort("zTile");
        this.block = Block.getBlockById(nbtTagCompound.getByte("inTile") & 255);
        this.inGround = nbtTagCompound.getByte("inGround") == 1;
        this.throwerName = nbtTagCompound.getString("ownerName");
        if(this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }

    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public EntityLivingBase getThrower() {
        if(this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
        }

        return this.thrower;
    }

    protected void onImpact(MovingObjectPosition mop){
        if(!worldObj.isRemote){
            OpenRadio.logger.info("Impact at X=" + this.posX + ", Y=" + this.posY + ", Z="+ this.posZ); //debugging!
            this.setDead();
            if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                TileEntity te = this.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                if(te instanceof LaserTileEntity){
                    if(mop.sideHit == te.getBlockMetadata())
                        ((LaserTileEntity) te).hitByLaserEntity(this.uid, this.laserDim, this.laserX, this.laserY, this.laserZ);
                    OpenRadio.logger.info("Hit Laser at X=" + mop.blockX + ", Y=" + mop.blockY + ", Z=" + mop.blockZ + " on block side " + mop.sideHit + ", the laser is on side " + te.getBlockMetadata() + ". my uid is: " + this.uid); //debugging!
                }
            }
        }
    }
}
