package xyz.domi1819.uniq;

import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class NEIHelper
{
    private static ResourceUnifier unifier;

    private Method mHideItem;

    public NEIHelper(boolean enable, ResourceUnifier resourceUnifier)
    {
        if (enable && Loader.isModLoaded("NotEnoughItems"))
        {
            try
            {
                this.mHideItem = Class.forName("codechicken.nei.api.API").getDeclaredMethod("hideItem", ItemStack.class);
            }
            catch (Exception ignored)
            {
            }

            unifier = resourceUnifier;
        }
    }

    public void tryHideItem(ItemStack stack)
    {
        if (this.mHideItem != null)
        {
            try
            {
                this.mHideItem.invoke(null, stack);
            }
            catch (Exception ignored)
            {
            }
        }
    }

    @SuppressWarnings("unused")
    public static void setPreferredStacks(List boxedStacks)
    {
        if (unifier != null)
        {
            for (Object stackObj : boxedStacks)
            {
                PositionedStack stack = (PositionedStack)stackObj;

                stack.item = unifier.getPreferredStack(stack.item);
                unifier.setPreferredStacks(stack.items);
            }
        }
    }
}
