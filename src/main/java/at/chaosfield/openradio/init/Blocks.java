package at.chaosfield.openradio.init;

//import at.chaosfield.openradio.block.AEEncoderBlock;
import at.chaosfield.openradio.OpenRadio;
import at.chaosfield.openradio.block.BaseItemBlock;
import at.chaosfield.openradio.block.LaserBlock;
//import net.minecraftforge.fml.common.Loader;
import at.chaosfield.openradio.block.LensBlock;
import at.chaosfield.openradio.block.MirrorBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jakob (Jack/XDjackieXD)
 */
public class Blocks {
    public static LaserBlock laserBlock = null;
    public static LensBlock lensBlock1 = null;
    public static LensBlock lensBlock2 = null;
    public static LensBlock lensBlock3 = null;
    public static MirrorBlock mirrorBlock = null;
    //public static AEEncoderBlock aeencoderBlock = null;

    //Register all blocks (Has to be called during FML Init)
    public static void init(){
        laserBlock = new LaserBlock();
        lensBlock1 = new LensBlock(1);
        lensBlock2 = new LensBlock(2);
        lensBlock3 = new LensBlock(3);
        mirrorBlock = new MirrorBlock();
        
        registerBlock(laserBlock, new BaseItemBlock(laserBlock), "laser");
        registerBlock(lensBlock1, new BaseItemBlock(lensBlock1), "lenst1");
        registerBlock(lensBlock2, new BaseItemBlock(lensBlock2), "lenst2");
        registerBlock(lensBlock3, new BaseItemBlock(lensBlock3), "lenst3");
        registerBlock(mirrorBlock, new BaseItemBlock(mirrorBlock), "blockmirror");
        /*if(Loader.isModLoaded("appliedenergistics2")) {
            aeencoderBlock = new AEEncoderBlock();
            ItemUtil.registerBlock(aeencoderBlock, "aeencoder");
        }*/
    }

    public static void registerBlock(Block block, BaseItemBlock itemBlock, String name){
        block.setUnlocalizedName(OpenRadio.MODID+"."+name);
        block.setRegistryName(OpenRadio.MODID, name);
        GameRegistry.register(block);
        itemBlock.setRegistryName(block.getRegistryName());
        GameRegistry.register(itemBlock);
    }
}
