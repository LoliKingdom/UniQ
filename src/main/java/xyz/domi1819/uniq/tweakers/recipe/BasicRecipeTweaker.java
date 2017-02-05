package xyz.domi1819.uniq.tweakers.recipe;

import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IRecipeTweaker;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class BasicRecipeTweaker implements IRecipeTweaker
{
    private Field fRecipeOutput;

    private String modId = "";
    private String fieldName = "output";

    public BasicRecipeTweaker()
    {
    }

    public BasicRecipeTweaker(String modId)
    {
        this.modId = modId;
    }

    public BasicRecipeTweaker(String modId, String fieldName)
    {
        this.modId = modId;
        this.fieldName = fieldName;
    }

    @Override
    public String getName()
    {
        return "Basic crafting [" + this.modId + "]";
    }

    @Override
    public String getModId()
    {
        return this.modId;
    }

    @Override
    public void prepareTransform(String className) throws Exception
    {
        this.fRecipeOutput = Class.forName(className).getDeclaredField(this.fieldName);
        this.fRecipeOutput.setAccessible(true);
    }

    @Override
    public void transform(ResourceUnifier unifier, IRecipe recipe) throws Exception
    {
        unifier.setPreferredStack(this.fRecipeOutput, recipe);
    }
}
