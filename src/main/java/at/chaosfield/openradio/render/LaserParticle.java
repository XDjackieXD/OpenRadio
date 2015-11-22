package at.chaosfield.openradio.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */

@SideOnly(Side.CLIENT)
public class LaserParticle extends EntityFX{
    float reddustParticleScale;

    public LaserParticle(World worldObj, double posX, double posY, double posZ, float colorR, float colorG, float colorB){
        this(worldObj, posX, posY, posZ, 1.0F, colorR, colorG, colorB);
    }

    public LaserParticle(World worldObj, double posX, double posY, double posZ, float scale, float colorR, float colorG, float colorB){
        super(worldObj, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;

        float f4 = (float) Math.random() * 0.4F + 0.6F;
        this.particleRed = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * colorR * f4;
        this.particleGreen = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * colorG * f4;
        this.particleBlue = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * colorB * f4;
        this.particleScale *= 0.75F;
        this.particleScale *= scale;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
        this.noClip = false;
    }

    public void renderParticle(Tessellator tesselator, float par2, float par3, float par4, float par5, float par6, float par7){
        float f14 = ((float) this.particleAge + par2) / (float) this.particleMaxAge * 32.0F;

        if(f14 < 0.0F){
            f14 = 0.0F;
        }

        if(f14 > 1.0F){
            f14 = 1.0F;
        }

        this.particleScale = this.reddustParticleScale * f14;
        float f6 = (float) this.particleTextureIndexX / 16.0F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float) this.particleTextureIndexY / 16.0F;
        float f9 = f8 + 0.0624375F;
        float f10 = 0.1F * this.particleScale;

        if(this.particleIcon != null){
            f6 = this.particleIcon.getMinU();
            f7 = this.particleIcon.getMaxU();
            f8 = this.particleIcon.getMinV();
            f9 = this.particleIcon.getMaxV();
        }

        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) par2 - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) par2 - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) par2 - interpPosZ);
        tesselator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tesselator.setBrightness(255);
        tesselator.addVertexWithUV((double) (f11 - par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 - par5 * f10 - par7 * f10), (double) f7, (double) f9);
        tesselator.addVertexWithUV((double) (f11 - par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 - par5 * f10 + par7 * f10), (double) f7, (double) f8);
        tesselator.addVertexWithUV((double) (f11 + par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 + par5 * f10 + par7 * f10), (double) f6, (double) f8);
        tesselator.addVertexWithUV((double) (f11 + par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 + par5 * f10 - par7 * f10), (double) f6, (double) f9);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate(){
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if(this.particleAge++ >= this.particleMaxAge){
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if(this.posY == this.prevPosY){
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if(this.onGround){
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
