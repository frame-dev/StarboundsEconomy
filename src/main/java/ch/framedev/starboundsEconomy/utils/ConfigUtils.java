package ch.framedev.starboundsEconomy.utils;

import ch.framedev.starboundsEconomy.StarboundsEconomy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {

    public ConfigUtils(StarboundsEconomy plugin) {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        FileConfiguration config = plugin.getConfig();
        if(!config.contains("database")) {
            ConfigurationSection database = config.createSection("database");
            database.set("host", "localhost");
            database.set("port", 5432);
            database.set("name", "your_database");
            database.set("user", "your_username");
            database.set("password", "your_password");
            plugin.saveConfig();
        }
    }
}
