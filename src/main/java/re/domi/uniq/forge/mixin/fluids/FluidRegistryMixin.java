package re.domi.uniq.forge.mixin.fluids;

import com.google.common.collect.BiMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import re.domi.uniq.UniQ;
import re.domi.uniq.UniQLogger;
import re.domi.uniq.fluids.FluidsHandler;
import re.domi.uniq.utils.ReflectionUtils;

import java.util.Map;

@Mixin(FluidRegistry.class)
public class FluidRegistryMixin {

    @Shadow(remap = false) static BiMap<String, Fluid> fluids;

    // @Unique private static final ThreadLocal<Map<Fluid, String>> uniqNamespaces = ThreadLocal.withInitial(Object2ObjectOpenHashMap::new);

    @SuppressWarnings("all")
    @Redirect(method = "loadFluidDefaults(Lcom/google/common/collect/BiMap;Ljava/util/Set;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/BiMap;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, remap = false), remap = false)
    private static <V> V changeFluidDetermination(BiMap<String, V> biMap, Object key) {
        if (FluidsHandler.isDefaultNotPrioritized((String) key)) {
            return (V) fluids.get(((String) key).split(":")[1]);
        }
        return biMap.get(key);
    }

    /*
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "registerFluid", at = @At("HEAD"), remap = false, cancellable = true)
    private static void checkUnification(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (fluidsToBeUnified == null) {
            return;
        }
        String unifiedFluid;
        String currentFluidName = fluid.getName();
        if ((unifiedFluid = FluidsHandler.getUnifiedFluid(currentFluidName)) != null) {
            Fluid delegate;
            if ((delegate = fluids.get(unifiedFluid)) == null) {
                fluidsToBeUnified.get().put(unifiedFluid, fluid);
            }
            fluid = delegate;
            cir.setReturnValue(true);
        }
        if (fluidsToBeUnified.get().containsKey(currentFluidName)) {
            for (Fluid waitingFluid : fluidsToBeUnified.get().get(currentFluidName)) {
                waitingFluid = fluid;
            }
        }
    }
     */

    /*
    @Inject(method = "registerFluid", at = @At("HEAD"), remap = false)
    private static void checkForUnification(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        String unifiedFluid;
        String originalName;
        if ((unifiedFluid = FluidsHandler.getUnifiedFluid((originalName = fluid.getName()))) != null) {
            String[] splitUnifiedFluid = unifiedFluid.split(":");
            ReflectionUtils.setInstanceFinalField(Fluid.class, "fluidName", fluid, splitUnifiedFluid[1]);
            UniQLogger.LOGGER.info("Unifying {} to {}", originalName, unifiedFluid);
        }
    }
     */

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

    /*
     * @author Rongmario
     * @reason Take into consideration our 'delegated' namespaces

    @Overwrite(remap = false)
    private static String uniqueName(Fluid fluid) {
        ModContainer activeModContainer = Loader.instance().activeModContainer();
        String activeModContainerName = activeModContainer == null ? "minecraft" : activeModContainer.getModId();
        String name = fluid.getName();
        String delegate;
        if ((delegate = FluidsHandler.getUnifiedFluid((name))) != null) {
            activeModContainerName = activeModContainerName.equals(delegate.split(":")[0]) ? UniQ.MOD_ID : activeModContainerName;
        }
        return activeModContainerName + ":" + name;
    }
    */

}
