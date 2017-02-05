package xyz.domi1819.uniq.core;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

@SuppressWarnings("unused")
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
        meta.version = "1.0";
        meta.authorList = Arrays.asList("domi1819");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}
