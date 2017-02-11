package xyz.domi1819.uniq;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class Config
{
    public String[] unificationTargets;
    public String[] unificationPrefixes;
    public String[] unificationInclusions;

    public String[] unificationPriorities;

    public String[] unificationOverrides;

    public boolean enableNEIIntegration;
    public boolean debug;

    private Configuration config;

    public Config(File file)
    {
        this.config = new Configuration(file);
    }

    public Config load()
    {
        this.config.load();

        this.unificationTargets = this.config.getStringList("unificationTargets", "settings", new String[] {"Iron", "Gold", "Copper", "Tin", "Silver", "Lead", "Nickel", "Platinum", "Bronze", "Invar", "Electrum", "Steel", "Aluminum", "Lithium", "Ruby", "Sapphire", "Amber"}, "");
        this.unificationPrefixes = this.config.getStringList("unificationPrefixes", "settings", new String[] {"ingot", "block", "ore", "dust", "dustTiny", "plate", "gear", "nugget", "gem"}, "");
        this.unificationInclusions = this.config.getStringList("unificationInclusions", "settings", new String[] {"dustObsidian", "dustStone", "dustCoal", "dustCharcoal", "dustDiamond", "dustLapis", "dustWood", "dustSalt", "dustPlastic", "sheetPlastic", "blockPlastic", "itemRubber", "fuelCoke", "blockFuelCoke"}, "");

        this.unificationPriorities = this.config.getStringList("unificationPriorities", "settings", new String[] {"minecraft", "ThermalFoundation", "Mekanism", "ImmersiveEngineering", "IC2"}, "");

        this.unificationOverrides = this.config.getStringList("unificationOverrides", "settings", new String[] {"oreAluminum:TConstruct"}, "");

        this.enableNEIIntegration = this.config.getBoolean("enableNEIIntegration", "settings", true, "");
        this.debug = this.config.getBoolean("debug", "settings", false, "");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }
}
