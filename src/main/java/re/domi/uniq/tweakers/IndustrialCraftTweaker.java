package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
        return "IC2";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cRecipes = Class.forName("ic2.api.recipe.Recipes");

        Method mGetRecipes = Class.forName("ic2.api.recipe.IMachineRecipeManager").getDeclaredMethod("getRecipes");
        Field fItems = Class.forName("ic2.api.recipe.RecipeOutput").getDeclaredField("items");

        for (String machine : new String[] {"macerator", "extractor", "compressor", "centrifuge", "blockcutter", "blastfurance", "metalformerExtruding", "metalformerCutting", "metalformerRolling", "oreWashing"})
        {
            this.processMachineRecipeManager(unifier, machine, cRecipes, mGetRecipes, fItems);
        }

        this.processScrapBoxDrops(unifier, cRecipes);
    }

    @SuppressWarnings("unchecked")
    private void processMachineRecipeManager(ResourceUnifier unifier, String machineName, Class cRecipes, Method mGetRecipes, Field fItems) throws Exception
    {
        Map map = (Map) mGetRecipes.invoke(cRecipes.getDeclaredField(machineName).get(null));

        for (Object recipeOutput : map.values())
        {
            unifier.setPreferredStacks((List<ItemStack>) fItems.get(recipeOutput));
        }
    }

    private void processScrapBoxDrops(ResourceUnifier unifier, Class cRecipes) throws Exception
    {
        Field fScrapboxDrops = cRecipes.getDeclaredField("scrapboxDrops");
        Field fDrops = Class.forName("ic2.core.item.ItemScrapbox$ScrapboxRecipeManager").getDeclaredField("drops");
        Field fItem = Class.forName("ic2.core.item.ItemScrapbox$Drop").getDeclaredField("item");

        fDrops.setAccessible(true);
        fItem.setAccessible(true);

        List drops = (List) fDrops.get(fScrapboxDrops.get(fScrapboxDrops.get(null)));

        for (Object drop : drops)
        {
            unifier.setPreferredStack(fItem, drop);
        }
    }
}
