package re.domi.uniq.tweakers.recipe;

import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IRecipeTweaker;

import java.lang.reflect.Field;

public class MinecraftRecipeTweaker implements IRecipeTweaker
{
    private Field fRecipeOutput;

    private String obfFieldName;

    public MinecraftRecipeTweaker(String obfFieldName)
    {
        this.obfFieldName = obfFieldName;
    }

    public String getName()
    {
        return "Minecraft crafting";
    }

    public String getModId()
    {
        return "";
    }

    @Override
    public void prepareTransform(String className) throws ReflectiveOperationException
    {
        try
        {
            this.fRecipeOutput = Class.forName(className).getDeclaredField(this.obfFieldName);
        }
        catch (NoSuchFieldException ex)
        {
            this.fRecipeOutput = Class.forName(className).getDeclaredField("recipeOutput");
        }

        this.fRecipeOutput.setAccessible(true);
    }

    @Override
    public void transform(ResourceUnifier unifier, IRecipe recipe) throws ReflectiveOperationException
    {
        unifier.setPreferredStack(fRecipeOutput, recipe);
    }
}
