package xyz.domi1819.uniq;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import xyz.domi1819.uniq.tweakers.IndustrialCraftTweaker;
import xyz.domi1819.uniq.tweakers.MinecraftTweaker;
import xyz.domi1819.uniq.tweakers.NuclearCraftTweaker;
import xyz.domi1819.uniq.tweakers.ThermalExpansionTweaker;

import java.util.ArrayList;

@Mod(modid = "uniq", dependencies = "after:*")
@SuppressWarnings("unused")
public class UniQ
{
    public Logger logger;

    private ArrayList<ITweaker> tweakers = new ArrayList<>();
    private Config config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        this.logger = event.getModLog();
        this.config = new Config(event.getSuggestedConfigurationFile()).load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.registerTweaker(new NuclearCraftTweaker());
        this.registerTweaker(new MinecraftTweaker());
        this.registerTweaker(new IndustrialCraftTweaker());
        this.registerTweaker(new ThermalExpansionTweaker());
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        ResourceUnifier unifier = new ResourceUnifier();

        this.logger.info("Building target list...");

        unifier.build(this.config);

        this.logger.info("Running tweakers...");

        for (ITweaker tweaker : this.tweakers)
        {
            String modId = tweaker.getModId();

            if (modId != null && !Loader.isModLoaded(modId))
            {
                continue;
            }

            try
            {
                tweaker.run(unifier);
            }
            catch (Exception ex)
            {
                this.logger.error("Tweaker " + tweaker.getName() + " threw an exception while running:", ex);
            }
        }

        this.logger.info("Done.");
    }

    public void registerTweaker(ITweaker tweaker)
    {
        this.tweakers.add(tweaker);
    }

    @Mod.Instance
    public static UniQ instance;
}
