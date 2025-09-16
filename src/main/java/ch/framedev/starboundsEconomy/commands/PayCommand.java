package ch.framedev.starboundsEconomy.commands;

import ch.framedev.starboundsEconomy.StarboundsEconomy;
import ch.framedev.starboundsEconomy.utils.DatabaseHelper;
import ch.framedev.starboundsEconomy.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PayCommand implements CommandExecutor {

    private final DatabaseHelper database;

    public PayCommand(DatabaseHelper database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.only_player", "&cNur Spieler können diesen Befehl ausführen.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }
        if (args.length != 2) {
            String usagePay = StarboundsEconomy.getInstance().getConfig().getString("messages.usage_pay", "&cVerwendung: /pay <Spieler> <Betrag>");
            usagePay = usagePay.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + usagePay);
            return true;
        }

        Player player = (Player) sender;
        String targetName = args[0];
        int amount;

        try {
            amount = Integer.parseInt(args[1]);
            if (amount <= 0) {
                String message = StarboundsEconomy.getInstance().getConfig().getString("messages.amount_positive", "&cDer Betrag muss positiv sein.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
        } catch (NumberFormatException e) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.invalid_amount", "&cBitte gib einen gültigen Betrag an.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        if (!database.playerExists(player.getUniqueId())) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.player_self_no_account", "&cDu hast kein Konto. Bitte kontaktiere einen Administrator.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        if (targetUUID == null) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.player_not_found", "&cSpieler nicht gefunden.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        if (player.getUniqueId().equals(targetUUID)) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.pay_self", "&cDu kannst dir selbst kein Geld schicken.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
        if (!database.playerExists(targetUUID)) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.player_no_account", "&cDer Spieler &e{player} &chat kein Konto.");
            message = message.replace("&", "§").replace("{player}", targetName);
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        double senderBalance = database.getBalance(player.getUniqueId());
        if (senderBalance < amount) {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.insufficient_funds", "&cDu hast nicht genügend Guthaben.");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }

        StarboundsEconomy.getInstance().getEconomy().withdrawPlayer(player, amount);
        StarboundsEconomy.getInstance().getEconomy().depositPlayer(target, amount);

        String message = StarboundsEconomy.getInstance().getConfig().getString("messages.pay_success_sender", "&aDu hast &e{amount} ★ &aan &e{player} &agesendet.");
        message = message.replace("&", "§").replace("{amount}", String.valueOf(amount)).replace("{player}", targetName);
        sender.sendMessage(StarboundsEconomy.PREFIX + message);
        if (target.isOnline() && target.getPlayer() != null) {
            String messageReceiver = StarboundsEconomy.getInstance().getConfig().getString("messages.pay_other", "&aDu hast von &e{player} {amount} ★ &abekommen.");
            messageReceiver = messageReceiver.replace("{player}", player.getName()).replace("{amount}", String.valueOf(amount));
            messageReceiver = messageReceiver.replace("&", "§");
            target.getPlayer().sendMessage(StarboundsEconomy.PREFIX + messageReceiver);
        }
        return true;
    }
}