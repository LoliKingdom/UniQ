package xyz.domi1819.uniq.tweakers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import xyz.domi1819.uniq.ITweaker;
import xyz.domi1819.uniq.ResourceUnifier;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unchecked")
public class MinecraftTweaker implements ITweaker
{
    @Override
    public String getName()
    {
        return "Minecraft";
    }

    @Override
    public String getModId()
    {
        return null;
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        this.processCraftingRecipes(unifier);
        this.processFurnaceRecipes(unifier);
        this.processChestGen(unifier);
    }

    private void processCraftingRecipes(ResourceUnifier unifier)
    {

    }

    private void processFurnaceRecipes(ResourceUnifier unifier)
    {
        HashMap<ItemStack, ItemStack> recipes = (HashMap<ItemStack, ItemStack>)FurnaceRecipes.smelting().getSmeltingList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }
    }

    private void processChestGen(ResourceUnifier unifier) throws Exception
    {
        Field fChestInfo = ChestGenHooks.class.getDeclaredField("chestInfo");
        Field fContents = ChestGenHooks.class.getDeclaredField("contents");

        fChestInfo.setAccessible(true);
        fContents.setAccessible(true);

        HashMap<String, ChestGenHooks> chestInfo = (HashMap<String, ChestGenHooks>) fChestInfo.get(null);

        for (ChestGenHooks chestGenHooks : chestInfo.values())
        {
            List<WeightedRandomChestContent> contents = (List<WeightedRandomChestContent>) fContents.get(chestGenHooks);

            for (WeightedRandomChestContent content : contents)
            {
                content.theItemId = unifier.getPreferredStack(content.theItemId);
            }
        }
    }
}
