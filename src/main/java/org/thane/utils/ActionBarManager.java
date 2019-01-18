package org.thane.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ActionBarManager {
    private static final Map<String, ActionBarRunnable> messages = new HashMap<>();
    private static final List<String> order = new ArrayList<>();

    public static void setOrder(String... strings) {
        synchronized (order) {
            order.clear();
            Collections.addAll(order, strings);
        }
    }
    public static void setOrder(List<String> order) {
        synchronized (ActionBarManager.order) {
            ActionBarManager.order.clear();
            ActionBarManager.order.addAll(order);
        }
    }

    public static Map<String, ActionBarRunnable> getMessages() {
        return messages;
    }

    public static void removeMessage(String string) {
        synchronized (messages) {
            messages.remove(string);
        }
    }

    public static void addMessage(String string, ActionBarRunnable runnable) {
        synchronized (messages) {
            messages.put(string, runnable);
        }
    }

    public static void addOrder(String string) {
        synchronized (order) {
            order.add(string);
        }
    }

    public static void addOrder(String string, int index) {
        synchronized (order) {
            order.add(index, string);
        }
    }

    public static List<String> getOrder() {
        return order;
    }

    static {
        new TaskUtil(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String message = null;
                for (String string : order) {
                    message = messages.get(string).run(player);
                    if (message != null) break;
                }
                if (message == null) continue;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            }
        }, 60, 1).runAsync();
    }
}
