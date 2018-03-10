package xyz.domi1819.uniq;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceUnifier
{
    private HashMap<Integer, ItemStack> preferences = new HashMap<>();
    private HashMap<String, String> overrides = new HashMap<>();
    private NEIHelper neiHelper;

    public ResourceUnifier build(Config config, Logger logger)
    {
        HashMap<String, Integer> modPriorities = new HashMap<>();
        String[] unificationPriorities = config.unificationPriorities;

        this.neiHelper = new NEIHelper(config.enableNEIIntegration, this);

        for (int i = 0; i < unificationPriorities.length; i++)
        {
            modPriorities.put(unificationPriorities[i], i);
        }

        for (String line : config.unificationOverrides)
        {
            int colonIndex = line.indexOf(":");

            if (colonIndex == -1)
            {
                logger.error("Format error in line " + line);
                continue;
            }

            this.overrides.put(line.substring(0, colonIndex), line.substring(colonIndex + 1, line.length()));
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
        String overrideModId = this.overrides.get(name);

        int bestPriority = Integer.MAX_VALUE;
        ItemStack bestItemStack = null;

        for (ItemStack stack : entries)
        {
            String modId = this.getModId(stack);
            int priority = modPriorities.getOrDefault(modId, Integer.MAX_VALUE - 1);

            if (modId.equals(overrideModId))
            {
                priority = Integer.MIN_VALUE;
            }

            if (priority < bestPriority)
            {
                if (bestItemStack != null)
                {
                    this.neiHelper.tryHideItem(bestItemStack);
                }

                bestItemStack = stack;
                bestPriority = priority;
            }
            else
            {
                this.neiHelper.tryHideItem(stack);
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
