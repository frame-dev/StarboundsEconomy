package ch.framedev.starboundsEconomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VaultEconomy implements Economy {

    private static final String CURRENCY_NAME = "Stars";
    private static final String CURRENCY_NAME_PLURAL = "Stars";
    private static final String CURRENCY_SYMBOL = "★";

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "StarboundsEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return amount + " " + CURRENCY_SYMBOL;
    }

    @Override
    public String currencyNamePlural() {
        return CURRENCY_NAME_PLURAL;
    }

    @Override
    public String currencyNameSingular() {
        return CURRENCY_NAME;
    }

    @Override
    public boolean hasAccount(String playerName) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid == null) {
            return false;
        }
        return StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid == null) {
            return false;
        }
        return StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(uuid);
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName, String world) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(uuid);
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(player.getUniqueId());
    }

    @Override
    public boolean has(String playerName, double amount) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(uuid) >= amount;
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(player.getUniqueId()) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(uuid) >= amount;
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().getBalance(player.getUniqueId()) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double currentBalance = getBalance(playerName);
        if (currentBalance < amount) {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            boolean success = StarboundsEconomy.getInstance().getDatabaseSQL().setBalance(uuid, currentBalance - amount);
            if (success) {
                return new EconomyResponse(amount, currentBalance - amount, EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Database error");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        double currentBalance = getBalance(player);
        if (currentBalance < amount) {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        boolean success = StarboundsEconomy.getInstance().getDatabaseSQL().setBalance(player.getUniqueId(), currentBalance - amount);
        if (success) {
            return new EconomyResponse(amount, currentBalance - amount, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Database error");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double currentBalance = getBalance(playerName);
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            boolean success = StarboundsEconomy.getInstance().getDatabaseSQL().setBalance(uuid, currentBalance + amount);
            if (success) {
                return new EconomyResponse(amount, currentBalance + amount, EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Database error");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double currentBalance = getBalance(player);
        boolean success = StarboundsEconomy.getInstance().getDatabaseSQL().setBalance(player.getUniqueId(), currentBalance + amount);
        if (success) {
            return new EconomyResponse(amount, currentBalance + amount, EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, currentBalance, EconomyResponse.ResponseType.FAILURE, "Database error");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(uuid);
        } else {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            return StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(uuid);
        } else {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(player.getUniqueId());
    }
}
