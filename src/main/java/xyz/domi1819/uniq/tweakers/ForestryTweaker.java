package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ForestryTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Forestry";
    }

    @Override
    public String getModId()
    {
        return "Forestry";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        this.processCarpenterRecipes(unifier);
        this.processCentrifugeRecipes(unifier);
        this.processBeeProducts(unifier);
    }

    private void processCarpenterRecipes(ResourceUnifier unifier) throws Exception
    {
        Field fRecipes = Class.forName("forestry.factory.recipes.CarpenterRecipeManager").getDeclaredField("recipes");
        Field fInternal = Class.forName("forestry.factory.recipes.CarpenterRecipe").getDeclaredField("internal");
        Field fOutput = Class.forName("forestry.core.recipes.ShapedRecipeCustom").getDeclaredField("output");
        Field fOutput2 = Class.forName("net.minecraftforge.oredict.ShapedOreRecipe").getDeclaredField("output");

        fRecipes.setAccessible(true);
        fInternal.setAccessible(true);
        fOutput.setAccessible(true);
        fOutput2.setAccessible(true);

        Set recipes = (Set) fRecipes.get(null);

        for (Object recipe : recipes)
        {
            unifier.setPreferredStack(fOutput, fInternal.get(recipe));
            unifier.setPreferredStack(fOutput2, fInternal.get(recipe));
        }
    }

    @SuppressWarnings("unchecked")
    private void processCentrifugeRecipes(ResourceUnifier unifier) throws Exception
    {
        Field fRecipes = Class.forName("forestry.factory.recipes.CentrifugeRecipeManager").getDeclaredField("recipes");
        Field fOutputs = Class.forName("forestry.factory.recipes.CentrifugeRecipe").getDeclaredField("outputs");

        fRecipes.setAccessible(true);
        fOutputs.setAccessible(true);

        Set recipes = (Set) fRecipes.get(null);

        for(Object recipe: recipes)
        {
            this.processProductMap(unifier, (Map<ItemStack, Float>) fOutputs.get(recipe));
        }
    }

    @SuppressWarnings("unchecked")
    private void processBeeProducts(ResourceUnifier unifier) throws Exception
    {
        Class cBeeSpecies = Class.forName("forestry.api.apiculture.IAlleleBeeSpecies");

        Method mGetProductChances = cBeeSpecies.getDeclaredMethod("getProductChances");
        Method mGetSpecialtyChances = cBeeSpecies.getDeclaredMethod("getSpecialtyChances");

        Method mGetRegisteredAlleles = Class.forName("forestry.api.genetics.IAlleleRegistry").getDeclaredMethod("getRegisteredAlleles");

        Field fAlleleRegistry = Class.forName("forestry.api.genetics.AlleleManager").getDeclaredField("alleleRegistry");

        Map<?, ?> genomes = (Map<?, ?>) mGetRegisteredAlleles.invoke(fAlleleRegistry.get(null));

        for (Map.Entry<?, ?> entry : genomes.entrySet())
        {
            Object allele = entry.getValue();

            if (cBeeSpecies.isInstance(allele))
            {
                this.processProductMap(unifier, (Map<ItemStack, Float>) mGetProductChances.invoke(allele));
                this.processProductMap(unifier, (Map<ItemStack, Float>) mGetSpecialtyChances.invoke(allele));
            }
        }
    }

    private void processProductMap(ResourceUnifier unifier, Map<ItemStack, Float> products)
    {
        for (Map.Entry<ItemStack, Float> product : new HashMap<>(products).entrySet())
        {
            ItemStack output = product.getKey();
            ItemStack replacement = unifier.getPreferredStack(output);

            if (!output.isItemEqual(replacement))
            {
                products.remove(output);
                products.put(replacement, product.getValue());
            }
        }
    }
}
