package at.chaosfield.openradio.entity;

import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.block.LaserBlock;
import at.chaosfield.openradio.interfaces.ILaserModifier;
import at.chaosfield.openradio.render.LaserParticle;
import at.chaosfield.openradio.tileentity.LaserTileEntity;
import at.chaosfield.openradio.util.DamageSourceLaser;
import at.chaosfield.openradio.util.Location;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jakob Riepler (XDjackieXD)
 */

public class LaserEntity extends Entity implements IProjectile, IEntityAdditionalSpawnData{

    private List<Location> appliedModifier = new ArrayList<Location>();

    private Location senderLaser;
    private Location locNow;

    private double distance = 1;
    private double maxDistance = 0;
    private double multiplier = 1;

    private float colorR = 1.0F, colorG = 0, colorB = 0, colorA = 1.0F;
    private int laserTier = 1;
    Vec3d lastParticle = new Vec3d(0, 0, 0);
    private int lastParticleDim;

    public LaserEntity(World world){
        super(world);
        this.setSize(0.25F, 0.25F);
        this.setInvisible(true);
    }

    protected void entityInit(){
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double renderDistance){
        /*double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return renderDistance < d1 * d1;*/
        return false;
    }

    public LaserEntity(World world, double x, double y, double z, double accX, double accY, double accZ, int laserDim, int laserX, int laserY, int laserZ, double maxDistance, int laserTier){
        this(world);
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);

        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
        this.senderLaser = new Location(laserDim, laserX, laserY, laserZ);
        this.maxDistance = maxDistance;

