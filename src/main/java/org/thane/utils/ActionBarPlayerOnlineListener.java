package org.thane.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActionBarPlayerOnlineListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ActionBar.ACTION_BARS.remove(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ActionBar.ACTION_BARS.put(event.getPlayer(), new ActionBar());
    }
}
