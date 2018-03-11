package xyz.domi1819.uniq;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;

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
        }

        unifier = resourceUnifier;
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
    public static ItemStack getPreferredStack(ItemStack stack)
    {
        return unifier.getPreferredStack(stack);
    }

    @SuppressWarnings("unused")
    public static boolean itemsEqual(ItemStack first, ItemStack second)
    {
        return first == second || first != null && first.getItem() == second.getItem() && first.getItemDamage() == second.getItemDamage();
    }
}
