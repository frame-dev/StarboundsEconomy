# StarboundsEconomy

A simple Minecraft economy plugin with Vault support.

## Features

- Check your own or others' balances
- Set player balances (with permission)
- Vault integration for compatibility with other plugins
- PostgreSQL database support

## Installation

1. Download the plugin JAR and place it in your server's `plugins` folder.
2. Ensure [Vault](https://dev.bukkit.org/projects/vault) is installed.
3. Restart your server.

## Commands

- `/balance [player]` — Check your own or another player's balance
- `/eco set <amount> <player>` — Main command for balance checking
- `/pay <player> <amount>` — Pay another player

## Permissions

- `starboundseconomy.balance` — Check your own balance (default: true)
- `starboundseconomy.balance.others` — Check others' balances (default: op)
- `starboundseconomy.set` — Set another player's balance (default: op)

## Configuration

PostgreSQL configuration can be set in `config.yml`:

```yaml
database:
  host: localhost
  port: 5432
  name: minecraft
  user: yourusername
  password: yourpassword
```

## License

MIT
