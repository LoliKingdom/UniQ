package xyz.domi1819.uniq.tweakers.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.ICraftingTweaker;

import java.lang.reflect.Field;

public class BasicCraftingTweaker implements ICraftingTweaker
{
    Field fRecipeOutput;

    String fieldName;
    String modId = "";

    public BasicCraftingTweaker(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public BasicCraftingTweaker(String fieldName, String modId)
    {
        this.fieldName = fieldName;
        this.modId = modId;
    }

    @Override
    public String getName()
    {
        return "Minecraft recipes";
    }

    @Override
    public String getModId()
    {
        return "";
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
        ItemStack output = (ItemStack)this.fRecipeOutput.get(recipe);
        ItemStack replacement = unifier.getPreferredStack(output);

        if (!output.isItemEqual(replacement))
        {
            this.fRecipeOutput.set(recipe, replacement);
        }
    }
}
