package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;
import xyz.domi1819.uniq.ResourceUnifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
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
    }

    private void processMachineRecipeManager(ResourceUnifier unifier, String machineName, Class cRecipes, Method mGetRecipes, Field fItems) throws Exception
    {
        Map map = (Map)mGetRecipes.invoke(cRecipes.getDeclaredField(machineName).get(null));

        for (Object recipeOutput : map.values())
        {
            unifier.setPreferredStacks((List<ItemStack>)fItems.get(recipeOutput));
        }
    }
}
