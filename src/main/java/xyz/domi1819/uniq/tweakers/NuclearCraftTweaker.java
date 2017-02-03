package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import xyz.domi1819.uniq.ITweaker;
import xyz.domi1819.uniq.ResourceUnifier;

import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NuclearCraftTweaker implements ITweaker
{
    @Override
    public String getName()
    {
        return "NuclearCraft";
    }

    @Override
    public String getModId()
    {
        return "NuclearCraft";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Field fRecipeList = Class.forName("nc.crafting.NCRecipeHelper").getDeclaredField("recipeList");

        fRecipeList.setAccessible(true);

        this.processRecipes(unifier, "nc.crafting.machine.CrusherRecipes", fRecipeList);
        this.processRecipes(unifier, "nc.crafting.machine.HastenerRecipes", fRecipeList);
        this.processRecipes(unifier, "nc.crafting.machine.FactoryRecipes", fRecipeList);

        this.processOldCrusherRecipes(unifier);
    }

    private void processRecipes(ResourceUnifier unifier, String className, Field fRecipeList) throws Exception
    {
        Field fRecipes = Class.forName(className).getDeclaredField("recipes");

        fRecipes.setAccessible(true);

        ((Map<Object[], Object[]>) fRecipeList.get(fRecipes.get(null))).values().forEach(unifier::setPreferredStacksObj);
    }

    private void processOldCrusherRecipes(ResourceUnifier unifier) throws Exception
    {
        Class cCrusherRecipes = Class.forName("nc.crafting.machine.CrusherRecipesOld");

        Field fSmeltingBase = cCrusherRecipes.getDeclaredField("smeltingBase");
        Field fSmeltingList = cCrusherRecipes.getDeclaredField("smeltingList");

        fSmeltingBase.setAccessible(true);
        fSmeltingList.setAccessible(true);

        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>)fSmeltingList.get(fSmeltingBase.get(null));

        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }
    }
}
