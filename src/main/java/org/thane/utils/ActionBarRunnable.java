package org.thane.utils;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ActionBarRunnable {

    String run(Player player);
}
