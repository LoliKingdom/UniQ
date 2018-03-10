package xyz.domi1819.uniq.tweakers;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.UniQ;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RailcraftTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Railcraft";
    }

    @Override
    public String getModId()
    {
        return "Railcraft";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cCraftingManager = Class.forName("mods.railcraft.api.crafting.RailcraftCraftingManager");

        this.processRecipes(unifier, "blastFurnace", cCraftingManager, "mods.railcraft.common.util.crafting.BlastFurnaceCraftingManager", "$BlastFurnaceRecipe", true);
        this.processRecipes(unifier, "cokeOven", cCraftingManager, "mods.railcraft.common.util.crafting.CokeOvenCraftingManager", "$CokeOvenRecipe", true);

        this.processRecipes(unifier, "rockCrusher", cCraftingManager, "mods.railcraft.common.util.crafting.RockCrusherCraftingManager", "$CrusherRecipe", false);

        this.processRollingMachineRecipes(unifier, cCraftingManager);
    }

    private void processRecipes(ResourceUnifier unifier, String machineName, Class craftingManager, String className, String recipeClassName, boolean singleOutput) throws Exception
    {
        Class cBase = Class.forName(className);

        Field fInstance = craftingManager.getDeclaredField(machineName);
        Field fRecipes = cBase.getDeclaredField("recipes");

        fRecipes.setAccessible(true);

        Object instance = fInstance.get(null);

        if (singleOutput)
        {
            Field fOutput = Class.forName(className + recipeClassName).getDeclaredField("output");
            fOutput.setAccessible(true);

            for (Object recipe : (List) fRecipes.get(instance))
            {
                unifier.setPreferredStack(fOutput, recipe);
            }
        }
        else
        {
            Field fOutputs = Class.forName(className + recipeClassName).getDeclaredField("outputs");
            fOutputs.setAccessible(true);

            ItemStack output;
            ItemStack replacement;

            for (Object recipe : (List) fRecipes.get(instance))
            {
                List<Map.Entry<ItemStack, Float>> outputs = (List<Map.Entry<ItemStack, Float>>) fOutputs.get(recipe);

                for (int i = 0; i < outputs.size(); i++)
                {
                    Map.Entry<ItemStack, Float> entry = outputs.get(i);

                    output = entry.getKey();
                    replacement = unifier.getPreferredStack(output);

                    if (!output.isItemEqual(replacement))
                    {
                        outputs.set(i, Maps.immutableEntry(replacement, entry.getValue()));
                    }
                }
            }
        }
    }

    private void processRollingMachineRecipes(ResourceUnifier unifier, Class craftingManager) throws Exception
    {
        Field fRecipes = Class.forName("mods.railcraft.common.util.crafting.RollingMachineCraftingManager").getDeclaredField("recipes");

        fRecipes.setAccessible(true);

        UniQ.instance.recipeProcessor.transform(unifier, (List<IRecipe>) fRecipes.get(craftingManager.getDeclaredField("rollingMachine").get(null)));
    }
}
