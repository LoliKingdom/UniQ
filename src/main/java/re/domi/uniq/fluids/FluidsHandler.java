package re.domi.uniq.fluids;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fluids.Fluid;
import re.domi.uniq.UniQLogger;

import javax.annotation.Nullable;
import java.util.Map;

public class FluidsHandler {

    private static final Map<String, String> priorityMap = new Object2ObjectOpenHashMap<>();

    private static final Multimap<String, String> unificationMap = HashMultimap.create();
    private static final Map<String, String> inverseUnificationLookup = new Object2ObjectOpenHashMap<>();

    /*
    static {
        priorityMap.put("water", "minecraft");
        priorityMap.put("lava", "minecraft");
    }
     */

    public static void prioritize(String string) {
        String[] split = string.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException(string + ": is not valid! Check config comment for formatting tips!");
        }
        priorityMap.put(split[1], split[0]);
    }

    public static void unify(String string) {
        UniQLogger.LOGGER.info("Processing {}", string);
        String[] split = string.split("<->");
        if (split.length != 2) {
            throw new IllegalArgumentException(string + ": is not valid! Check config comment for formatting tips!");
        }
        String[] primarySplit = split[0].split(":");
        if (primarySplit.length != 2) {
            throw new IllegalArgumentException(string + ": is not valid! Check config comment for formatting tips!");
        }
        priorityMap.put(primarySplit[1], primarySplit[0]);
        for (String type : split[1].split(",")) {
            UniQLogger.LOGGER.warn(type);
            unificationMap.put(split[0], type);
            inverseUnificationLookup.put(type, split[0]);
        }
    }

    public static Map<String, String> getPriority() {
        return priorityMap;
    }

    public static Multimap<String, String> getUnifications() {
        return unificationMap;
    }

    public static Map<String, String> getInverseUnifications() {
        return inverseUnificationLookup;
    }

    @Nullable
    public static String getUnifiedFluid(String lookup) {
        return inverseUnificationLookup.get(lookup);
    }

    public static boolean isFluidUnified(Fluid fluid) {
        return isFluidUnified(fluid.getName());
    }

    public static boolean isFluidUnified(String fluidName) {
        return inverseUnificationLookup.containsKey(fluidName);
    }

    public static boolean isDefaultNotPrioritized(String defaultName) {
        String[] split = defaultName.split(":");
        String namespace;
        if ((namespace = priorityMap.get(split[1])) == null) {
            return false;
        }
        return !namespace.equals(split[0]);
    }

    private FluidsHandler() { }

}
