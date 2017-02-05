package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.ICraftingTweaker;

import java.lang.reflect.Field;

public class ForestryCraftingTweaker implements ICraftingTweaker
{
    private Field fRecipeOutput;
    private Field fRecipeOutput2;

    @Override
    public String getName()
    {
        return "Forestry Crafting";
    }

    @Override
    public String getModId()
    {
        return "Forestry";
    }

    @Override
    public void prepareTransform(String className) throws Exception
    {
        this.fRecipeOutput = Class.forName(className).getDeclaredField("output");
        this.fRecipeOutput.setAccessible(true);

        this.fRecipeOutput2 = Class.forName("net.minecraftforge.oredict.ShapedOreRecipe").getDeclaredField("output");
        this.fRecipeOutput2.setAccessible(true);
    }

    @Override
    public void transform(ResourceUnifier unifier, IRecipe recipe) throws Exception
    {
        unifier.setPreferredStack(this.fRecipeOutput, recipe);
        unifier.setPreferredStack(this.fRecipeOutput2, recipe);
    }
}