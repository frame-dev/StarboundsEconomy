# StarboundsEconomy

A simple Minecraft economy plugin with Vault support.

## Features

- Check your own or others' balances
- Set player balances (with permission)
- Vault integration for compatibility with other plugins

## Installation

1. Download the plugin JAR and place it in your server's `plugins` folder.
2. Ensure [Vault](https://dev.bukkit.org/projects/vault) is installed.
3. Restart your server.

## Commands

- `/balance [player]` — Check your own or another player's balance
- `/eco set <amount> <player>` — Main command for balance checking

## Permissions

- `starboundseconomy.balance` — Check your own balance (default: true)
- `starboundseconomy.balance.others` — Check others' balances (default: op)
- `starboundseconomy.set` — Set another player's balance (default: op)

## Configuration

No configuration required for basic usage.

## License

MIT
