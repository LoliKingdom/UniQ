package re.domi.uniq.tweaker;

import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.ResourceUnifier;

public interface IRecipeTweaker
{
    String getName();

    String getModId();

    void prepareTransform(String className) throws ReflectiveOperationException;

    void transform(ResourceUnifier unifier, IRecipe recipe) throws ReflectiveOperationException;
}
