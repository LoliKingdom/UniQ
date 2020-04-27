package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.UniQ;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;

public class NuclearCraftTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "NuclearCraft";
    }

    @Override
    public String getModId()
    {
        return "nuclearcraft";
    }

    @Override
    public void run(ResourceUnifier unifier) throws ReflectiveOperationException
    {
        ReflectionData r = new ReflectionData();

        processMachineRecipes(unifier, r, "manufactory");
        processMachineRecipes(unifier, r, "decay_hastener");
        processMachineRecipes(unifier, r, "alloy_furnace");
        processMachineRecipes(unifier, r, "infuser");
        processMachineRecipes(unifier, r, "ingot_former");
        processMachineRecipes(unifier, r, "pressurizer");
        processMachineRecipes(unifier, r, "crystallizer");
        processMachineRecipes(unifier, r, "rock_crusher");
        processMachineRecipes(unifier, r, "decay_generator");
    }

    @SuppressWarnings("unchecked")
    private void processMachineRecipes(ResourceUnifier unifier, ReflectionData r, String machine) throws ReflectiveOperationException
    {
        for (Object recipe : (List<?>) r.fRecipeList.get(r.cRecipes.getDeclaredField(machine).get(null)))
        {
            List<?> products = (List<?>) r.fItemProducts.get(recipe);
            ListIterator it = products.listIterator();

            while (it.hasNext())
            {
                Object replacement = this.handleProduct(unifier, r, it.next());

                if (replacement != null)
                {
                    it.set(replacement);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object handleProduct(ResourceUnifier unifier, ReflectionData r, Object product) throws ReflectiveOperationException
    {
        Class productClass = product.getClass();

        if (productClass.equals(r.cOreIngredient))
        {
            ItemStack preferredStack = unifier.getPreferredStack((String) r.fOreName.get(product));

            if (!preferredStack.isEmpty())
            {
                preferredStack.setCount((Integer) r.fStackSize.get(product));

                return r.cItemIngredient.getConstructor(ItemStack.class).newInstance(preferredStack);
            }
        }
        else if (productClass.equals(r.cItemIngredient))
        {
            unifier.setPreferredStack(r.fStack, product);
        }
        else if (productClass.equals(r.cChanceItemIngredient))
        {
            Object innerReplacement = this.handleProduct(unifier, r, r.fIngredient.get(product));

            if (innerReplacement != null)
            {
                r.fIngredient.set(product, innerReplacement);
            }
        }
        else
        {
            UniQ.LOGGER.error("NuclearCraft Tweaker: Unknown product class " + productClass.getName());
        }

        return null;
    }

    private static class ReflectionData
    {
        // nc.recipe.NCRecipes
        Class cRecipes;

        // nc.recipe.AbstractRecipeHandler
        Field fRecipeList;

        // nc.recipe.ProcessorRecipe
        Field fItemProducts;

        // nc.recipe.ingredient.OreIngredient
        Class cOreIngredient;
        Field fOreName;
        Field fStackSize;

        // nc.recipe.ingredient.ItemIngredient
        Class cItemIngredient;
        Field fStack;

        // nc.recipe.ingredient.ChanceItemIngredient
        Class cChanceItemIngredient;
        Field fIngredient;

        Class cItemArrayIngredient;
        Field fIngredientList;

        ReflectionData() throws ReflectiveOperationException
        {
            this.cRecipes = Class.forName("nc.recipe.NCRecipes");

            this.fRecipeList = Class.forName("nc.recipe.AbstractRecipeHandler").getDeclaredField("recipeList");
            this.fRecipeList.setAccessible(true);

            this.fItemProducts = Class.forName("nc.recipe.ProcessorRecipe").getDeclaredField("itemProducts");
            this.fItemProducts.setAccessible(true);

            this.cOreIngredient = Class.forName("nc.recipe.ingredient.OreIngredient");
            this.fOreName = this.cOreIngredient.getDeclaredField("oreName");
            this.fStackSize = this.cOreIngredient.getDeclaredField("stackSize");

            this.cItemIngredient = Class.forName("nc.recipe.ingredient.ItemIngredient");
            this.fStack = this.cItemIngredient.getDeclaredField("stack");

            this.cChanceItemIngredient = Class.forName("nc.recipe.ingredient.ChanceItemIngredient");
            this.fIngredient = this.cChanceItemIngredient.getDeclaredField("ingredient");

            this.cItemArrayIngredient = Class.forName("nc.recipe.ingredient.ItemArrayIngredient");
            this.fIngredientList = this.cItemArrayIngredient.getDeclaredField("ingredientList");
        }
    }
}
