package re.domi.uniq.forge.mixin.fluids;

import com.google.common.collect.BiMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import re.domi.uniq.fluids.FluidsHandler;

@Mixin(FluidRegistry.class)
public class FluidRegistryMixin {

    @Shadow(remap = false) static BiMap<String, Fluid> fluids;

    @SuppressWarnings("all")
    @Redirect(method = "loadFluidDefaults(Lcom/google/common/collect/BiMap;Ljava/util/Set;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/BiMap;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, remap = false), remap = false)
    private static <V> V changeFluidDetermination(BiMap<String, V> biMap, Object key) {
        if (FluidsHandler.isDefaultNotPrioritized((String) key)) {
            return (V) fluids.get(((String) key).split(":")[1]);
        }
        return biMap.get(key);
    }

    @SuppressWarnings("all")
    @Redirect(method = "registerFluid", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/BiMap;containsKey(Ljava/lang/Object;)Z", remap = false), remap = false)
    private static boolean considerPrioritization(BiMap<String, Fluid> biMap, Object key, Fluid fluid) {
        if (biMap.containsKey(key)) {
            return true;
        }
        String namespace;
        ModContainer container;
        if ((namespace = FluidsHandler.getPriority().get(key)) == null || (container = Loader.instance().activeModContainer()) == null) {
            return false;
        }
        return !namespace.equals(container.getModId());
    }

    /**
     * @author Rongmario
     * @reason Taking unified fluids into consideration
     **/
    @Overwrite(remap = false)
    public static Fluid getFluid(String fluidName) {
        Fluid fluid;
        if ((fluid = fluids.get(fluidName)) == null) {
            String unifyFluid = FluidsHandler.getUnifiedFluid(fluidName);
            if (unifyFluid == null) {
                return null;
            }
            return fluids.get(unifyFluid.split(":")[1]);
        }
        return fluid;
    }

    /**
     * @author Rongmario
     * @reason Taking unified fluids into consideration
     **/
    @Overwrite(remap = false)
    public static boolean isFluidRegistered(Fluid fluid) {
        return fluid != null && isFluidRegistered(fluid.getName());
    }

    /**
     * @author Rongmario
     * @reason Taking unified fluids into consideration
     **/
    @Overwrite(remap = false)
    public static boolean isFluidRegistered(String fluidName) {
        return fluids.containsKey(fluidName) || FluidsHandler.isFluidUnified(fluidName);
    }

}
