package ch.framedev.starboundsEconomy.commands;

import ch.framedev.starboundsEconomy.StarboundsEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StarboundsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("starboundseconomy.reload")) {
                StarboundsEconomy.getInstance().reloadConfig();
                StarboundsEconomy.getInstance().getDatabaseSQL().reload();
                String message = StarboundsEconomy.getInstance().getConfig().getString("message.reload_success", "&aDie Konfiguration wurde erfolgreich neu geladen.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
            } else {
                String message = StarboundsEconomy.getInstance().getConfig().getString("messages.no_permission", "&cDu hast keine Rechte, um diesen Befehl zu nutzen.");
                message = message.replace("&", "§");
                sender.sendMessage(StarboundsEconomy.PREFIX + message);
                return true;
            }
        } else {
            String message = StarboundsEconomy.getInstance().getConfig().getString("messages.usage_starboundseconomy", "&cVerwendung: /starboundseconomy reload");
            message = message.replace("&", "§");
            sender.sendMessage(StarboundsEconomy.PREFIX + message);
            return true;
        }
        return true;
    }
}
