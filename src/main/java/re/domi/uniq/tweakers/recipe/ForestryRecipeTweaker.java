package re.domi.uniq.tweakers.recipe;

import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IRecipeTweaker;

import java.lang.reflect.Field;

public class ForestryRecipeTweaker implements IRecipeTweaker
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