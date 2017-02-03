package xyz.domi1819.uniq.tweaker;

import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;

public interface ICraftingTweaker extends IBaseTweaker
{
    public void prepareTransform(String className) throws Exception;
    public void transform(ResourceUnifier unifier, IRecipe recipe) throws Exception;
}