        if(!this.getEntityWorld().isRemote){
            this.locNow = new Location(world.provider.getDimension(), (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        }

        this.lastParticle = new Vec3d(x, y, z);
        this.lastParticleDim = laserDim;
        this.laserTier = laserTier;
    }

    public void setThrowableHeading(double accX, double accY, double accZ, float accMult, float accRand){
        float f2 = MathHelper.sqrt(accX * accX + accY * accY + accZ * accZ);
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
        float f3 = MathHelper.sqrt(accX * accX + accZ * accZ);
        //noinspection SuspiciousNameCombination
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(accX, accZ) * 180.0D / 3.141592653589793D);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(accY, (double) f3) * 180.0D / 3.141592653589793D);
    }

    public void setVelocity(double accX, double accY, double accZ){
        this.motionX = accX;
        this.motionY = accY;
        this.motionZ = accZ;
    }

    @Override
    public void onEntityUpdate(){
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;


        /**** Replace Super onUpdate because some not-needed stuff in there ****/
        this.getEntityWorld().theProfiler.startSection("entityBaseTick");

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if(!this.getEntityWorld().isRemote && this.getEntityWorld() instanceof WorldServer){
            this.getEntityWorld().theProfiler.startSection("portal");
            MinecraftServer minecraftserver = this.getEntityWorld().getMinecraftServer();
            int i = this.getMaxInPortalTime();

            if(this.inPortal){
                if(minecraftserver != null && minecraftserver.getAllowNether()){
                    if(this.getRidingEntity() == null && this.portalCounter++ >= i){
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;

                        if(this.getEntityWorld().provider.getDimension() == -1){
                            j = 0;
                        }else{
                            j = -1;
                        }

                        this.changeDimension(j);
                    }

                    this.inPortal = false;
                }
            }else{
                if(this.portalCounter > 0){
                    this.portalCounter -= 4;
                }

                if(this.portalCounter < 0){
                    this.portalCounter = 0;
                }
            }

            if(this.timeUntilPortal > 0){
                --this.timeUntilPortal;
            }

            this.getEntityWorld().theProfiler.endSection();
        }

        if(this.posY < -64.0D){
            this.kill();
        }

        this.firstUpdate = false;
        this.getEntityWorld().theProfiler.endSection();
        /***********************************************************************/


        if(!this.getEntityWorld().isRemote){
            if(this.locNow != null){
                if(((int) Math.floor(this.posX) != this.locNow.getX()) || ((int) Math.floor(this.posY) != this.locNow.getY()) || ((int) Math.floor(this.posZ) != this.locNow.getZ()) || (this.getEntityWorld().provider.getDimension() != this.locNow.getDim())){
                    this.locNow.setX((int) Math.floor(this.posX));
                    this.locNow.setY((int) Math.floor(this.posY));
                    this.locNow.setZ((int) Math.floor(this.posZ));
                    this.locNow.setDim(this.getEntityWorld().provider.getDimension());
                    Block block = DimensionManager.getWorld(this.locNow.getDim()).getBlockState(this.locNow.getPos()).getBlock();
                    if(block.isAir(DimensionManager.getWorld(this.locNow.getDim()).getBlockState(this.locNow.getPos()), DimensionManager.getWorld(this.locNow.getDim()), this.locNow.getPos())){
                        distance += OpenRadio.instance.settings.DistancePerAir;
                    }else if(!block.getBlockState().getBaseState().getMaterial().isSolid()){
                        distance += OpenRadio.instance.settings.DistancePerTransparent;
                    }
                }
            }else{
                this.locNow = new Location(this.getEntityWorld().provider.getDimension(), (int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ));
            }
            if(distance > maxDistance * multiplier){
                this.setDead();
            }
        }else{
            if(lastParticleDim != this.getEntityWorld().provider.getDimension() || ((new Vec3d(this.posX, this.posY, this.posZ)).distanceTo(lastParticle) >= 1.0)){
                lastParticleDim = this.getEntityWorld().provider.getDimension();
                lastParticle = new Vec3d(this.posX, this.posY, this.posZ);
                renderParticle();
            }
        }

        if(!this.getEntityWorld().isRemote){
            List<Entity> list = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ));
            for(Entity entity : list){
                if(entity instanceof EntityLivingBase && entity.canBeCollidedWith()){
                    if(!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()))
                        switch(this.laserTier){
                            case 2:
                                entity.attackEntityFrom(DamageSourceLaser.DAMAGE_SOURCE_LASER, 3);
                                entity.setFire(1);
                                break;
                            case 3:
                                entity.attackEntityFrom(DamageSourceLaser.DAMAGE_SOURCE_LASER, 6);
                                entity.setFire(10);
                                break;
                            default:
                        }
                }
            }
        }

        Vec3d posVec = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d nextPosVec = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult movingobjectposition = this.getEntityWorld().rayTraceBlocks(posVec, nextPosVec);


        if(movingobjectposition != null){
            if(movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
                if(this.getEntityWorld().getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.PORTAL){
                    this.setPortal(this.getPosition());
                }else{
                    if(movingobjectposition.getBlockPos().getX() == MathHelper.floor(this.posX) &&
                            movingobjectposition.getBlockPos().getY() == MathHelper.floor(this.posY) &&
                            movingobjectposition.getBlockPos().getZ() == MathHelper.floor(this.posZ)){
                        movingobjectposition.sideHit = movingobjectposition.sideHit.getOpposite();
                    }
                    this.onImpact(movingobjectposition);
                }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void setTier(int laserTier){
        this.colorR = OpenRadio.instance.settings.LaserColor[laserTier - 1][0];
        this.colorG = OpenRadio.instance.settings.LaserColor[laserTier - 1][1];
        this.colorB = OpenRadio.instance.settings.LaserColor[laserTier - 1][2];
        this.colorA = OpenRadio.instance.settings.LaserColor[laserTier - 1][3];
    }

    public void addDistance(int blocks){
        distance += blocks;
    }

    @SideOnly(Side.CLIENT)
    private void renderParticle(){
        LaserParticle particle = new LaserParticle(this.getEntityWorld(), this.posX, this.posY, this.posZ, 0.75F, this.colorR, this.colorG, this.colorB, this.colorA);
        particle.setRBGColorF(this.colorR, this.colorG, this.colorB);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound){
        tagCompound.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
        tagCompound.setIntArray("senderLaser", new int[]{this.senderLaser.getDim(), this.senderLaser.getX(), this.senderLaser.getY(), this.senderLaser.getZ()});
        tagCompound.setTag("color", this.newFloatNBTList(this.colorR, this.colorG, this.colorB, this.colorA));
        tagCompound.setInteger("lastParticleDim", this.lastParticleDim);
        tagCompound.setDouble("distance", this.distance);
        tagCompound.setDouble("maxDistance", this.maxDistance);
        tagCompound.setDouble("multiplier", this.multiplier);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound){
        if(tagCompound.hasKey("direction", 9)){
            NBTTagList nbttaglist = tagCompound.getTagList("direction", 6);
            this.motionX = nbttaglist.getDoubleAt(0);
            this.motionY = nbttaglist.getDoubleAt(1);
            this.motionZ = nbttaglist.getDoubleAt(2);
        }else{
            this.setDead();
        }
        int[] sender = tagCompound.getIntArray("senderLaser");
        this.senderLaser = new Location(sender[0], sender[1], sender[2], sender[3]);

        NBTTagList nbttaglist = tagCompound.getTagList("color", 5);
        this.colorR = nbttaglist.getFloatAt(0);
        this.colorG = nbttaglist.getFloatAt(1);
        this.colorB = nbttaglist.getFloatAt(2);
        this.colorA = nbttaglist.getFloatAt(3);
        this.lastParticleDim = tagCompound.getInteger("lastParticleDim");
        this.distance = tagCompound.getDouble("distance");
        this.maxDistance = tagCompound.getDouble("maxDistance");
        this.multiplier = tagCompound.getDouble("multiplier");
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound nbtTagCompound){
        //Don't save to disk!
        return false;
    }

    protected void onImpact(RayTraceResult mop){
        TileEntity senderLaserTe = null;
        if(!getEntityWorld().isRemote){
            senderLaserTe = DimensionManager.getWorld(senderLaser.getDim()).getTileEntity(senderLaser.getPos());
        }

        if(mop.typeOfHit == RayTraceResult.Type.BLOCK){
            Block hitBlock = this.getEntityWorld().getBlockState(mop.getBlockPos()).getBlock();

            if(hitBlock instanceof LaserBlock && senderLaserTe instanceof LaserTileEntity){
                TileEntity te = this.getEntityWorld().getTileEntity(mop.getBlockPos());
                if(te instanceof LaserTileEntity){
                    if(mop.sideHit.getIndex() == te.getBlockMetadata()){
                        ((LaserTileEntity) senderLaserTe).setDestination(this.getEntityWorld().provider.getDimension(), mop.getBlockPos(), this.distance);
                    }else
                        ((LaserTileEntity) senderLaserTe).disconnect();
                }else
                    ((LaserTileEntity) senderLaserTe).disconnect();
                this.setDead();
            }else if(hitBlock instanceof ILaserModifier){
                if(!appliedModifier.contains(new Location(this.dimension, mop.getBlockPos()))){
                    appliedModifier.add(new Location(this.dimension, mop.getBlockPos()));
                    ((ILaserModifier) hitBlock).hitByLaser(this, mop.getBlockPos(), this.getEntityWorld(), mop.sideHit);
                }
            }else if(this.getEntityWorld().getBlockState(mop.getBlockPos()).isOpaqueCube()){
                this.setDead();
                if(senderLaserTe instanceof LaserTileEntity)
                    ((LaserTileEntity) senderLaserTe).disconnect();
            }

        }else{
            this.setDead();
            if(senderLaserTe instanceof LaserTileEntity)
                ((LaserTileEntity) senderLaserTe).disconnect();
        }
    }

    public double getMultiplier(){
        return multiplier;
    }

    public void setMultiplier(double multiplier){
        this.multiplier = multiplier;
    }

    public double getMaxDistance(){
        return maxDistance;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer){
        buffer.writeInt(this.laserTier);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData){
        this.laserTier = additionalData.readInt();
        setTier(this.laserTier);
    }
}
