package xyz.domi1819.uniq.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class UniQoreLoadingPlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"xyz.domi1819.uniq.core.UniQoreClassTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return "xyz.domi1819.uniq.core.UniQoreModContainer";
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
