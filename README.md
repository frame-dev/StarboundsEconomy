# **StarboundsEconomy**

A simple Minecraft economy plugin with Vault support.

## **Features**

- Check your own or others' balances
- Set player balances (with permission)
- Vault integration for compatibility with other plugins
- PostgreSQL database support

## **Installation**

1. Download the plugin JAR and place it in your server's `plugins` folder.
2. Ensure [Vault](https://dev.bukkit.org/projects/vault) is installed.
3. Restart your server.
4. Configure the plugin by editing the `config.yml` file in the `plugins/StarboundsEconomy` folder.

## **Commands**

- `/balance [player]` — Check your own or another player's balance
- `/eco set <amount> <player>` — Main command for balance checking
- `/pay <player> <amount>` — Pay another player

[] means optional, <> means required.

## **Permissions**

- `starboundseconomy.balance` — Check your own balance (default: true)
- `starboundseconomy.balance.others` — Check others' balances (default: op)
- `starboundseconomy.set` — Set another player's balance (default: op)

## **License**

This project is licensed under the GNU 3.0 License. See the `LICENSE` file for details.
