package xyz.domi1819.uniq.tweakers;

import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class AdvancedSolarPanelsTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Advanced Solar Panels";
    }

    @Override
    public String getModId()
    {
        return "AdvancedSolarPanel";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Field fTransformerRecipes = Class.forName("advsolar.utils.MTRecipeManager").getDeclaredField("transformerRecipes");
        Field fOutputStack = Class.forName("advsolar.utils.MTRecipeRecord").getDeclaredField("outputStack");

        List recipes = (List) fTransformerRecipes.get(null);

        for (Object recipe : recipes)
        {
            unifier.setPreferredStack(fOutputStack, recipe);
        }
    }
}
