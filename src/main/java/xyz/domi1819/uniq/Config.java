package xyz.domi1819.uniq;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
    public String[] unificationTargets;
    public String[] unificationPrefixes;

    public String[] unificationPriorities;

    private Configuration config;

    public Config(File file)
    {
        this.config = new Configuration(file);
    }

    public Config load()
    {
        this.config.load();

        this.unificationTargets = this.config.getStringList("unificationTargets", "settings", new String[]{"Copper", "Iron"}, "");
        this.unificationPrefixes = this.config.getStringList("unificationPrefixes", "settings", new String[]{"ingot", "dust", "nugget"}, "");

        this.unificationPriorities =this.config.getStringList("unificationPriorities", "settings", new String[]{"minecraft", "ThermalFoundation"}, "");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }
}
