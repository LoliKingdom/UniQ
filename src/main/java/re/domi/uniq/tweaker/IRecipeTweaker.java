package re.domi.uniq.tweaker;

import net.minecraft.item.crafting.IRecipe;
import re.domi.uniq.ResourceUnifier;

public interface IRecipeTweaker extends IBaseTweaker
{
    void prepareTransform(String className) throws Exception;

    void transform(ResourceUnifier unifier, IRecipe recipe) throws Exception;
}
