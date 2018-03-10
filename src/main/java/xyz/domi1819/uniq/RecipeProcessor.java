package xyz.domi1819.uniq;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.tweaker.IRecipeTweaker;

import java.util.*;

public class RecipeProcessor
{
    private HashMap<String, IRecipeTweaker> recipeTweakers = new HashMap<>();

    public void registerRecipeTweaker(IRecipeTweaker tweaker, String recipeClassName)
    {
        String modId = tweaker.getModId();

        if (modId.equals("") || Loader.isModLoaded(modId))
        {
            this.recipeTweakers.put(recipeClassName, tweaker);
        }
    }

    public void setup()
    {
        Iterator<Map.Entry<String, IRecipeTweaker>> iterator = this.recipeTweakers.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, IRecipeTweaker> entry = iterator.next();

            try
            {
                entry.getValue().prepareTransform(entry.getKey());
            }
            catch (Exception ex)
            {
                UniQ.instance.logger.error("Crafting tweaker " + entry.getValue().getName() + " threw an exception while preparing and was disabled:", ex);
                iterator.remove();
            }
        }
    }

    public void transform(ResourceUnifier unifier, List<IRecipe> recipes)
    {
        Set<String> unknownRecipeClasses = new HashSet<>();

        for (IRecipe recipe : recipes)
        {
            IRecipeTweaker tweaker = this.recipeTweakers.get(recipe.getClass().getName());

            if (tweaker != null)
            {
                try
                {
                    tweaker.transform(unifier, recipe);
                }
                catch (Exception ex)
                {
                    UniQ.instance.logger.error("Crafting tweaker " + tweaker.getName() + " threw an exception while transforming:", ex);
                }
            }
            else
            {
                unknownRecipeClasses.add(recipe.getClass().getName());
            }
        }

        if (UniQ.instance.config.debug)
        {
            System.out.println("Unknown recipe classes:");
            unknownRecipeClasses.forEach(System.out::println);
        }
    }
}
