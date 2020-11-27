package re.domi.uniq.forge.mixin.fluids;

import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import re.domi.uniq.fluids.FluidsHandler;

@Deprecated
@Mixin(Fluid.class)
public class FluidMixin {

    @ModifyVariable(method = "<init>(Ljava/lang/String;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/ResourceLocation;)V", at = @At("HEAD"))
    private static String captureFluidName(String fluidName) {
        String unifiedFluid;
        if ((unifiedFluid = FluidsHandler.getUnifiedFluid((fluidName))) != null) {
            return unifiedFluid.split(":")[1];
        }
        return fluidName;
    }

}
