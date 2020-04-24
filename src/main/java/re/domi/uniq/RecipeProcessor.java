package re.domi.uniq;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.tweaker.IRecipeTweaker;

import java.util.*;

public class RecipeProcessor
{
    private HashMap<String, IRecipeTweaker> recipeTweakers = new HashMap<>();

    @SuppressWarnings("WeakerAccess")
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
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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
    }
}
