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
        setupMessages(plugin);
    }

    private void setupMessages(StarboundsEconomy plugin) {
        FileConfiguration config = plugin.getConfig();
        if(!config.contains("messages")) {
            ConfigurationSection messages = config.createSection("messages");
            messages.set("only_player", "&cDieser Befehl kann nur von einem Spieler ausgeführt werden.");
            messages.set("no_permission", "&cDu hast keine Berechtigung, diesen Befehl auszuführen.");
            messages.set("player_not_found", "&cSpieler nicht gefunden.");
            messages.set("player_no_account", "&cDer Spieler &e{player} &chat kein Konto.");
            messages.set("player_self_no_account", "&cDu hast kein Konto. Bitte kontaktiere einen Administrator.");
            messages.set("specify_player", "&cBitte gib einen Spieler namen an.");
            messages.set("invalid_amount", "&cBitte gib einen gültigen Betrag an.");
            messages.set("amount_positive", "&cDer Betrag muss positiv sein.");
            messages.set("insufficient_funds", "&cDu hast nicht genügend Guthaben.");

            messages.set("balance_info", "&aDein Kontostand beträgt: &e{balance} ★");
            messages.set("balance_info_other", "&aDer Kontostand von &e{player} &abeträgt: &e{balance} ★");
            messages.set("usage_balance", "&cVerwendung: /eco balance [Spieler]");

            messages.set("usage_pay", "&cVerwendung: /pay <Spieler> <Betrag>");
            messages.set("pay_self", "&cDu kannst dir selbst kein Geld schicken.");
            messages.set("pay_success_sender", "&aDu hast &e{amount} ★ &aan &e{player} &agesendet.");
            messages.set("pay_other", "&aDu hast von &e{player} {amount} ★ &abekommen.");

            messages.set("usage_eco_set", "&cVerwendung: /eco set <Spieler> <Betrag>");
            messages.set("eco_set_success", "&aDer Kontostand von &e{player} &awurde auf &e{balance} ★ &agesetzt.");
            messages.set("eco_set_success_sender", "&aDu hast den Kontostand von &e{player} &aauf &e{balance} ★ &agesetzt.");
            messages.set("eco_set_failure", "&cFehler beim Setzen des Kontostands von &e{player}&c.");
            plugin.saveConfig();
        }
    }
}
