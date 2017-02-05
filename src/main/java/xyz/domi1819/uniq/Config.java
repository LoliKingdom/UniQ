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

    private Configuration config;

    public Config(File file)
    {
        this.config = new Configuration(file);
    }

    public Config load()
    {
        this.config.load();

        this.unificationTargets = this.config.getStringList("unificationTargets", "settings", new String[]{"Iron", "Gold", "Copper", "Tin", "Silver", "Lead", "Nickel", "Platinum", "Bronze", "Invar", "Electrum", "Steel", "Aluminum", "Lithium"}, "");
        this.unificationPrefixes = this.config.getStringList("unificationPrefixes", "settings", new String[]{"ingot", "block", "ore", "dust", "plate", "gear", "nugget"}, "");
        this.unificationInclusions = this.config.getStringList("unificationInclusions", "settings", new String[]{"dustObsidian", "dustStone", "dustCoal", "dustCharcoal", "dustDiamond", "dustLapis", "dustWood", "dustPlastic", "sheetPlastic", "blockPlastic", "itemRubber", "fuelCoke", "blockFuelCoke"}, "");

        this.unificationPriorities = this.config.getStringList("unificationPriorities", "settings", new String[]{"minecraft", "ThermalFoundation", "Mekanism", "ImmersiveEngineering", "IC2"}, "");

        if (this.config.hasChanged())
        {
            this.config.save();
        }

        return this;
    }
}
