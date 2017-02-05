package xyz.domi1819.uniq.tweaker;

import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;

public interface IRecipeTweaker extends IBaseTweaker
{
    void prepareTransform(String className) throws Exception;

    void transform(ResourceUnifier unifier, IRecipe recipe) throws Exception;
}
