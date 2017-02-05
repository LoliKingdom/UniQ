package xyz.domi1819.uniq.tweakers;

import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.Set;

public class ForestryTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Forestry";
    }

    @Override
    public String getModId()
    {
        return "Forestry";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Field fRecipes = Class.forName("forestry.factory.recipes.CarpenterRecipeManager").getDeclaredField("recipes");
        Field fInternal = Class.forName("forestry.factory.recipes.CarpenterRecipe").getDeclaredField("internal");
        Field fOutput = Class.forName("forestry.core.recipes.ShapedRecipeCustom").getDeclaredField("output");
        Field fOutput2 = Class.forName("net.minecraftforge.oredict.ShapedOreRecipe").getDeclaredField("output");

        fRecipes.setAccessible(true);
        fInternal.setAccessible(true);
        fOutput.setAccessible(true);
        fOutput2.setAccessible(true);

        Set recipes = (Set) fRecipes.get(null);

        for (Object recipe : recipes)
        {
            unifier.setPreferredStack(fOutput, fInternal.get(recipe));
            unifier.setPreferredStack(fOutput2, fInternal.get(recipe));
        }
    }
}
