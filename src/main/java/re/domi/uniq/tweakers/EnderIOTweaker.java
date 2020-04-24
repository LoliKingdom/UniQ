package re.domi.uniq.tweakers;

import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class EnderIOTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Ender IO";
    }

    @Override
    public String getModId()
    {
        return "EnderIO";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Field fOutputs = Class.forName("crazypants.enderio.machine.recipe.Recipe").getDeclaredField("outputs");
        Field fOutput = Class.forName("crazypants.enderio.machine.recipe.RecipeOutput").getDeclaredField("output");

        fOutputs.setAccessible(true);
        fOutput.setAccessible(true);

        this.clearOreDictionaryPreferences();
        this.processSagMillRecipes(unifier, fOutputs, fOutput);
        this.processAlloySmelterRecipes(unifier, fOutputs, fOutput);
    }

    private void processSagMillRecipes(ResourceUnifier unifier, Field fOutputs, Field fOutput) throws Exception
    {
        Class cCrusherRecipeManager = Class.forName("crazypants.enderio.machine.crusher.CrusherRecipeManager");

        Field fInstance = cCrusherRecipeManager.getDeclaredField("instance");
        Field fRecipes = cCrusherRecipeManager.getDeclaredField("recipes");

        fInstance.setAccessible(true);
        fRecipes.setAccessible(true);

        List recipes = (List) fRecipes.get(fInstance.get(null));

        for (Object recipe : recipes)
        {
            this.processRecipeObject(unifier, recipe, fOutputs, fOutput);
        }
    }

    private void processAlloySmelterRecipes(ResourceUnifier unifier, Field fOutputs, Field fOutput) throws Exception
    {
        Class cAlloyRecipeManager = Class.forName("crazypants.enderio.machine.alloy.AlloyRecipeManager");
        Class cBasicManyToOneRecipe = Class.forName("crazypants.enderio.machine.recipe.BasicManyToOneRecipe");

        Field fInstance = cAlloyRecipeManager.getDeclaredField("instance");
        Field fRecipes = Class.forName("crazypants.enderio.machine.recipe.ManyToOneRecipeManager").getDeclaredField("recipes");
        Field fWrapperOutput = cBasicManyToOneRecipe.getDeclaredField("output");
        Field fRecipe = cBasicManyToOneRecipe.getDeclaredField("recipe");

        fInstance.setAccessible(true);
        fRecipes.setAccessible(true);
        fWrapperOutput.setAccessible(true);
        fRecipe.setAccessible(true);

        List recipes = (List) fRecipes.get(fInstance.get(null));

        for (Object wrappedRecipe : recipes)
        {
            unifier.setPreferredStack(fWrapperOutput, wrappedRecipe);

            this.processRecipeObject(unifier, fRecipe.get(wrappedRecipe), fOutputs, fOutput);
        }
    }

    private void processRecipeObject(ResourceUnifier unifier, Object recipe, Field fOutputs, Field fOutput) throws Exception
    {
        for (Object recipeOutput : (Object[]) fOutputs.get(recipe))
        {
            unifier.setPreferredStack(fOutput, recipeOutput);
        }
    }

    private void clearOreDictionaryPreferences() throws Exception
    {
        Class cOreDictionaryPreferences = Class.forName("crazypants.enderio.material.OreDictionaryPreferences");

        Field fInstance = cOreDictionaryPreferences.getDeclaredField("instance");
        Field fPreferences = cOreDictionaryPreferences.getDeclaredField("preferences");
        Field fStackCache = cOreDictionaryPreferences.getDeclaredField("stackCache");

        fInstance.setAccessible(true);
        fPreferences.setAccessible(true);
        fStackCache.setAccessible(true);

        ((Map) fPreferences.get(fInstance.get(null))).clear();
        ((Map) fStackCache.get(fInstance.get(null))).clear();
    }
}
