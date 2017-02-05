package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.UniQ;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class ExtraUtilitiesTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Extra Utilities";
    }

    @Override
    public String getModId()
    {
        return "ExtraUtilities";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Field fRecipes = Class.forName("com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler").getDeclaredField("recipes");

        UniQ.instance.recipeProcessor.transform(unifier, (List<IRecipe>) fRecipes.get(null));
    }
}
