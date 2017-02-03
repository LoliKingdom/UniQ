package xyz.domi1819.uniq;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ResourceUnifier
{
    private HashMap<Integer, ItemStack> preferences = new HashMap<>();

    public ResourceUnifier build(Config config)
    {
        HashMap<String, Integer> modPriorities = new HashMap<>();
        String[] unificationPriorities = config.unificationPriorities;

        for (int i = 0; i < unificationPriorities.length; i++)
        {
            modPriorities.put(unificationPriorities[i], i);
        }

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

                int bestPriority = Integer.MAX_VALUE;
                ItemStack bestItemStack = null;

                for (ItemStack stack : entries)
                {
                    int priority = modPriorities.getOrDefault(this.getModId(stack), Integer.MAX_VALUE - 1);

                    if (priority < bestPriority)
                    {
                        bestItemStack = stack;
                        bestPriority = priority;
                    }
                }

                this.preferences.put(oreId, bestItemStack);
            }
        }

        return this;
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

    public void setPreferredStacks(List<ItemStack> stacks)
    {
        for (int i = 0; i < stacks.size(); i++)
        {
            stacks.set(i, this.getPreferredStack(stacks.get(i)));
        }
    }

    private String getModId(ItemStack stack)
    {
        String modId = GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId;

        if (modId.equals(""))
        {
            return "minecraft";
        }

        return modId;
    }
}
