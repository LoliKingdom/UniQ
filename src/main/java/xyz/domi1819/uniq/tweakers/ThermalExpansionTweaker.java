package xyz.domi1819.uniq.tweakers;

import xyz.domi1819.uniq.Reflect;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;
import xyz.domi1819.uniq.ResourceUnifier;

import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class ThermalExpansionTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Thermal Expansion";
    }

    @Override
    public String getModId()
    {
        return "ThermalExpansion";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        this.processSingleOutputRecipes(unifier, "cofh.thermalexpansion.util.crafting.FurnaceManager", "RecipeFurnace");
        this.processDualOutputRecipes(unifier, "cofh.thermalexpansion.util.crafting.PulverizerManager", "RecipePulverizer");
        this.processDualOutputRecipes(unifier, "cofh.thermalexpansion.util.crafting.SmelterManager", "RecipeSmelter");
        this.processDualOutputRecipes(unifier, "cofh.thermalexpansion.util.crafting.SawmillManager", "RecipeSawmill");
    }

    @SuppressWarnings("SameParameterValue")
    private void processSingleOutputRecipes(ResourceUnifier unifier, String baseName, String nestedName) throws Exception
    {
        Class cBase = Class.forName(baseName);

        Field fRecipeMap = cBase.getDeclaredField("recipeMap");
        Field fOutput = Reflect.getNestedClass(cBase, nestedName).getDeclaredField("output");

        fRecipeMap.setAccessible(true);
        fOutput.setAccessible(true);

        for (Map.Entry entry : ((Map<?, ?>) fRecipeMap.get(null)).entrySet())
        {
            unifier.setPreferredStack(fOutput, entry.getValue());
        }
    }

    private void processDualOutputRecipes(ResourceUnifier unifier, String baseName, String nestedName) throws Exception
    {
        Class cBase = Class.forName(baseName);
        Class cNested = Reflect.getNestedClass(cBase, nestedName);

        Field fRecipeMap = cBase.getDeclaredField("recipeMap");
        Field fOutputPrimary = cNested.getDeclaredField("primaryOutput");
        Field fOutputSecondary = cNested.getDeclaredField("secondaryOutput");

        fRecipeMap.setAccessible(true);
        fOutputPrimary.setAccessible(true);
        fOutputSecondary.setAccessible(true);

        for (Map.Entry entry : ((Map<?, ?>) fRecipeMap.get(null)).entrySet())
        {
            unifier.setPreferredStack(fOutputPrimary, entry.getValue());
            unifier.setPreferredStack(fOutputSecondary, entry.getValue());
        }
    }
}
