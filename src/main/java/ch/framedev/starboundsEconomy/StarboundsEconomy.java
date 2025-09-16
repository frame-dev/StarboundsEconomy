package ch.framedev.starboundsEconomy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public final class StarboundsEconomy extends JavaPlugin {

    private static StarboundsEconomy instance;

    private Database database;

    public static final String PREFIX = "§8§l•● §f§lStarbounds §8§l┃ §7";

    @Override
    public void onEnable() {
        instance = this;

        this.database = new Database();

        new ConfigUtils(this);

        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found! Disabling StarboundsEconomy.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("eco").setExecutor(new EcoCommand(database));
        getServer().getPluginManager().registerEvents(new Events(), this);

        getServer().getServicesManager().register(Economy.class, new VaultEconomy(), this, org.bukkit.plugin.ServicePriority.High);
        getLogger().info("StarboundsEconomy has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StarboundsEconomy has been disabled!");
    }

    public static StarboundsEconomy getInstance() {
        return instance;
    }

    public Database getDatabaseSQL() {
        return database;
    }
}
