package re.domi.uniq.tweakers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import re.domi.uniq.RecipeProcessor;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MinecraftTweaker implements IGeneralTweaker
{
    private RecipeProcessor recipeProcessor;

    public MinecraftTweaker(RecipeProcessor recipeProcessor)
    {
        this.recipeProcessor = recipeProcessor;
    }

    @Override
    public String getName()
    {
        return "Minecraft";
    }

    @Override
    public String getModId()
    {
        return "";
    }

    @Override
    public void run(ResourceUnifier unifier)
    {
        this.recipeProcessor.transform(unifier, CraftingManager.REGISTRY);

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }

        MinecraftForge.EVENT_BUS.register(new EventHandler(unifier));
    }

    @SuppressWarnings("unused")
    public static class EventHandler
    {
        private ResourceUnifier unifier;

        private Field fPools;

        private EventHandler(ResourceUnifier unifier)
        {
            this.unifier = unifier;

            try
            {
                this.fPools = LootTable.class.getDeclaredField("pools");
                this.fPools.setAccessible(true);
            }
            catch (NoSuchFieldException ex)
            {
                ex.printStackTrace();
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void harvestDrops(BlockEvent.HarvestDropsEvent event)
        {
            this.unifier.setPreferredStacks(event.getDrops());
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void livingDrops(LivingDropsEvent event)
        {
            for (EntityItem droppedItem : event.getDrops())
            {
                droppedItem.setItem(unifier.getPreferredStack(droppedItem.getItem()));
            }
        }

        @SuppressWarnings("unchecked")
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void lootTableLoad(LootTableLoadEvent event)
        {
            try
            {
                List<LootPool> pools = (List<LootPool>) fPools.get(event.getTable());
                event.setTable(new UnifiedLootTable(pools.toArray(new LootPool[0]), this.unifier));
            }
            catch (IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }

        @SuppressWarnings({"NullableProblems", "WeakerAccess"})
        private static class UnifiedLootTable extends LootTable
        {
            private ResourceUnifier unifier;

            public UnifiedLootTable(LootPool[] poolsIn, ResourceUnifier unifier)
            {
                super(poolsIn);

                this.unifier = unifier;
            }

            @Override
            public List<ItemStack> generateLootForPools(Random rand, LootContext context)
            {
                return this.unifier.setPreferredStacks(super.generateLootForPools(rand, context));
            }
        }
    }
}
