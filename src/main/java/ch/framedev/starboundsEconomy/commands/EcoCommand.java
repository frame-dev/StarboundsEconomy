package ch.framedev.starboundsEconomy.commands;

import ch.framedev.starboundsEconomy.utils.Database;
import ch.framedev.starboundsEconomy.StarboundsEconomy;
import ch.framedev.starboundsEconomy.utils.UUIDFetcher;
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
        StarboundsEconomy plugin = StarboundsEconomy.getInstance();
        if (args.length == 1 && args[0].equalsIgnoreCase("balance")) {
            if (!sender.hasPermission("starboundseconomy.balance")) {
                String message = plugin.getConfig().getString("messages.no_permission", "&cDu hast keine Berechtigung, diesen Befehl auszuführen.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            if (!(sender instanceof Player)) {
                String message = plugin.getConfig().getString("messages.only_player", "&cDieser Befehl kann nur von einem Spieler ausgeführt werden.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            Player player = (Player) sender;
            double balance = database.getBalance(player.getUniqueId());
            String message = plugin.getConfig().getString("messages.balance_info", "&aDein Kontostand beträgt: &e{balance}");
            message = message.replace("&", "§").replace("{balance}", String.valueOf(balance));
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("balance")) {
            if (!sender.hasPermission("starboundseconomy.balance.others")) {
                String message = plugin.getConfig().getString("messages.no_permission", "&cDu hast keine Berechtigung, diesen Befehl auszuführen.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                String message = plugin.getConfig().getString("messages.player_not_found", "&cSpieler nicht gefunden.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
            if (!database.playerExists(target.getUniqueId())) {
                String message = plugin.getConfig().getString("messages.player_not_found", "&cSpieler nicht gefunden.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            double balance = database.getBalance(target.getUniqueId());
            String message = plugin.getConfig().getString("messages.balance_info_other", "&aDer Kontostand von &e{player} &abeträgt: &e{balance}");
            message = message.replace("&", "§").replace("{player}", target.getName()).replace("{balance}", String.valueOf(balance));
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        } else if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("starboundseconomy.set")) {
                String message = plugin.getConfig().getString("messages.no_permission", "&cDu hast keine Berechtigung, diesen Befehl auszuführen.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            UUID targetUUID = UUIDFetcher.getUUID(args[2]);
            if (targetUUID == null) {
                String message = plugin.getConfig().getString("messages.player_not_found", "&cSpieler nicht gefunden.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
            if (!database.playerExists(target.getUniqueId())) {
                String message = plugin.getConfig().getString("messages.player_not_found", "&cSpieler nicht gefunden.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                String message = plugin.getConfig().getString("messages.invalid_amount", "&cBitte gib einen gültigen Betrag an.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            if (amount < 0) {
                String message = plugin.getConfig().getString("messages.amount_positive", "&cDer Betrag muss positiv sein.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
            boolean success = database.setBalance(target.getUniqueId(), amount);
            if (success) {
                String message = plugin.getConfig().getString("messages.eco_set_success", "&aDer Kontostand von &e{player} &awurde auf &e{balance} &agesetzt.");
                message = message.replace("&", "§").replace("{player}", target.getName()).replace("{balance}", String.valueOf(amount));
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                if (target.isOnline()) {
                    String messageSender = plugin.getConfig().getString("messages.eco_set_success_sender", "&aDu hast den Kontostand von &e{player} &aauf &e{balance} &agesetzt.");
                    messageSender = messageSender.replace("&", "§").replace("{player}", target.getName()).replace("{balance}", String.valueOf(amount));
                    target.getPlayer().sendMessage(StarboundsEconomy.PREFIX + messageSender);
                }
            } else {
                String message = plugin.getConfig().getString("messages.eco_set_failure", "&cFehler beim Setzen des Kontostands von &e{player}&c.");
                message = message.replace("&", "§").replace("{player}", target.getName());
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
            }
            return true;
        } else {
            String usageBalance = plugin.getConfig().getString("messages.usage_balance", "&cVerwendung: /balance [Spieler]");
            usageBalance = usageBalance.replace("&", "§");
            String usageSet = plugin.getConfig().getString("messages.usage_eco_set", "&cVerwendung: /eco set <Betrag> <Spieler>");
            usageSet = usageSet.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + usageBalance);
            sender.sendMessage(StarboundsEconomy.PREFIX + usageSet);
            return true;
        }
    }
}