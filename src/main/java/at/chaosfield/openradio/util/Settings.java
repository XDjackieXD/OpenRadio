package at.chaosfield.openradio.util;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by Jakob Riepler (XDjackieXD)
 */
public class Settings{
    public double EntitySpeed = 1;
    public double DistancePerAir = 1;
    public double DistancePerTransparent = 2;
    public double LaserMaxDistanceTier[] = {64, 128, 256}; //Blocks
    public double LensMultiplierTier[] = {1, 3, 7};
    public int EnergyUseLaserTier[] = {25, 50, 100}; // RF/tick
    public int AEEnergyMultiplier = 10;
    public int EnergyBuffer = 100;

    public Settings(File configFile){
        Configuration config = new Configuration(configFile);

        config.load();

        EntitySpeed = (double)config.getFloat("LaserEntitySpeed", "misc", 1, 0.1f, 1, "speed of the connection-check entity (blocks/tick. max 1 for reliable block checking)");
        DistancePerAir = (double)config.getFloat("LaserDistancePerAir", "distances", 1, 0, 2048, "added distance per air block");
        DistancePerTransparent = (double)config.getFloat("LaserDistancePerTransparent", "distances", 2, 0, 2048, "added distance per transparent non-air block");

        LaserMaxDistanceTier[0] = (double)config.getFloat("LaserTier1MasDistance", "distances", 64, 0, 16384, "maximum distance of a tier 1 laser");
        LaserMaxDistanceTier[1] = (double)config.getFloat("LaserTier2MasDistance", "distances", 128, 0, 16384, "maximum distance of a tier 2 laser");
        LaserMaxDistanceTier[2] = (double)config.getFloat("LaserTier3MasDistance", "distances", 256, 0, 16384, "maximum distance of a tier 3 laser");

        LensMultiplierTier[0] = (double)config.getFloat("LensMultiplierTier1", "distances", 1, 0, 100, "distance multiplier of a tier 1 lens");
        LensMultiplierTier[1] = (double)config.getFloat("LensMultiplierTier2", "distances", 3, 0, 100, "distance multiplier of a tier 2 lens");
        LensMultiplierTier[2] = (double)config.getFloat("LensMultiplierTier3", "distances", 7, 0, 100, "distance multiplier of a tier 3 lens");

        EnergyUseLaserTier[0] = config.getInt("EnergyUseLaserTier1", "energy", 25, 0, 100000, "energy usage of a tier 1 laser");
        EnergyUseLaserTier[1] = config.getInt("EnergyUseLaserTier2", "energy", 50, 0, 100000, "energy usage of a tier 2 laser");
        EnergyUseLaserTier[2] = config.getInt("EnergyUseLaserTier3", "energy", 100, 0, 100000, "energy usage of a tier 3 laser");

        AEEnergyMultiplier = config.getInt("AEEncoderMultiplier", "energy", 10, 1, 10000, "energy usage multiplier if an AE Encoder is connected");

        EnergyBuffer = config.getInt("EnergyBuffer", "energy", 100, 10, 100000, "internal energy buffer size");

        config.save();
    }
}
