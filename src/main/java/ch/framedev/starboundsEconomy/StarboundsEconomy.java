package ch.framedev.starboundsEconomy;

import ch.framedev.starboundsEconomy.commands.EcoCommand;
import ch.framedev.starboundsEconomy.commands.PayCommand;
import ch.framedev.starboundsEconomy.commands.StarboundsCommand;
import ch.framedev.starboundsEconomy.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public final class StarboundsEconomy extends JavaPlugin {

    // Singleton instance
    private static StarboundsEconomy instance;

    // Database and Economy instances
    private DatabaseHelper database;
    private Economy economy;

    // Prefix for messages
    public static final String PREFIX = "§8§l•● §f§lStarbounds §8§l┃ §7";

    @Override
    public void onEnable() {
        instance = this;

        new ConfigUtils(this);

        if (getConfig().getBoolean("use_database")) {
            database = new Database();
            getLogger().info("Using PostgreSQL database for economy.");
        } else {
            getLogger().info("Using file storage for economy.");
            database = new FileManager();
        }

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found! Disabling StarboundsEconomy.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("eco").setExecutor(new EcoCommand(database));
        this.getCommand("pay").setExecutor(new PayCommand(database));
        this.getCommand("starboundseconomy").setExecutor(new StarboundsCommand());
        getServer().getPluginManager().registerEvents(new Events(), this);

        getServer().getServicesManager().register(Economy.class, new VaultEconomy(), this, org.bukkit.plugin.ServicePriority.High);
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        getLogger().info("StarboundsEconomy has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StarboundsEconomy has been disabled!");
    }

    public static StarboundsEconomy getInstance() {
        return instance;
    }

    public DatabaseHelper getDatabaseSQL() {
        return database;
    }

    public Economy getEconomy() {
        return economy;
    }
}
