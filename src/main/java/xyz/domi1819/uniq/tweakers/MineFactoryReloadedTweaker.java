package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import xyz.domi1819.uniq.ResourceUnifier;
import xyz.domi1819.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MineFactoryReloadedTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "MineFactory Reloaded";
    }

    @Override
    public String getModId()
    {
        return "MineFactoryReloaded";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cMFRRegistry = Class.forName("powercrystals.minefactoryreloaded.MFRRegistry");

        Field fLaserOres = cMFRRegistry.getDeclaredField("_laserOres");
        Field fLaserPreferredOres = cMFRRegistry.getDeclaredField("_laserPreferredOres");
        Field fStack = Class.forName("cofh.lib.util.WeightedRandomItemStack").getDeclaredField("stack");

        fLaserOres.setAccessible(true);
        fLaserPreferredOres.setAccessible(true);
        fStack.setAccessible(true);

        for (Object ore : (List) fLaserOres.get(null))
        {
            unifier.setPreferredStack(fStack, ore);
        }

        for (Map.Entry<Integer, List<ItemStack>> entry : ((Map<Integer, List<ItemStack>>) fLaserPreferredOres.get(null)).entrySet())
        {
            unifier.setPreferredStacks(entry.getValue());
        }
    }
}
