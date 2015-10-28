package at.chaosfield.openradio.common.entity;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import at.chaosfield.openradio.util.Location;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserEntity extends Entity implements IProjectile{

    private Location senderLaser;
    private Location locNow;

    private double distance = 1;
    private double maxDistance = 0;

    private float colourR, colourG, colourB;
    Vec3 lastParticle = Vec3.createVectorHelper(0, 0, 0);
    private int lastParticleDim;

    public LaserEntity(World world){
        super(world);
        this.setSize(0.25F, 0.25F);
        this.colourR = 1.0F;
        this.colourG = 0;
        this.colourB = 0;
    }

    protected void entityInit(){
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double renderDistance){
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return renderDistance < d1 * d1;
    }

    public LaserEntity(World world, double x, double y, double z, double accX, double accY, double accZ, int laserDim, int laserX, int laserY, int laserZ, double maxDistance, float colourR, float colourG, float colourB){
        super(world);
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
        this.senderLaser = new Location(laserDim, laserX, laserY, laserZ);
        this.maxDistance = maxDistance;

        if(!worldObj.isRemote){
            this.locNow = new Location(world.provider.dimensionId, (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        }

        this.colourR = colourR;
        this.colourG = colourG;
        this.colourB = colourB;
        this.lastParticle = Vec3.createVectorHelper(x, y, z);
        this.lastParticleDim = laserDim;
    }

    public void setThrowableHeading(double accX, double accY, double accZ, float accMult, float accRand){
        float f2 = MathHelper.sqrt_double(accX * accX + accY * accY + accZ * accZ);
        accX /= (double) f2;
        accY /= (double) f2;
        accZ /= (double) f2;
        accX += this.rand.nextGaussian() * 0.007499999832361937D * (double) accRand;
        accY += this.rand.nextGaussian() * 0.007499999832361937D * (double) accRand;
        accZ += this.rand.nextGaussian() * 0.007499999832361937D * (double) accRand;
        accX *= (double) accMult;
        accY *= (double) accMult;
        accZ *= (double) accMult;
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
        float f3 = MathHelper.sqrt_double(accX * accX + accZ * accZ);
        //noinspection SuspiciousNameCombination
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(accX, accZ) * 180.0D / 3.141592653589793D);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(accY, (double) f3) * 180.0D / 3.141592653589793D);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double accX, double accY, double accZ){
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
    }

    public void onUpdate(){
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if(!worldObj.isRemote){
            if(this.locNow != null){
                if(((int) Math.floor(this.posX) != this.locNow.getX()) || ((int) Math.floor(this.posY) != this.locNow.getY()) || ((int) Math.floor(this.posZ) != this.locNow.getZ()) || (worldObj.provider.dimensionId != this.locNow.getDim())){
                    this.locNow.setX((int) Math.floor(this.posX));
                    this.locNow.setY((int) Math.floor(this.posY));
                    this.locNow.setZ((int) Math.floor(this.posZ));
                    this.locNow.setDim(worldObj.provider.dimensionId);
                    Block block = DimensionManager.getWorld(this.locNow.getDim()).getBlock(this.locNow.getX(), this.locNow.getY(), this.locNow.getZ());
                    if(block.isAir(DimensionManager.getWorld(this.locNow.getDim()), this.locNow.getX(), this.locNow.getY(), this.locNow.getZ())){
                        distance += OpenRadio.instance.settings.DistancePerAir;
                    }else if(!block.getMaterial().isSolid()){
                        distance += OpenRadio.instance.settings.DistancePerTransparent;
                    }
                }
            }else{
                this.locNow = new Location(worldObj.provider.dimensionId, (int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ));
            }
            if(distance > maxDistance){
                this.setDead();
            }
        }else{
            Vec3.createVectorHelper(this.posX, this.posY, this.posZ).distanceTo(lastParticle);
            if(lastParticleDim != this.worldObj.provider.dimensionId || (Vec3.createVectorHelper(this.posX, this.posY, this.posZ).distanceTo(lastParticle) >= 1.0)){
                lastParticleDim = this.worldObj.provider.dimensionId;
                lastParticle.xCoord = this.posX;
                lastParticle.yCoord = this.posY;
                lastParticle.zCoord = this.posZ;
                EntityReddustFX particle = new EntityReddustFX(this.worldObj, this.posX, this.posY, this.posZ, 0.75F, this.colourR, this.colourG, this.colourB);
                particle.setRBGColorF(this.colourR, this.colourG, this.colourB);
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            }
        }

        Vec3 posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(posVec, nextPosVec);


        //Don't collide with entities
        /*posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if(movingobjectposition != null){
            nextPosVec = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if(!this.worldObj.isRemote){
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double f3 = 0.0D;

            for(Object aList : list){
                Entity entity1 = (Entity) aList;
                if(entity1.canBeCollidedWith()){
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(posVec, nextPosVec);
                    if(movingobjectposition1 != null){
                        double d1 = posVec.distanceTo(movingobjectposition1.hitVec);
                        if(d1 < f3 || f3 == 0.0D){
                            entity = entity1;
                            f3 = d1;
                        }
                    }
                }
            }

            if(entity != null){
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }*/

        if(movingobjectposition != null){
            if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                if(this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal){
                    this.setInPortal();
                }else if(this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).getMaterial().isSolid()){ //only collide with solid blocks
                    this.onImpact(movingobjectposition);
                }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.setPosition(this.posX, this.posY, this.posZ);
    }


    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
        tagCompound.setIntArray("senderLaser", new int[]{this.senderLaser.getDim(), this.senderLaser.getX(), this.senderLaser.getY(), this.senderLaser.getZ()});
        tagCompound.setTag("colour", this.newFloatNBTList(this.colourR, this.colourG, this.colourB));
        tagCompound.setInteger("lastParticleDim", this.lastParticleDim);
        tagCompound.setDouble("distance", this.distance);
        tagCompound.setDouble("macDistance", this.maxDistance);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        if(tagCompound.hasKey("direction", 9)) {
            NBTTagList nbttaglist = tagCompound.getTagList("direction", 6);
            this.motionX = nbttaglist.func_150309_d(0);
            this.motionY = nbttaglist.func_150309_d(1);
            this.motionZ = nbttaglist.func_150309_d(2);
        } else {
            this.setDead();
        }
        int[] sender = tagCompound.getIntArray("senderLaser");
        this.senderLaser = new Location(sender[0], sender[1], sender[2], sender[3]);

        NBTTagList nbttaglist = tagCompound.getTagList("colour", 5);
        this.colourR = nbttaglist.func_150308_e(0);
        this.colourG = nbttaglist.func_150308_e(1);
        this.colourB = nbttaglist.func_150308_e(2);
OpenRadio.logger.info(this.colourR + " " + this.colourG + " " + this.colourB);
        this.lastParticleDim = tagCompound.getInteger("lastParticleDim");
        this.distance = tagCompound.getDouble("distance");
        this.maxDistance = tagCompound.getDouble("maxDistance");
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound nbtTagCompound){
        //Don't save to disk!
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize(){
        return 0.0F;
    }

    protected void onImpact(MovingObjectPosition mop){
        this.setDead();
        if(!worldObj.isRemote){
            TileEntity senderLaserTe = DimensionManager.getWorld(senderLaser.getDim()).getTileEntity(senderLaser.getX(), senderLaser.getY(), senderLaser.getZ());
            if(senderLaserTe instanceof LaserTileEntity){
                if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                    TileEntity te = this.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                    if(te instanceof LaserTileEntity){
                        if(mop.sideHit == te.getBlockMetadata())
                            ((LaserTileEntity) senderLaserTe).setDestination(this.worldObj.provider.dimensionId, mop.blockX, mop.blockY, mop.blockZ, this.distance);
                        else
                            ((LaserTileEntity) senderLaserTe).disconnect();
                    }else
                        ((LaserTileEntity) senderLaserTe).disconnect();
                }else
                    ((LaserTileEntity) senderLaserTe).disconnect();
            }
        }
    }
}
