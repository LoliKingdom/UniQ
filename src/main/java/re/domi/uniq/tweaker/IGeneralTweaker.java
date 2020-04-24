package re.domi.uniq.tweaker;

import re.domi.uniq.ResourceUnifier;

public interface IGeneralTweaker extends IBaseTweaker
{
    void run(ResourceUnifier unifier) throws Exception;
}
