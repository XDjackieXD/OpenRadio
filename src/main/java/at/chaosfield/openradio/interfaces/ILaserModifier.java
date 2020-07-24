package at.chaosfield.openradio.interfaces;

import at.chaosfield.openradio.entity.LaserEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public interface ILaserModifier{
    void hitByLaser(LaserEntity laserEntity, BlockPos pos, World world, EnumFacing facing);
}
