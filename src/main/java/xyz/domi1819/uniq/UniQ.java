package xyz.domi1819.uniq;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.apache.logging.log4j.Logger;
import xyz.domi1819.uniq.tweaker.ICraftingTweaker;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;
import xyz.domi1819.uniq.tweakers.*;

import java.lang.reflect.Field;
import java.util.*;

@Mod(modid = "uniq", dependencies = "after:*")
@SuppressWarnings({"unused", "WeakerAccess"})
public class UniQ
{
    @Mod.Instance("uniq")
    public static UniQ instance;

    public Logger logger;
    public Config config;

    public ArrayList<IGeneralTweaker> tweakers = new ArrayList<>();
    public HashMap<String, ICraftingTweaker> craftingTweakers = new HashMap<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        this.logger = event.getModLog();
        this.config = new Config(event.getSuggestedConfigurationFile()).load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.registerCraftingTweaker(new BasicCraftingTweaker("", "recipeOutput"), "net.minecraft.item.crafting.ShapedRecipes");
        this.registerCraftingTweaker(new BasicCraftingTweaker("", "recipeOutput"), "net.minecraft.item.crafting.ShapelessRecipes");
        this.registerCraftingTweaker(new BasicCraftingTweaker(""), "net.minecraftforge.oredict.ShapedOreRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker(""), "net.minecraftforge.oredict.ShapelessOreRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker("IC2"), "ic2.core.AdvRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker("IC2"), "ic2.core.AdvShapelessRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker("Mekanism"), "mekanism.common.recipe.ShapedMekanismRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker("Mekanism"), "mekanism.common.recipe.ShapelessMekanismRecipe");
        this.registerCraftingTweaker(new ForestryCraftingTweaker(), "forestry.core.recipes.ShapedRecipeCustom");
        this.registerCraftingTweaker(new BasicCraftingTweaker("appliedenergistics2"), "appeng.recipes.game.ShapedRecipe");
        this.registerCraftingTweaker(new BasicCraftingTweaker("appliedenergistics2"), "appeng.recipes.game.ShapelessRecipe");

        this.registerTweaker(new MinecraftTweaker());
        this.registerTweaker(new ThermalExpansionTweaker());
        this.registerTweaker(new IndustrialCraftTweaker());
        this.registerTweaker(new ImmersiveEngineeringTweaker());
        this.registerTweaker(new MekanismTweaker());
        this.registerTweaker(new NuclearCraftTweaker());
        this.registerTweaker(new EnderIOTweaker());
        this.registerTweaker(new ForestryTweaker());
        this.registerTweaker(new RailcraftTweaker());
        this.registerTweaker(new AppliedEnergisticsTweaker());
        this.registerTweaker(new TinkersConstructTweaker());
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        this.logger.info("Building target list...");

        ResourceUnifier unifier = new ResourceUnifier().build(this.config);

        this.logger.info("Running crafting tweakers...");

        this.runCraftingTweakers(unifier);

        this.logger.info("Running other tweakers...");

        for (IGeneralTweaker tweaker : this.tweakers)
        {
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

    public void registerCraftingTweaker(ICraftingTweaker craftingTweaker, String recipeClassName)
    {
        String modId = craftingTweaker.getModId();

        if (modId.equals("") || Loader.isModLoaded(modId))
        {
            this.craftingTweakers.put(recipeClassName, craftingTweaker);
        }
    }

    public void registerTweaker(IGeneralTweaker tweaker)
    {
        String modId = tweaker.getModId();

        if (modId.equals("") || Loader.isModLoaded(modId))
        {
            this.tweakers.add(tweaker);
        }
    }

    @SuppressWarnings("unchecked")
    private void runCraftingTweakers(ResourceUnifier unifier)
    {
        Iterator<Map.Entry<String, ICraftingTweaker>> iterator = this.craftingTweakers.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, ICraftingTweaker> entry = iterator.next();

            try
            {
                entry.getValue().prepareTransform(entry.getKey());
            }
            catch (Exception ex)
            {
                this.logger.error("Crafting tweaker " + entry.getValue().getName() + " threw an exception while preparing and was disabled:", ex);
                iterator.remove();
            }
        }

        Set<String> unknownRecipeClasses = new HashSet<>();

        try
        {
            Field fRecipes = CraftingManager.class.getDeclaredField("recipes");

            fRecipes.setAccessible(true);

            List<IRecipe> recipes = (List<IRecipe>) fRecipes.get(CraftingManager.getInstance());

            for (IRecipe recipe : recipes)
            {
                ICraftingTweaker tweaker = this.craftingTweakers.get(recipe.getClass().getName());

                if (tweaker != null)
                {
                    try
                    {
                        tweaker.transform(unifier, recipe);
                    }
                    catch (Exception ex)
                    {
                        this.logger.error("Crafting tweaker " + tweaker.getName() + " threw an exception while transforming:", ex);
                    }
                }
                else
                {
                    unknownRecipeClasses.add(recipe.getClass().getName());
                }
            }
        }
        catch (Exception ex)
        {
            this.logger.error("Error while setting up crafting tweakers:", ex);
        }

        unknownRecipeClasses.forEach(System.out::println);
    }
}
