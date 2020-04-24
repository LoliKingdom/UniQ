package re.domi.uniq.tweakers;

import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class ThaumcraftTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Thaumcraft";
    }

    @Override
    public String getModId()
    {
        return "Thaumcraft";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cCrucibleRecipe = Class.forName("thaumcraft.api.crafting.CrucibleRecipe");

        Field fCraftingRecipes = Class.forName("thaumcraft.api.ThaumcraftApi").getDeclaredField("craftingRecipes");
        Field fRecipeOutput = cCrucibleRecipe.getDeclaredField("recipeOutput");

        fCraftingRecipes.setAccessible(true);
        fRecipeOutput.setAccessible(true);

        List recipes = (List) fCraftingRecipes.get(null);

        for (Object recipe : recipes)
        {
            if (cCrucibleRecipe.isInstance(recipe))
            {
                unifier.setPreferredStack(fRecipeOutput, recipe);
            }
        }
    }
}
