package re.domi.uniq;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ResourceUnifier
{
    private HashMap<Integer, ItemStack> preferences = new HashMap<>();
    private HashMap<String, String> overrides = new HashMap<>();
    private HashSet<String> blacklist = new HashSet<>();

    public void addOredictNames(String[] rules)
    {
        for (String rule : rules)
        {
            String[] ruleComponents = rule.split(":");

            if (ruleComponents.length < 3 || ruleComponents.length > 5)
            {
                UniQ.LOGGER.error("Custom OreDict rule " + rule + " couldn't be parsed. Skipping.");
                continue;
            }

            if (Loader.isModLoaded(ruleComponents[1]) || ruleComponents[1].equals("minecraft"))
            {
                int meta = 0;
                Item item = Item.REGISTRY.getObject(new ResourceLocation(ruleComponents[1], ruleComponents[2]));

                if (item == null)
                {
                    UniQ.LOGGER.error("Item " + ruleComponents[1] + ":" + ruleComponents[2] + " wasn't found despite the mod being loaded!");
                    continue;
                }

                if (ruleComponents.length == 4)
                {
                    meta = Integer.parseInt(ruleComponents[3]);
                }

                OreDictionary.registerOre(ruleComponents[0], new ItemStack(item, 1, meta));
            }
        }
    }

    public void prepareUnificationMaps(Config config)
    {
        Collections.addAll(blacklist, config.unificationBlacklist);

        HashMap<String, Integer> modPriorities = new HashMap<>();

        for (int i = 0; i < config.unificationPriorities.length; i++)
        {
            modPriorities.put(config.unificationPriorities[i], i);
        }

        for (String override : config.unificationOverrides)
        {
            String[] overrideComponents = override.split(":");

            if (overrideComponents.length != 2)
            {
                UniQ.LOGGER.error("Format error in line " + override);
                continue;
            }

            this.overrides.put(overrideComponents[0], overrideComponents[1]);
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
    }

    public ItemStack getPreferredStack(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return stack;
        }

        String identifier = getIdentifier(stack);

        if (this.blacklist.contains(identifier) || this.blacklist.contains(String.format("%s:%s", identifier, stack.getItemDamage())))
        {
            return stack;
        }

        int[] ids = OreDictionary.getOreIDs(stack);

        for (int id : ids)
        {
            ItemStack result = this.preferences.get(id);

            if (result != null)
            {
                ItemStack copy = result.copy();

                copy.setCount(stack.getCount());

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

    public List<ItemStack> setPreferredStacks(List<ItemStack> stacks)
    {
        if (stacks != null)
        {
            for (int i = 0; i < stacks.size(); i++)
            {
                stacks.set(i, this.getPreferredStack(stacks.get(i)));
            }
        }

        return stacks;
    }

    @SuppressWarnings("ConstantConditions")
    public void setPreferredStack(Field field, Object instance) throws ReflectiveOperationException
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
        if (!OreDictionary.doesOreNameExist(name))
        {
            return;
        }

        int oreId = OreDictionary.getOreID(name);

        List<ItemStack> entries = OreDictionary.getOres(name);
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
                bestItemStack = stack;
                bestPriority = priority;
            }
        }

        this.preferences.put(oreId, bestItemStack);
    }

    @SuppressWarnings("ConstantConditions")
    private String getModId(ItemStack stack)
    {
        String modId = Item.REGISTRY.getNameForObject(stack.getItem()).getResourceDomain();

        return modId.equals("") ? "minecraft" : modId;
    }

    private String getIdentifier(ItemStack stack)
    {
        ResourceLocation loc = Item.REGISTRY.getNameForObject(stack.getItem());

        return loc == null ? null : loc.getResourceDomain().equals("") ? "minecraft:" + loc.getResourcePath() : loc.toString();
    }
}
