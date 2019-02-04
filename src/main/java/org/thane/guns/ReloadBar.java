package org.thane.guns;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.thane.utils.ActionBarMessage;

public abstract class ReloadBar extends ActionBarMessage {

    public abstract BaseComponent[] messageForTime(Reloader reloader, int ticksReloading, int totalReloadTime);

    @Override
    public BaseComponent[] getMessageFor(Player player) {
        Reloader reloader = Reloader.RELOADING_PLAYERS.get(player);
        return messageForTime(reloader, reloader.timeReloading(), reloader.getReloadTime());
    }

    @Override
    public int getImportance() {
        return 2;
    }
}
