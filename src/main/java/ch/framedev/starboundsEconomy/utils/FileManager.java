package ch.framedev.starboundsEconomy.utils;

import ch.framedev.starboundsEconomy.StarboundsEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class FileManager implements DatabaseHelper {

    private final File file;
    private final FileConfiguration config;

    public FileManager() {
        this.file = new File(StarboundsEconomy.getInstance().getDataFolder(), "economy.yml");
        if (!file.exists()) {
            try {
                if(!file.createNewFile())
                    StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not create economy.yml");
            } catch (IOException e) {
                StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not create economy.yml", e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    private boolean set(String path, Object value) {
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not save economy.yml after setting " + path, e);
            return false;
        }
    }

    private Object get(String path) {
        return config.get(path);
    }

    public boolean setBalance(UUID uuid, int balance) {
        return set("players." + uuid.toString() + ".balance", balance);
    }

    public int getBalance(UUID uuid) {
        Object value = get("players." + uuid.toString() + ".balance");
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    public boolean playerExists(UUID uuid) {
        List<String> players = config.contains("accounts") ? config.getStringList("accounts") : new ArrayList<>();
        return players.contains(uuid.toString());
    }

    public boolean insertPlayer(UUID uuid) {
        if (playerExists(uuid)) return false;
        List<String> players = config.contains("accounts") ? config.getStringList("accounts") : new ArrayList<>();
        players.add(uuid.toString());
        set("accounts", players);
        if (!setBalance(uuid, StarboundsEconomy.getInstance().getConfig().getInt("starting_balance", 100))) {
            return false;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not save economy.yml after inserting player " + uuid, e);
            return false;
        }
        return true;
    }
}
