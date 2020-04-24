package re.domi.uniq.core;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

public class UniQoreModContainer extends DummyModContainer
{
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public UniQoreModContainer()
    {
        super(new ModMetadata());

        ModMetadata meta = this.getMetadata();

        meta.modId = "uniqore";
        meta.name = "UniQore";
        meta.description = "UniQ CoreMod for NEI integration";
        meta.version = "1.1";
        meta.url = "https://github.com/FakeDomi/UniQ";
        meta.authorList = Arrays.asList("Domi");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}
