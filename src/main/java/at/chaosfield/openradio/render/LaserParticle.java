package at.chaosfield.openradio.render;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */

@SideOnly(Side.CLIENT)
public class LaserParticle extends Particle{
    float reddustParticleScale;

    public LaserParticle(World worldObj, double posX, double posY, double posZ, float scale, float colorR, float colorG, float colorB, float alpha){
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
        this.particleAlpha = alpha;
        this.reddustParticleScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_){
        float f14 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
        f14 = MathHelper.clamp(f14, 0.0F, 1.0F);
        this.particleScale = this.reddustParticleScale * f14;

        float f = (float) this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float) this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null)
        {
            f = this.particleTexture.getMinU();
            f1 = this.particleTexture.getMaxU();
            f2 = this.particleTexture.getMinV();
            f3 = this.particleTexture.getMaxV();
        }

        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        int j = 0;
        int k = 32767;
        worldRendererIn.pos((double) (f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double) (f6 - p_180434_5_ * f4), (double) (f7 - p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double) (f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double) (f6 + p_180434_5_ * f4), (double) (f7 - p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double) (f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double) (f6 + p_180434_5_ * f4), (double) (f7 + p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double) (f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double) (f6 - p_180434_5_ * f4), (double) (f7 + p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
    }
}
