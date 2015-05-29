package at.chaosfield.openradio.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class LaserAirBlock extends Block {
    protected LaserAirBlock() {
        super(Material.air);
    }

    public int getRenderType() {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
        return false;
    }

    public void dropBlockAsItemWithChance(World world, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {}

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int metadata) {
        super.onBlockPreDestroy(world, x, y, z, metadata);
        //TODO find out how to store the positon of the laser which placed this block so it can notify the laser if it gets broken

    }
}
