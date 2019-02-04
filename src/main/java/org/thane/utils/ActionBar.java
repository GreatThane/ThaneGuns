package org.thane.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class ActionBar {

    static final ConcurrentMap<Player, ActionBar> ACTION_BARS = new ConcurrentHashMap<>();

    public static ActionBar getActionBar(Player player) {
        return ACTION_BARS.get(player);
    }

    private final Set<ActionBarMessage> messages = new ConcurrentSkipListSet<>();

    public void add(ActionBarMessage message) {
        messages.add(message);
    }

    public void remove(ActionBarMessage message) {
        messages.remove(message);
    }

    public boolean has(ActionBarMessage message) {
        return messages.contains(message);
    }

    public  boolean has(Class<? extends ActionBarMessage> messageClass) {
        return messages.stream().anyMatch(m -> m.getClass().isAssignableFrom(messageClass));
    }

    public void remove(Class<? extends ActionBarMessage> messageClass) {
        messages.stream().filter(m -> m.getClass().isAssignableFrom(messageClass))
                .collect(Collectors.toSet())
                .forEach(messages::remove);
    }

    public Set<ActionBarMessage> getMessages() {
        return messages;
    }

    static {
        new TaskUtil(() -> {
            for (Map.Entry<Player, ActionBar> entry : ACTION_BARS.entrySet()) {
                BaseComponent[] message = null;
                for (ActionBarMessage message1 : entry.getValue().getMessages()) {
                    message = message1.getMessageFor(entry.getKey());
                    if (message != null) break;
                }
                if (message == null) continue;
                entry.getKey().spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
            }
        }, 60, 1).runAsync();
    }
}
