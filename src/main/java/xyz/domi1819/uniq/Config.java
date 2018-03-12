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
    public String[] unificationBlacklist;

    public boolean enableNEIIntegration;

    private Configuration config;

    public Config(File file)
    {
        this.config = new Configuration(file);
    }

    public Config load()
    {
        this.config.load();

        Defaults defaults = new Defaults();

        this.unificationTargets = this.config.getStringList("unificationTargets", "settings", defaults.targets(), "Item types that should be unified, without their prefixes.");
        this.unificationPrefixes = this.config.getStringList("unificationPrefixes", "settings", defaults.prefixes(), "Prefixes that will be prepended to the targets.");
        this.unificationInclusions = this.config.getStringList("unificationInclusions", "settings", defaults.inclusions(), "Additionally included types.");

        this.unificationPriorities = this.config.getStringList("unificationPriorities", "settings", defaults.priorities(), "ModIds in the order that should be used to find the best items.");
        this.unificationOverrides = this.config.getStringList("unificationOverrides", "settings", defaults.overrides(), "Types that should be forced for unification to a specific mod.");
        this.unificationBlacklist = this.config.getStringList("unificationBlacklist", "settings", defaults.blacklist(), "Specific items that should never be unified. Optionally with meta/dmg.");

        this.enableNEIIntegration = this.config.getBoolean("hideVariantsNEI", "settings", false, "");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }

    private static class Defaults
    {
        // @formatter:off

        private String[] targets()
        {
            return new String[]
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
        }

        private String[] prefixes()
        {
            return new String[]
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
        }

        private String[] inclusions()
        {
            return new String[]
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
        }

        private String[] priorities()
        {
            return new String[]
            {
                "minecraft",
                "ThermalFoundation",
                "Mekanism",
                "ImmersiveEngineering",
                "IC2"
            };
        }

        private String[] overrides()
        {
            return new String[]
            {
                "oreAluminum:TConstruct"
            };
        }

        private String[] blacklist()
        {
            return new String[]
            {
                "TConstruct:GravelOre",
                "IC2:itemPartIndustrialDiamond:0"
            };
        }

        // @formatter:on
    }
}
