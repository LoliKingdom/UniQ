package xyz.domi1819.uniq.tweakers;

import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class MekanismTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Mekanism";
    }

    @Override
    public String getModId()
    {
        return "Mekanism";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cRecipe = Class.forName("mekanism.common.recipe.RecipeHandler$Recipe");

        Method mGet = cRecipe.getDeclaredMethod("get");
        Field fRecipeOutput = Class.forName("mekanism.common.recipe.machines.MachineRecipe").getDeclaredField("recipeOutput");
        Field fOutput = Class.forName("mekanism.common.recipe.outputs.ItemStackOutput").getDeclaredField("output");

        Object[] recipes = cRecipe.getEnumConstants();

        for (Object machine : recipes)
        {
            switch (machine.toString())
            {
                case "ENERGIZED_SMELTER":
                case "ENRICHMENT_CHAMBER":
                case "OSMIUM_COMPRESSOR":
                case "COMBINER":
                case "CRUSHER":
                case "PURIFICATION_CHAMBER":
                case "METALLURGIC_INFUSER":
                case "CHEMICAL_INJECTION_CHAMBER":
                case "CHEMICAL_CRYSTALLIZER":
                {
                    this.processMachineRecipes(unifier, fRecipeOutput, fOutput, null, (Map<?, ?>) mGet.invoke(machine));

                    break;
                }
                case "PRECISION_SAWMILL":
                {
                    Class cChanceOutput = Class.forName("mekanism.common.recipe.outputs.ChanceOutput");

                    this.processMachineRecipes(unifier, fRecipeOutput, cChanceOutput.getDeclaredField("primaryOutput"), cChanceOutput.getDeclaredField("secondaryOutput"), (Map<?, ?>) mGet.invoke(machine));

                    break;
                }
            }
        }
    }

    private void processMachineRecipes(ResourceUnifier unifier, Field fRecipeOutput, Field fOutput, Field fOutput2, Map<?, ?> recipeList) throws Exception
    {
        for (Map.Entry entry : recipeList.entrySet())
        {
            Object mekOutputStack = fRecipeOutput.get(entry.getValue());

            unifier.setPreferredStack(fOutput, mekOutputStack);

            if (fOutput2 != null)
            {
                unifier.setPreferredStack(fOutput2, mekOutputStack);
            }
        }
    }
}
