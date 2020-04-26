package re.domi.uniq.tweakers.recipe;

import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IRecipeTweaker;

import java.lang.reflect.Field;

public class BasicRecipeTweaker implements IRecipeTweaker
{
    private Field fRecipeOutput;

    private String modId;
    private String fieldName = "output";
    private String baseClass;

    public BasicRecipeTweaker(String modId)
    {
        this.modId = modId;
    }

    @SuppressWarnings("unused")
    public BasicRecipeTweaker(String modId, String fieldName)
    {
        this.modId = modId;
        this.fieldName = fieldName;
    }

    public BasicRecipeTweaker(String modId, String fieldName, String baseClass)
    {
        this.modId = modId;
        this.fieldName = fieldName;
        this.baseClass = baseClass;
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
    public void prepareTransform(String className) throws ReflectiveOperationException
    {
        this.fRecipeOutput = Class.forName(this.baseClass == null ? className : this.baseClass).getDeclaredField(this.fieldName);
        this.fRecipeOutput.setAccessible(true);
    }

    @Override
    public void transform(ResourceUnifier unifier, IRecipe recipe) throws ReflectiveOperationException
    {
        unifier.setPreferredStack(this.fRecipeOutput, recipe);
    }
}
