package xyz.domi1819.uniq;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
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
                this.findBestItem(prefix + target, modPriorities);
            }
        }

        for (String name : config.unificationInclusions)
        {
            this.findBestItem(name, modPriorities);
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

    public void setPreferredStacks(ItemStack[] stacks)
    {
        if (stacks == null)
        {
            return;
        }

        for (int i = 0; i < stacks.length; i++)
        {
            stacks[i] = this.getPreferredStack(stacks[i]);
        }
    }

    public void setPreferredStacksObj(Object[] objects)
    {
        if (objects == null)
        {
            return;
        }

        for (int i = 0; i < objects.length; i++)
        {
            objects[i] = this.getPreferredStack((ItemStack) objects[i]);
        }
    }

    public void setPreferredStacks(List<ItemStack> stacks)
    {
        if (stacks == null)
        {
            return;
        }

        for (int i = 0; i < stacks.size(); i++)
        {
            stacks.set(i, this.getPreferredStack(stacks.get(i)));
        }
    }

    public void setPreferredStack(Field field, Object instance) throws Exception
    {
        ItemStack output = (ItemStack) field.get(instance);
        ItemStack replacement = this.getPreferredStack(output);

        if (output != null && !output.isItemEqual(replacement))
        {
            field.set(instance, replacement);
        }
    }

    private void findBestItem(String name, HashMap<String, Integer> modPriorities)
    {
        int oreId = OreDictionary.getOreID(name);

        if (oreId < 0)
        {
            return;
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
