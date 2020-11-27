package re.domi.uniq;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = UniQ.MOD_ID)
@Mod.EventBusSubscriber(modid = UniQ.MOD_ID)
public class UniQConfig {

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(UniQ.MOD_ID)) {
            ConfigManager.sync(UniQ.MOD_ID, Config.Type.INSTANCE);
        }
    }

    public static final Fluids FLUIDS = new Fluids();
    public static final FluidBlocks FLUID_BLOCKS = new FluidBlocks();

    public static final class Fluids {

        @Config.Comment("List any Mod's Fluid you want to be prioritized. Format as such: `mod_id:fluid_id`.")
        @Config.LangKey("config." + UniQ.MOD_ID + ".fluid.fluidPriorities")
        public String[] fluidPriorities = new String[0];

        @Config.Comment("List any Fluids here where you want the colour to be change. Format as such: `fluid_id<->rgb`, where 'rgb' is an integer rgb value. For example: `water<->5778175`")
        @Config.LangKey("config." + UniQ.MOD_ID + ".fluid.fluidColourCustomizations")
        public String[] fluidColourCustomizations = new String[0];

        @Config.Comment("List any Fluids here you want to unify. Format as such: `main_fluid_id<->other_fluid_id,another_fluid_id`, For example: `oil<->crude_oil,black_gold,light_oil`")
        @Config.LangKey("config." + UniQ.MOD_ID + ".fluid.equivalentFluids")
        public String[] equivalentFluids = new String[0];

    }

    public static final class FluidBlocks {

        @Config.Comment("List any Blocks you want the Fluid to possess. If the Fluid already has a Block, this will override it. Format as such: `fluid_id<->block_id")
        @Config.LangKey("config." + UniQ.MOD_ID + ".fluid_block.fluidBlockIDs")
        public String[] fluidBlockIDs = new String[0];

    }

}
