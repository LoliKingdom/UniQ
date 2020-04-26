package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ThermalExpansionTweaker implements IGeneralTweaker
{
    private static final String PACKAGE = "cofh.thermalexpansion.util.managers.";

    @Override
    public String getName()
    {
        return "Thermal Expansion";
    }

    @Override
    public String getModId()
    {
        return "thermalexpansion";
    }

    @Override
    public void run(ResourceUnifier unifier) throws ReflectiveOperationException
    {
        this.processMultiOutputRecipes(unifier, "Centrifuge", "recipeMap", "recipeMapMobs");
        this.processSingleOutputRecipes(unifier, "machine", "Charger", "output", "recipeMap");
        this.processSingleOutputRecipes(unifier, "machine", "Compactor", "output", "recipeMapAll", "recipeMapPlate", "recipeMapCoin", "recipeMapGear");
        this.processSingleOutputRecipes(unifier, "machine", "Enchanter", "output", "recipeMap");
        this.processSingleOutputRecipes(unifier, "machine", "Extruder", "output", "recipeMapIgneous", "recipeMapSedimentary");
        this.processSingleOutputRecipes(unifier, "machine", "Furnace", "output", "recipeMap", "recipeMapPyrolysis");
        this.processDualOutputRecipes(unifier, "Insolator");
        this.processSingleOutputRecipes(unifier, "machine", "Precipitator", "output", "recipeMap");
        this.processDualOutputRecipes(unifier, "Pulverizer");
        this.processSingleOutputRecipes(unifier, "machine", "Refinery", "outputItem", "recipeMap");
        this.processDualOutputRecipes(unifier, "Sawmill");
        this.processDualOutputRecipes(unifier, "Smelter");
        this.processSingleOutputRecipes(unifier, "machine", "Transposer", "output", "recipeMapFill", "recipeMapExtract");

        this.processSingleOutputRecipes(unifier, "device", "Factorizer", "output", "recipeMap", "recipeMapReverse");
    }

    private void processSingleOutputRecipes(ResourceUnifier unifier, String type, String machine, String outputName, String... recipeMapNames) throws ReflectiveOperationException
    {
        String baseName = PACKAGE + type + "." + machine + "Manager";
        Field fOutput = Class.forName(baseName + "$" + machine + "Recipe").getDeclaredField(outputName);

        fOutput.setAccessible(true);

        for (String recipeMapName : recipeMapNames)
        {
            Field fRecipeMap = Class.forName(baseName).getDeclaredField(recipeMapName);
            fRecipeMap.setAccessible(true);

            for (Map.Entry entry : ((Map<?, ?>) fRecipeMap.get(null)).entrySet())
            {
                unifier.setPreferredStack(fOutput, entry.getValue());
            }
        }
    }

    private void processDualOutputRecipes(ResourceUnifier unifier, String machine) throws ReflectiveOperationException
    {
        String baseName = PACKAGE + "machine." + machine + "Manager";
        Class cNested = Class.forName(baseName + "$" + machine + "Recipe");

        Field fRecipeMap = Class.forName(baseName).getDeclaredField("recipeMap");
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

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private void processMultiOutputRecipes(ResourceUnifier unifier, String machine, String... recipeMapNames) throws ReflectiveOperationException
    {
        String baseName = PACKAGE + "machine." + machine + "Manager";
        Field fOutput = Class.forName(baseName + "$" + machine + "Recipe").getDeclaredField("output");
        fOutput.setAccessible(true);

        for (String recipeMapName : recipeMapNames)
        {
            Field fRecipeMap = Class.forName(baseName).getDeclaredField(recipeMapName);

            fRecipeMap.setAccessible(true);

            for (Map.Entry entry : ((Map<?, ?>) fRecipeMap.get(null)).entrySet())
            {
                unifier.setPreferredStacks((List<ItemStack>) fOutput.get(entry.getValue()));
            }
        }
    }
}
