package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class IndustrialCraftTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "IndustrialCraft 2";
    }

    @Override
    public String getModId()
    {
        return "ic2";
    }

    @Override
    public void run(ResourceUnifier unifier) throws ReflectiveOperationException
    {
        Class cRecipes = Class.forName("ic2.api.recipe.Recipes");

        Method mGetRecipes = Class.forName("ic2.api.recipe.IMachineRecipeManager").getDeclaredMethod("getRecipes");
        Field fOutput = Class.forName("ic2.api.recipe.MachineRecipe").getDeclaredField("output");

        fOutput.setAccessible(true);

        for (String machine : new String[] {"macerator", "extractor", "compressor", "centrifuge", "blockcutter", "blastfurnace", "metalformerExtruding", "metalformerCutting", "metalformerRolling", "oreWashing"})
        {
            this.processMachineRecipeManager(unifier, machine, cRecipes, mGetRecipes, fOutput);
        }

        this.processScrapBoxDrops(unifier, cRecipes);
    }

    @SuppressWarnings("unchecked")
    private void processMachineRecipeManager(ResourceUnifier unifier, String machineName, Class cRecipes, Method mGetRecipes, Field fOutput) throws ReflectiveOperationException
    {
        Iterable<?> recipes = (Iterable<?>) mGetRecipes.invoke(cRecipes.getDeclaredField(machineName).get(null));

        for (Object recipe : recipes)
        {
            unifier.setPreferredStacks((List<ItemStack>) fOutput.get(recipe));
        }
    }

    private void processScrapBoxDrops(ResourceUnifier unifier, Class cRecipes) throws ReflectiveOperationException
    {
        Field fScrapboxDrops = cRecipes.getDeclaredField("scrapboxDrops");

        Field fDrops = Class.forName("ic2.core.recipe.ScrapboxRecipeManager").getDeclaredField("drops");
        Field fItem = Class.forName("ic2.core.recipe.ScrapboxRecipeManager$Drop").getDeclaredField("item");

        fDrops.setAccessible(true);
        fItem.setAccessible(true);

        List<?> drops = (List<?>) fDrops.get(fScrapboxDrops.get(null));

        for (Object drop : drops)
        {
            unifier.setPreferredStack(fItem, drop);
        }
    }
}
