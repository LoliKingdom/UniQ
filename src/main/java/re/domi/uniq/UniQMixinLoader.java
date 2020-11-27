package re.domi.uniq;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import re.domi.uniq.fluids.FluidBlocksHandler;
import zone.rong.mixinbooter.MixinLoader;

@MixinLoader
public class UniQMixinLoader {

    {
        ModContainer modContainer = Loader.instance().getActiveModList().stream().filter(m -> m.getModId().equals("uniq")).findFirst().get();
        Loader.instance().setActiveModContainer(modContainer);
        MinecraftForge.EVENT_BUS.register(new FluidBlocksHandler()); // Ensure we're the first to listen to the events
        Loader.instance().setActiveModContainer(null);
    }

}
