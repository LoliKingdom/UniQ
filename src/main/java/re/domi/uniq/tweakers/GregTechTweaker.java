package re.domi.uniq.tweakers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.UniQ;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GregTechTweaker implements IGeneralTweaker
{
    public GregTechTweaker()
    {
        if (Loader.isModLoaded(getModId()))
        {
            Item metaItem = GameRegistry.findItem(getModId(), "gt.metaitem.01");

            OreDictionary.registerOre("ingotAluminum", new ItemStack(metaItem, 1, 11019));
            OreDictionary.registerOre("dustAluminum", new ItemStack(metaItem, 1, 2019));
            OreDictionary.registerOre("nuggetAluminum", new ItemStack(metaItem, 1, 9019));
            OreDictionary.registerOre("plateAluminum", new ItemStack(metaItem, 1, 17019));
            OreDictionary.registerOre("stickAluminum", new ItemStack(metaItem, 1, 23019));

            try
            {
                Class cMod = Class.forName("gregtech.GT_Mod");
                Class cProxy = Class.forName("gregtech.common.GT_Proxy");

                Object proxy = cMod.getDeclaredField("gregtechproxy").get(cMod.getDeclaredField("instance").get(null));

                cProxy.getDeclaredField("mCraftingUnification").set(proxy, false);
                cProxy.getDeclaredField("mInventoryUnification").set(proxy, false);
            }
            catch (Exception ex)
            {
                UniQ.instance.logger.error("Tweaker GregTech threw an exception while initializing:", ex);
            }
        }
    }

    @Override
    public String getName()
    {
        return "GregTech";
    }

    @Override
    public String getModId()
    {
        return "gregtech";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cRecipe = Class.forName("gregtech.api.util.GT_Recipe");
        Class cRecipeMap = Class.forName("gregtech.api.util.GT_Recipe$GT_Recipe_Map");

        Field fOutputs = cRecipe.getDeclaredField("mOutputs");
        Field fRecipeList = cRecipeMap.getDeclaredField("mRecipeList");

        Collection<?> mappings = (Collection<?>) cRecipeMap.getDeclaredField("sMappings").get(null);

        for (Object recipeMap : mappings)
        {
            Collection<?> recipeList = (Collection<?>) fRecipeList.get(recipeMap);

            for (Object recipe : recipeList)
            {
                unifier.setPreferredStacks((ItemStack[]) fOutputs.get(recipe));
            }
        }

        Class cOreDict = Class.forName("gregtech.api.util.GT_OreDictUnificator");

        Field fNameToStackMap = cOreDict.getDeclaredField("sName2StackMap");
        Field fStackToDataMap = cOreDict.getDeclaredField("sItemStack2DataMap");
        Field fUnificationTable = cOreDict.getDeclaredField("sUnificationTable");

        Field fUnificationTarget = Class.forName("gregtech.api.objects.ItemData").getDeclaredField("mUnificationTarget");

        fNameToStackMap.setAccessible(true);
        fStackToDataMap.setAccessible(true);
        fUnificationTable.setAccessible(true);

        Map<String, ItemStack> stackMap = (Map<String, ItemStack>) fNameToStackMap.get(null);

        for (Map.Entry<String, ItemStack> entry : stackMap.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }

        Map<?, ?> dataMap = (Map<?, ?>) fStackToDataMap.get(null);

        for (Map.Entry<?, ?> entry : dataMap.entrySet())
        {
            unifier.setPreferredStack(fUnificationTarget, entry.getValue());
        }
    }
}
