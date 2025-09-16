package ch.framedev.starboundsEconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Events implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        // Check if player exists in the database, if not insert them
        if (!StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(event.getPlayer().getUniqueId())) {
            StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(event.getPlayer().getUniqueId());
        }
    }
}
