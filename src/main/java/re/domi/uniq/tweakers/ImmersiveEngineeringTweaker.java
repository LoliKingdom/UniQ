package re.domi.uniq.tweakers;

import com.google.common.collect.Multimap;
import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class ImmersiveEngineeringTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Immersive Engineering";
    }

    @Override
    public String getModId()
    {
        return "immersiveengineering";
    }

    @Override
    public void run(ResourceUnifier unifier) throws ReflectiveOperationException
    {
        Class cMultiblockRecipe = Class.forName("blusunrize.immersiveengineering.api.crafting.MultiblockRecipe");
        Field fOutputList = cMultiblockRecipe.getDeclaredField("outputList");
        fOutputList.setAccessible(true);

        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.AlloyRecipe", cMultiblockRecipe, fOutputList, true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe", cMultiblockRecipe, fOutputList, true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe", cMultiblockRecipe, fOutputList, true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe", cMultiblockRecipe, fOutputList, true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe", cMultiblockRecipe, fOutputList, true, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.CrusherRecipe", cMultiblockRecipe, fOutputList, true, true);

        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe", cMultiblockRecipe, fOutputList, false, false);
        this.processRecipes(unifier, "blusunrize.immersiveengineering.api.crafting.MetalPressRecipe", cMultiblockRecipe, fOutputList, false, false);
    }

    @SuppressWarnings("unchecked")
    private void processRecipes(ResourceUnifier unifier, String className, Class cMultiblockRecipe, Field fOutputList, boolean listType, boolean multiOutput) throws ReflectiveOperationException
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

        for (Object recipe : listType ? (Iterable<?>) fRecipeList.get(null) : ((Multimap<?, ?>) fRecipeList.get(null)).values())
        {
            unifier.setPreferredStack(fOutput, recipe);

            if (multiOutput)
            {
                unifier.setPreferredStacks((ItemStack[]) fSecondaryOutput.get(recipe));
            }

            if (cMultiblockRecipe.isInstance(recipe))
            {
                unifier.setPreferredStacks((List<ItemStack>) fOutputList.get(recipe));
            }
        }
    }
}
