package org.thane.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public abstract class ActionBarMessage implements Importance, Cloneable {

    public abstract BaseComponent[] getMessageFor(Player player);

    public ActionBarMessage clone() {
        return this;
    }
}
