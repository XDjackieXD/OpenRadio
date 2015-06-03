package at.chaosfield.openradio.common.entity;


import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.common.tileentity.LaserTileEntity;
import at.chaosfield.openradio.util.Location;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.*;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserEntity extends Entity implements IProjectile{

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block block;

    private String uid = "";
    private int laserX;
    private int laserY;
    private int laserZ;
    private int laserDim;

    private Location locNow;

    private List<Location> blocks = new ArrayList<Location>();

    public LaserEntity(World world){
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit(){
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double renderDistance){
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return renderDistance < d1 * d1;
    }

    public LaserEntity(World world, double x, double y, double z, double accX, double accY, double accZ, String uid, int laserDim, int laserX, int laserY, int laserZ){
        super(world);
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

        if(!worldObj.isRemote){
            this.locNow = new Location(world.provider.dimensionId, (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
            this.blocks.add(new Location(this.locNow.getDim(), this.locNow.getX(), this.locNow.getY(), this.locNow.getZ()));
        }
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
                    this.blocks.add(new Location(this.locNow.getDim(), this.locNow.getX(), this.locNow.getY(), this.locNow.getZ()));
                }
            }else{
                this.locNow = new Location(worldObj.provider.dimensionId, (int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ));
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
                }else if(this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).isOpaqueCube()){ //only collide with solid blocks
                    this.onImpact(movingobjectposition);
                }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void writeEntityToNBT(NBTTagCompound nbtTagCompound){
        nbtTagCompound.setInteger("laserX", this.laserX);
        nbtTagCompound.setInteger("laserY", this.laserY);
        nbtTagCompound.setInteger("laserZ", this.laserZ);
        nbtTagCompound.setInteger("laserDim", this.laserDim);
        nbtTagCompound.setString("laserId", this.uid);
        NBTTagList blocks = new NBTTagList();
        for(Location loc : this.blocks){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("X", loc.getX());
            tag.setInteger("Y", loc.getY());
            tag.setInteger("Z", loc.getZ());
            tag.setInteger("Dim", loc.getDim());
            blocks.appendTag(tag);
        }
        nbtTagCompound.setTag("Blocks", blocks);

        nbtTagCompound.setInteger("locNowX", this.locNow.getX());
        nbtTagCompound.setInteger("locNowY", this.locNow.getY());
        nbtTagCompound.setInteger("locNowZ", this.locNow.getZ());
        nbtTagCompound.setInteger("locNowDim", this.locNow.getDim());

        nbtTagCompound.setShort("xTile", (short) this.xTile);
        nbtTagCompound.setShort("yTile", (short) this.yTile);
        nbtTagCompound.setShort("zTile", (short) this.zTile);
        nbtTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(this.block));
    }

    public void readEntityFromNBT(NBTTagCompound nbtTagCompound){
        this.laserX = nbtTagCompound.getInteger("laserX");
        this.laserY = nbtTagCompound.getInteger("laserY");
        this.laserZ = nbtTagCompound.getInteger("laserZ");
        this.laserDim = nbtTagCompound.getInteger("laserDim");
        this.uid = nbtTagCompound.getString("laserId");

        NBTTagList blocks = nbtTagCompound.getTagList("Blocks", nbtTagCompound.getId());
        for(int i = 0; i < blocks.tagCount(); i++){
            NBTTagCompound tag = blocks.getCompoundTagAt(i);
            this.blocks.add(new Location(tag.getInteger("Dim"), tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z")));
        }

        this.locNow = new Location(0, 0, 0, 0);
        this.locNow.setX(nbtTagCompound.getInteger("locNowX"));
        this.locNow.setY(nbtTagCompound.getInteger("locNowY"));
        this.locNow.setZ(nbtTagCompound.getInteger("locNowZ"));
        this.locNow.setDim(nbtTagCompound.getInteger("locNowDim"));

        this.xTile = nbtTagCompound.getShort("xTile");
        this.yTile = nbtTagCompound.getShort("yTile");
        this.zTile = nbtTagCompound.getShort("zTile");
        this.block = Block.getBlockById(nbtTagCompound.getByte("inTile") & 255);
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize(){
        return 0.0F;
    }

    protected void onImpact(MovingObjectPosition mop){
        if(!worldObj.isRemote){
            OpenRadio.logger.info("Impact at X=" + this.posX + ", Y=" + this.posY + ", Z=" + this.posZ); //debugging!
            this.setDead();
            if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                TileEntity te = this.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                if(te instanceof LaserTileEntity){
                    if(mop.sideHit == te.getBlockMetadata())
                        ((LaserTileEntity) te).hitByLaserEntity(this.uid, this.laserDim, this.laserX, this.laserY, this.laserZ, this.blocks);
                    OpenRadio.logger.info("Hit Laser at X=" + mop.blockX + ", Y=" + mop.blockY + ", Z=" + mop.blockZ + " on block side " + mop.sideHit + ", the laser is on side " + te.getBlockMetadata() + ". my uid is: " + this.uid); //debugging!
                }
            }
        }
    }
}
