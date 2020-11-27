package re.domi.uniq.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import re.domi.uniq.UniQConfig;
import re.domi.uniq.UniQLogger;

public class FluidBlocksHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void afterBlockRegistration(RegistryEvent.Register<Item> event) {
        if (UniQConfig.FLUID_BLOCKS.fluidBlockIDs.length > 0) {
            UniQLogger.LOGGER.info("Setting preferred fluid blocks...");
            for (final String id : UniQConfig.FLUID_BLOCKS.fluidBlockIDs) {
                int arrowIndex = id.indexOf("<->"), colonIndex = id.indexOf(':');
                if (colonIndex == -1 || arrowIndex == -1) {
                    throw new IllegalArgumentException(id + ": is not valid! Check config comment for formatting tips!");
                }
                String fluidString = id.substring(0, arrowIndex);
                UniQLogger.LOGGER.info(fluidString);
                Fluid fluid;
                if ((fluid = FluidRegistry.getFluid(fluidString)) == null) {
                    throw new IllegalArgumentException(fluidString + ": doesn't exist in the Fluids Registry!");
                }
                ResourceLocation location = new ResourceLocation(id.substring(arrowIndex + 3, colonIndex), id.substring(colonIndex + 1));
                UniQLogger.LOGGER.info(location);
                Block block;
                if ((block = ForgeRegistries.BLOCKS.getValue(location)) == null || block instanceof BlockAir) {
                    throw new IllegalArgumentException(location.toString() + ": doesn't exist in the Blocks Registry!");
                }
                // if (!(block instanceof IFluidBlock)) {
                    // UniQLogger.LOGGER.warn("{} is not a valid Fluid Block.", block);
                // }
                fluid.setBlock(block);
            }
            UniQLogger.LOGGER.info("Finished setting preferred fluid blocks.");
        }
    }

}
