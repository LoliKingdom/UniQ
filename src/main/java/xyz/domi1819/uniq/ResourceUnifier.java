package xyz.domi1819.uniq;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ResourceUnifier
{
    private HashMap<Integer, ItemStack> preferences = new HashMap<>();

    public void build(Config config)
    {
        for (String prefix : config.unificationPrefixes)
        {
            for (String target : config.unificationTargets)
            {
                String name = prefix + target;

                int oreId = OreDictionary.getOreID(name);

                if (oreId < 0)
                {
                    continue;
                }

                ArrayList<ItemStack> entries = OreDictionary.getOres(name);

                this.preferences.put(oreId, entries.get(1));
            }
        }
    }

    public ItemStack getPreferredStack(ItemStack stack)
    {
        int[] ids = OreDictionary.getOreIDs(stack);

        for (int id : ids)
        {
            ItemStack result = this.preferences.get(id);

            if (result != null)
            {
                ItemStack copy = result.copy();

                copy.stackSize = stack.stackSize;

                return copy;
            }
        }

        return stack;
    }

    public void setPreferredStacksObj(Object[] objects)
    {
        for (int i = 0; i < objects.length; i++)
        {
            objects[i] = this.getPreferredStack((ItemStack)objects[i]);
        }
    }
}
