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
        this.processCrusherRecipes(unifier);
        this.processOldCrusherRecipes(unifier);
    }

    private void processCrusherRecipes(ResourceUnifier unifier) throws Exception
    {
        Field inst = Class.forName("nc.crafting.machine.CrusherRecipes").getDeclaredField("recipes");
        Field recipes = Class.forName("nc.crafting.NCRecipeHelper").getDeclaredField("recipeList");

        inst.setAccessible(true);
        recipes.setAccessible(true);

        ((Map<Object[], Object[]>) recipes.get(inst.get(null))).values().forEach(unifier::setPreferredStacksObj);
    }

    private void processOldCrusherRecipes(ResourceUnifier unifier) throws Exception
    {
        Class clazz = Class.forName("nc.crafting.machine.CrusherRecipesOld");

        Field inst = clazz.getDeclaredField("smeltingBase");
        Field fRecipes = clazz.getDeclaredField("smeltingList");

        inst.setAccessible(true);
        fRecipes.setAccessible(true);

        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>)fRecipes.get(inst.get(null));

        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }
    }
}
