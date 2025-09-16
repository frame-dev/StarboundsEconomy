package ch.framedev.starboundsEconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PayCommand implements CommandExecutor {

    private final Database database;

    public PayCommand(Database database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Only players can use this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Usage: /pay <player> <amount>");
            return true;
        }

        Player player = (Player) sender;
        String targetName = args[0];
        double amount;

        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                sender.sendMessage(StarboundsEconomy.PREFIX + "Amount must be positive.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Invalid amount.");
            return true;
        }

        if (!database.playerExists(player.getUniqueId())) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "You do not have an account.");
            return true;
        }

        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        if (targetUUID == null) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
            return true;
        }

        if (player.getUniqueId().equals(targetUUID)) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "You cannot pay yourself.");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
        if (!database.playerExists(targetUUID)) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "Player not found.");
            return true;
        }

        double senderBalance = database.getBalance(player.getUniqueId());
        if (senderBalance < amount) {
            sender.sendMessage(StarboundsEconomy.PREFIX + "You do not have enough funds.");
            return true;
        }

        StarboundsEconomy.getInstance().getEconomy().withdrawPlayer(player, amount);
        StarboundsEconomy.getInstance().getEconomy().depositPlayer(target, amount);

        sender.sendMessage(StarboundsEconomy.PREFIX + "You have paid " + targetName + " §a$" + amount);
        if (target.isOnline() && target.getPlayer() != null) {
            target.getPlayer().sendMessage(StarboundsEconomy.PREFIX + "You have received §a$" + amount + " §7from " + player.getName());
        }
        return true;
    }
}