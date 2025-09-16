package ch.framedev.starboundsEconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EcoCommand implements CommandExecutor {

    private final Database database;

    public EcoCommand(Database database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("balance")) {
            if (!sender.hasPermission("starboundseconomy.balance")) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "You don't have permission to use this command.");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Please specify a player name.");
                return true;
            }
            Player player = (Player) sender;
            double balance = database.getBalance(player.getUniqueId());
            sender.sendMessage(StarboundsEconomy.PREFIX + player.getName() + "'s balance is: " + balance + " ★");
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("balance")) {
            if (!sender.hasPermission("starboundseconomy.balance.others")) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "You don't have permission to check others' balances.");
                return true;
            }
            UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
            if (!database.playerExists(target.getUniqueId())) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
                return true;
            }
            double balance = database.getBalance(target.getUniqueId());
            sender.sendMessage(StarboundsEconomy.PREFIX + target.getName() + "'s balance is: " + balance + " ★");
            return true;
        } else if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("starboundseconomy.set")) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "You don't have permission to use this command.");
                return true;
            }
            UUID targetUUID = UUIDFetcher.getUUID(args[2]);
            if (targetUUID == null) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
            if (!database.playerExists(target.getUniqueId())) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Invalid amount.");
                return true;
            }
            if (amount < 0) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Amount must be non-negative.");
                return true;
            }
            boolean success = database.setBalance(target.getUniqueId(), amount);
            if (success) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Set " + target.getName() + "'s balance to " + amount + " ★");
                if (target.isOnline()) {
                    target.getPlayer().sendMessage(StarboundsEconomy.PREFIX + "Your balance has been set to " + amount + " ★ by an administrator.");
                }
            } else {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Failed to set balance. Please try again later.");
            }
            return true;
        } else {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Usage:");
            sender.sendMessage("/eco balance - Check your balance");
            sender.sendMessage("/eco balance <player> - Check another player's balance");
            sender.sendMessage("/eco set <amount> <player> - Set a player's balance (admin only)");
            return true;
        }
    }
}