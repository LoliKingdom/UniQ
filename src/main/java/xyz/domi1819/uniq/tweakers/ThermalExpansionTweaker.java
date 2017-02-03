package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import xyz.domi1819.uniq.ITweaker;
import xyz.domi1819.uniq.ResourceUnifier;

import java.lang.reflect.Field;
import java.util.Map;

public class ThermalExpansionTweaker implements ITweaker
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
    }

    private void processSingleOutputRecipes(ResourceUnifier unifier, String baseName, String nestedName) throws Exception
    {
        Class cBase = Class.forName(baseName);

        Field fRecipeMap = cBase.getDeclaredField("recipeMap");
        Field fOutput = this.getNestedClass(cBase, nestedName).getDeclaredField("output");

        fRecipeMap.setAccessible(true);
        fOutput.setAccessible(true);

        Map<?,?> recipeMap = (Map<?,?>)fRecipeMap.get(null);

        ItemStack output;
        ItemStack replacement;

        for (Map.Entry entry : recipeMap.entrySet())
        {
            output = (ItemStack)fOutput.get(entry.getValue());
            replacement = unifier.getPreferredStack(output);

            if (!output.isItemEqual(replacement))
            {
                fOutput.set(entry.getValue(), replacement);
            }
        }
    }

    private void processDualOutputRecipes(ResourceUnifier unifier, String baseName, String nestedName) throws Exception
    {
        Class cBase = Class.forName(baseName);
        Class cNested = this.getNestedClass(cBase, nestedName);

        Field fRecipeMap = cBase.getDeclaredField("recipeMap");
        Field fOutputPrimary = cNested.getDeclaredField("primaryOutput");
        Field fOutputSecondary = cNested.getDeclaredField("secondaryOutput");

        fRecipeMap.setAccessible(true);
        fOutputPrimary.setAccessible(true);
        fOutputSecondary.setAccessible(true);

        Map<?,?> recipeMap = (Map<?,?>)fRecipeMap.get(null);

        ItemStack output;
        ItemStack replacement;

        for (Map.Entry entry : recipeMap.entrySet())
        {
            output = (ItemStack)fOutputPrimary.get(entry.getValue());
            replacement = unifier.getPreferredStack(output);

            if (!output.isItemEqual(replacement))
            {
                fOutputPrimary.set(entry.getValue(), replacement);
            }

            output = (ItemStack)fOutputSecondary.get(entry.getValue());
            replacement = unifier.getPreferredStack(output);

            if (output != null && !output.isItemEqual(replacement))
            {
                fOutputSecondary.set(entry.getValue(), replacement);
            }
        }
    }

    private Class getNestedClass(Class mainClass, String simpleName)
    {
        Class[] classes = mainClass.getDeclaredClasses();

        for (Class clazz : classes)
        {
            if (clazz.getSimpleName().equals(simpleName))
            {
                return clazz;
            }
        }

        return null;
    }
}
