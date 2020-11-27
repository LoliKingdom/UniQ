package re.domi.uniq.forge.mixin.fluids;

import com.google.common.collect.BiMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Deprecated
@MethodsReturnNonnullByDefault
@SuppressWarnings("ConstantConditions")
@Mixin(value = FluidRegistry.class, priority = 2000)
public interface MasterFluidsAccessor {

    @Accessor(value = "masterFluidReference", remap = false)
    static BiMap<String, Fluid> accessor$masterFluidReference() { return null; }

}
