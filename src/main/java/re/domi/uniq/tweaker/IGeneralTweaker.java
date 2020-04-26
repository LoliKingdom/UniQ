package re.domi.uniq.tweaker;

import re.domi.uniq.ResourceUnifier;

public interface IGeneralTweaker
{
    String getName();

    String getModId();

    void run(ResourceUnifier unifier) throws ReflectiveOperationException;
}
