package org.thane.guns;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.thane.utils.ActionBarMessage;

public abstract class ZoomBar extends ActionBarMessage {

    public abstract BaseComponent[] messageForZoom(Scope scope, float zoomLevel);

    @Override
    public BaseComponent[] getMessageFor(Player player) {
        if (Scope.ZOOMED_PLAYERS.containsKey(player)) {
            return messageForZoom(Scope.ZOOMED_PLAYERS.get(player), Scope.ZOOMED_PLAYERS.get(player).getCurrentZoom());
        } else return null;
    }

    @Override
    public int getImportance() {
        return 1;
    }
}
