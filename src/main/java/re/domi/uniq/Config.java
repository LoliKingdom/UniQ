package re.domi.uniq;

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
    public String[] unificationBlacklist;

    public String[] customOreDictNames;

    public boolean printUnknownRecipeClasses;

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

        this.customOreDictNames = this.config.getStringList("customOreDictNames", "settings", defaults.customNames(), "Custom additions to the Ore Dictionary. Useful if a mod doesn't add common names to specific items.");

        this.printUnknownRecipeClasses = this.config.getBoolean("printUnknownRecipeClasses", "settings", false, "Print IRecipe class names UniQ doesn't know how to transform.");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }

    private static class Defaults
    {
        private String[] targets()
        {
            return new String[]
                    {
                            "Aluminum",
                            "Amber",
                            "Brass",
                            "Bronze",
                            "CertusQuartz",
                            "Cobalt",
                            "Copper",
                            "Electrum",
                            "Gold",
                            "Invar",
                            "Iron",
                            "Lead",
                            "Lithium",
                            "NetherQuartz",
                            "Nickel",
                            "Osmium",
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
                            "thermalfoundation",
                            "Mekanism",
                            "ImmersiveEngineering",
                            "TConstruct",
                            "appliedenergistics2",
                            "ic2",
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

        private String[] customNames()
        {
            return new String[]
                    {
                            "dustNetherQuartz:NuclearCraft:material:13",

                            "dustNetherQuartz:ImmersiveEngineering:metal:18",
                            "ingotAluminium:ImmersiveEngineering:metal:1",
                            "dustAluminium:ImmersiveEngineering:metal:11",
                            "nuggetAluminium:ImmersiveEngineering:metal:23",
                            "plateAluminium:ImmersiveEngineering:metal:32",
                            "stickAluminium:ImmersiveEngineering:material:16"
                    };
        }

    }
}
