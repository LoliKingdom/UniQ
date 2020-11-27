package re.domi.uniq;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import re.domi.uniq.fluids.FluidsHandler;

@Mod(modid = UniQ.MOD_ID, name = UniQ.NAME, version = "2.1")
public class UniQ {

    public static final String MOD_ID = "uniq";
    public static final String NAME = "UniQ";

    @Mod.Instance
    public static UniQ INSTANCE;
    public static ModContainer CONTAINER;

    {
        INSTANCE = this;
        CONTAINER = Loader.instance().activeModContainer();
    }

    // TODO: Have to run configs earlier to support Mekanism and its stupid ways
    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        if (UniQConfig.FLUIDS.fluidPriorities.length > 0) {
            for (final String string : UniQConfig.FLUIDS.fluidPriorities) {
                FluidsHandler.prioritize(string);
            }
        }
        if (UniQConfig.FLUIDS.equivalentFluids.length > 0) {
            for (final String string : UniQConfig.FLUIDS.equivalentFluids) {
                FluidsHandler.unify(string);
            }
        }
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        if (UniQConfig.FLUIDS.fluidColourCustomizations.length > 0) {
            UniQLogger.LOGGER.info("Setting custom fluid colours...");
            for (final String id : UniQConfig.FLUIDS.fluidColourCustomizations) {
                int arrowIndex = id.indexOf("<->");
                if (arrowIndex == -1) {
                    throw new IllegalArgumentException(id + ": is not valid! Check config comment for formatting tips!");
                }
                String fluidString = id.substring(0, arrowIndex);
                UniQLogger.LOGGER.info(fluidString);
                Fluid fluid;
                if ((fluid = FluidRegistry.getFluid(fluidString)) == null) {
                    throw new IllegalArgumentException(fluidString + ": doesn't exist in the Fluids Registry!");
                }
                String colour = id.substring(arrowIndex + 3);
                try {
                    int intColour = colour.startsWith("0x") ? Long.decode(colour).intValue() : (int) Long.parseLong(colour);
                    fluid.setColor(intColour);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(colour + " of " + fluidString + " is not a valid colour!");
                }
            }
            UniQLogger.LOGGER.info("Finished setting custom fluid colours...");
        }
    }

}
