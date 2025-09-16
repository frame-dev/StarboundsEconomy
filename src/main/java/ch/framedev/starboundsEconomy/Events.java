package ch.framedev.starboundsEconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Events implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        if (!StarboundsEconomy.getInstance().getDatabaseSQL().playerExists(event.getPlayer().getUniqueId())) {
            StarboundsEconomy.getInstance().getDatabaseSQL().insertPlayer(event.getPlayer().getUniqueId());
        }
    }
}
