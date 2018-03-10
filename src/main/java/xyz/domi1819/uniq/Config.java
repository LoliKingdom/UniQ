package xyz.domi1819.uniq;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

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

        Defaults defaults = new Defaults();

        this.unificationTargets = this.config.getStringList("unificationTargets", "settings", defaults.targets, "");
        this.unificationPrefixes = this.config.getStringList("unificationPrefixes", "settings", defaults.prefixes, "");
        this.unificationInclusions = this.config.getStringList("unificationInclusions", "settings", defaults.inclusions, "");

        this.unificationPriorities = this.config.getStringList("unificationPriorities", "settings", defaults.priorities, "");
        this.unificationOverrides = this.config.getStringList("unificationOverrides", "settings", defaults.overrides, "");

        this.enableNEIIntegration = this.config.getBoolean("enableNEIIntegration", "settings", true, "");
        this.debug = this.config.getBoolean("debug", "settings", false, "");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }

    private static class Defaults
    {
        // @formatter:off

        private String[] targets = new String[]
        {
            "Aluminum",
            "Amber",
            "Bronze",
            "Copper",
            "Electrum",
            "Gold",
            "Invar",
            "Iron",
            "Lead",
            "Lithium",
            "Nickel",
            "Peridot",
            "Platinum",
            "Ruby",
            "Sapphire",
            "Silver",
            "Steel",
            "Tin",
            "Uranium"
        };

        private String[] prefixes = new String[]
        {
            "block",
            "dust",
            "dustTiny",
            "gear",
            "gem",
            "ingot",
            "nugget",
            "ore",
            "plate"
        };

        private String[] inclusions = new String[]
        {
            "blockFuelCoke",
            "blockPlastic",
            "dustCharcoal",
            "dustCoal",
            "dustDiamond",
            "dustLapis",
            "dustObsidian",
            "dustPlastic",
            "dustSalt",
            "dustSaltpeter",
            "dustStone",
            "dustSulfur",
            "dustWood",
            "fuelCoke",
            "itemRubber",
            "sheetPlastic"
        };

        private String[] priorities = new String[]
        {
            "minecraft",
            "ThermalFoundation",
            "Mekanism",
            "ImmersiveEngineering",
            "IC2"
        };

        private String[] overrides = new String[]
        {
            "oreAluminum:TConstruct"
        };

        // @formatter:on
    }
}
