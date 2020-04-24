package re.domi.uniq.tweakers;

import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class TinkersConstructTweaker implements IGeneralTweaker
{
    @Override
    public String getName()
    {
        return "Tinkers' Construct";
    }

    @Override
    public String getModId()
    {
        return "TConstruct";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception
    {
        Class cTConstruct = Class.forName("tconstruct.TConstruct");

        Field fCasts = Class.forName("tconstruct.library.crafting.LiquidCasting").getDeclaredField("casts");
        Field fOutput = Class.forName("tconstruct.library.crafting.CastingRecipe").getDeclaredField("output");

        fCasts.setAccessible(true);
        fOutput.setAccessible(true);

        this.processCastingRecipes(unifier, cTConstruct, "tableCasting", fCasts, fOutput);
        this.processCastingRecipes(unifier, cTConstruct, "basinCasting", fCasts, fOutput);
    }

    private void processCastingRecipes(ResourceUnifier unifier, Class cTConstruct, String castingName, Field fCasts, Field fOutput) throws Exception
    {
        for (Object recipe : (List) fCasts.get(cTConstruct.getDeclaredField(castingName).get(null)))
        {
            unifier.setPreferredStack(fOutput, recipe);
        }
    }
}
