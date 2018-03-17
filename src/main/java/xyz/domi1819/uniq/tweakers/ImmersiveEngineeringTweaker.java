package xyz.domi1819.uniq.tweakers;

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ImmersiveEngineeringTweaker implements IGeneralTweaker
{
    public ImmersiveEngineeringTweaker()
    {
        if (Loader.isModLoaded(getModId()))
        {
            Item metal = GameRegistry.findItem(getModId(), "metal");

            OreDictionary.registerOre("dustNetherQuartz", new ItemStack(metal, 1, 18));

            OreDictionary.registerOre("ingotAluminium", new ItemStack(metal, 1, 1));
            OreDictionary.registerOre("dustAluminium", new ItemStack(metal, 1, 11));
            OreDictionary.registerOre("nuggetAluminium", new ItemStack(metal, 1, 23));
            OreDictionary.registerOre("plateAluminium", new ItemStack(metal, 1, 32));

            OreDictionary.registerOre("stickAluminium", new ItemStack(GameRegistry.findItem(getModId(), "material"), 1, 16));
        }
    }

    @Override
    public String getName()
    {
        return "Immersive Engineering";
    }

    @Override
    public String getModId()
    {
        return "ImmersiveEngineering";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe", true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe", true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe", true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe", true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.CrusherRecipe", true, true);

        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe", false, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.MetalPressRecipe", false, false);
    }

    @SuppressWarnings("unchecked")
    private void processRecipes(ResourceUnifier unifier, String className, boolean listType, boolean multiOutput) throws Exception
    {
        Class cBase = Class.forName(className);

        Field fRecipeList = cBase.getDeclaredField("recipeList");
        Field fOutput = cBase.getDeclaredField("output");
        Field fSecondaryOutput = null;

        fOutput.setAccessible(true);

        if (multiOutput)
        {
            fSecondaryOutput = cBase.getDeclaredField("secondaryOutput");

            fSecondaryOutput.setAccessible(true);
        }

        if (listType)
        {
            List recipeList = (List) fRecipeList.get(null);

            for (Object recipe : recipeList)
            {
                unifier.setPreferredStack(fOutput, recipe);

                if (multiOutput)
                {
                    unifier.setPreferredStacks((ItemStack[]) fSecondaryOutput.get(recipe));
                }
            }
        }
        else
        {
            Multimap<?, ?> recipeList = (Multimap<?, ?>) fRecipeList.get(null);

            for (Map.Entry entry : recipeList.entries())
            {
                unifier.setPreferredStack(fOutput, entry.getValue());
            }
        }
    }
}
