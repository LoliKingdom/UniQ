package re.domi.uniq.tweakers;

import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class AppliedEnergisticsTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Applied Energistics 2";
    }

    @Override
    public String getModId()
    {
        return "appliedenergistics2";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cApi = Class.forName("appeng.core.Api");
        Class cGrinderRecipe = Class.forName("appeng.core.features.registries.entries.AppEngGrinderRecipe");

        Field fInstance = cApi.getDeclaredField("INSTANCE");
        Field fRegistryContainer = cApi.getDeclaredField("registryContainer");
        Field fGrinder = Class.forName("appeng.core.features.registries.RegistryContainer").getDeclaredField("grinder");
        Field fRecipes = Class.forName("appeng.core.features.registries.GrinderRecipeManager").getDeclaredField("recipes");
        Field fOut = cGrinderRecipe.getDeclaredField("out");
        Field fOut2 = cGrinderRecipe.getDeclaredField("optionalOutput");
        Field fOut3 = cGrinderRecipe.getDeclaredField("optionalOutput2");

        fRegistryContainer.setAccessible(true);
        fGrinder.setAccessible(true);
        fRecipes.setAccessible(true);
        fOut.setAccessible(true);
        fOut2.setAccessible(true);
        fOut3.setAccessible(true);

        for (Object recipe : (List) fRecipes.get(fGrinder.get(fRegistryContainer.get(fInstance.get(null)))))
        {
            unifier.setPreferredStack(fOut, recipe);
            unifier.setPreferredStack(fOut2, recipe);
            unifier.setPreferredStack(fOut3, recipe);
        }
    }
}
