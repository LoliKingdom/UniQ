package re.domi.uniq;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;

@SuppressWarnings("unused")
public class ForgeEventHandler
{
    private ResourceUnifier unifier;

    public ForgeEventHandler(ResourceUnifier unifier)
    {
        this.unifier = unifier;
    }

    @SubscribeEvent
    public void harvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        this.unifier.setPreferredStacks(event.drops);
    }

    @SubscribeEvent
    public void livingDrops(LivingDropsEvent event)
    {
        for (EntityItem droppedItem : event.drops)
        {
            droppedItem.setEntityItemStack(unifier.getPreferredStack(droppedItem.getEntityItem()));
        }
    }
}
