package re.domi.uniq;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;
import re.domi.uniq.tweakers.IndustrialCraftTweaker;
import re.domi.uniq.tweakers.MinecraftTweaker;
import re.domi.uniq.tweaker.IGeneralTweaker;
import re.domi.uniq.tweaker.IRecipeTweaker;
import re.domi.uniq.tweakers.ThermalExpansionTweaker;
import re.domi.uniq.tweakers.recipe.BasicRecipeTweaker;
import re.domi.uniq.tweakers.recipe.MinecraftRecipeTweaker;

import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(modid = "uniq", dependencies = "before:jei")
public class UniQ
{
    public static Logger LOGGER;

    private Config config;

    private RecipeProcessor recipeProcessor;
    private ArrayList<IGeneralTweaker> tweakers = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        this.config = new Config(event.getSuggestedConfigurationFile()).load();
        this.recipeProcessor = new RecipeProcessor(this.config.printUnknownRecipeClasses);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.registerRecipeTweaker(new MinecraftRecipeTweaker("field_77575_e"), "net.minecraft.item.crafting.ShapedRecipes");
        this.registerRecipeTweaker(new MinecraftRecipeTweaker("field_77580_a"), "net.minecraft.item.crafting.ShapelessRecipes");

        this.registerRecipeTweaker(new BasicRecipeTweaker("ic2"), "ic2.core.recipe.AdvRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("ic2"), "ic2.core.recipe.AdvShapelessRecipe");

        this.registerTweaker(new MinecraftTweaker(this.recipeProcessor));
        this.registerTweaker(new ThermalExpansionTweaker());
        this.registerTweaker(new IndustrialCraftTweaker());
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        ResourceUnifier unifier = new ResourceUnifier();

        LOGGER.info("Registering custom OreDict entries...");
        unifier.addOredictNames(this.config.customOreDictNames);

        LOGGER.info("Preparing unification maps...");
        unifier.prepareUnificationMaps(this.config);

        LOGGER.info("Setting up recipe tweakers...");
        this.recipeProcessor.setup();

        LOGGER.info("Running tweakers...");
        for (IGeneralTweaker tweaker : this.tweakers)
        {
            try
            {
                tweaker.run(unifier);
            }
            catch (ReflectiveOperationException ex)
            {
                LOGGER.error("Tweaker " + tweaker.getName() + " threw an exception while running:", ex);
            }
        }

        LOGGER.info("Done.");
    }

    public void registerRecipeTweaker(IRecipeTweaker craftingTweaker, String recipeClassName)
    {
        this.recipeProcessor.registerRecipeTweaker(craftingTweaker, recipeClassName);
    }

    public void registerTweaker(IGeneralTweaker tweaker)
    {
        String modId = tweaker.getModId();

        if (modId.equals("") || Loader.isModLoaded(modId))
        {
            this.tweakers.add(tweaker);
        }
    }
}
