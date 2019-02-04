package org.thane.guns;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.thane.utils.ActionBar;
import org.thane.utils.TaskUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Reloader implements Cloneable {

    static final Map<Player, Reloader> RELOADING_PLAYERS = new ConcurrentHashMap<>();

    private ReloadBar reloadBar = new ReloadBar() {
        @Override
        public BaseComponent[] messageForTime(Reloader reloader, int ticksReloading, int totalReloadTime) {
            ComponentBuilder builder = new ComponentBuilder("Reloading: ").color(ChatColor.YELLOW);
            float percentCompleted = ticksReloading / (float) totalReloadTime;
            for (int i = 1; i <= 10; i++) {
                float percentOfBar = i / 10F;
                builder.append("\u2588");
                if (percentOfBar < percentCompleted) {
                    builder.color(ChatColor.GRAY);
                } else builder.color(ChatColor.GREEN);
            }
            return builder.create();
        }
    };

    private int maxAmmo;
    private int ammo = maxAmmo;
    private int reloadTime;
    private int timeReloading = 0;
    private boolean reloading = false;
    private Player owner;

    public Reloader(int maxAmmo, int reloadTime) {
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
    }

    public boolean incrementAmmo() {
        return setAmmo(getAmmo() + 1);
    }

    public boolean decrementAmmo() {
        return setAmmo(getAmmo() - 1);
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean setMaxAmmo(int maxAmmo) {
        if (maxAmmo < 0) return false;
        this.maxAmmo = maxAmmo;
        return true;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean setAmmo(int ammo) {
        return setAmmo(ammo, false);
    }

    public boolean setAmmo(int ammo, boolean overFill) {
        if (ammo > maxAmmo && !overFill) return false;
        if (ammo < 0) return false;
        this.ammo = ammo;
        return true;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void refill() {
        this.ammo = maxAmmo;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void cancelReload() {
        if (!isReloading()) return;
        reloading = false;
        RELOADING_PLAYERS.remove(owner);
        ActionBar.getActionBar(owner).remove(reloadBar);

    }

    public void reload() {
        if (isReloading()) return;
        reloading = true;
        timeReloading = 0;
        RELOADING_PLAYERS.put(owner, this);
        ActionBar.getActionBar(owner).add(reloadBar);

    }

    public ReloadBar getReloadBar() {
        return reloadBar;
    }

    public void setReloadBar(ReloadBar reloadBar) {
        this.reloadBar = reloadBar;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int timeReloading() {
        return timeReloading;
    }

    @Override
    public Reloader clone() {
        try {
            Reloader reloader = (Reloader) super.clone();
            reloader.reloadBar = (ReloadBar) reloadBar.clone();
            return reloader;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        new TaskUtil(() -> {
            for (Map.Entry<Player, Reloader> entry : RELOADING_PLAYERS.entrySet()) {
                entry.getValue().timeReloading++;
                if (entry.getValue().timeReloading >= entry.getValue().reloadTime) {
                    entry.getValue().cancelReload();
                }
            }
        }, 60, 1).runAsync();
    }
}
