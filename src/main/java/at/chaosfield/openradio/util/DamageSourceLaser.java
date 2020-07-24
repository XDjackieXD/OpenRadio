package at.chaosfield.openradio.util;

import at.chaosfield.openradio.OpenRadio;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class DamageSourceLaser extends DamageSource{
    public static final DamageSource DAMAGE_SOURCE_LASER = new DamageSourceLaser("laser", 5).setDamageBypassesArmor();

    private final int messageCount;

    public DamageSourceLaser(String name, int messageCount){
        super(name);
        this.messageCount = messageCount;
    }

    @Nonnull
    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity){
        String locTag = "death." + OpenRadio.MODID + "." + this.damageType + "." + (new Random().nextInt(this.messageCount)+1);
        return new TextComponentTranslation(locTag, entity.getDisplayName());
    }
}
