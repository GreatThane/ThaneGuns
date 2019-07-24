package org.thane.guns;

import com.google.common.collect.Iterables;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thane.events.ZoomChangeEvent;
import org.thane.packetWrappers.WrapperPlayServerAbilities;
import org.thane.utils.ActionBar;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Scope {

    static final Map<Player, Scope> ZOOMED_PLAYERS = new ConcurrentHashMap<>();

    private ZoomBar zoomBar = new ZoomBar() {
        @Override
        public BaseComponent[] messageForZoom(Scope scope, float zoomLevel) {
            ComponentBuilder builder = new ComponentBuilder("Zoom Level: ").color(ChatColor.GRAY);
            for (float level : scope.getZoomLevels()) {
                builder.append(String.valueOf(level));
                if (level == zoomLevel) {
                    builder.color(ChatColor.DARK_GRAY);
                } else builder.color(ChatColor.GREEN);
                if (level != Iterables.getLast(scope.zoomLevels)) {
                    builder.append(" > ").color(ChatColor.GRAY);
                }
            }
            return builder.create();
        }
    };

    private List<Float> zoomLevels = new ArrayList<Float>() {{
        zoomLevels.add(1F);
        zoomLevels.add(2F);
        zoomLevels.add(4F);
        zoomLevels.add(8F);
    }};

    private Material scope = Material.WOODEN_HOE;
    private short scopeData = 1;

    private int index = 0;

    private Player owner;

    public Scope() {
    }

    public Scope(Material scope) {
        this.scope = scope;
    }

    public Scope(Material material, short scopeData) {
        this(material);
        this.scopeData = scopeData;
    }

    public Scope(Material material, List<Float> zoomLevels) {
        this(material);
        this.zoomLevels = zoomLevels;
        Collections.sort(this.zoomLevels);
    }

    public Scope(Material material, Float... zoomLevels) {
        this(material, Arrays.asList(zoomLevels));
    }

    public Scope(Material material, short scopeData, Float... zoomLevels) {
        this(material, scopeData, Arrays.asList(zoomLevels));
    }

    public Scope(Material material, short scopeData, List<Float> zoomLevels) {
        this(material, scopeData);
        this.zoomLevels = zoomLevels;
        Collections.sort(this.zoomLevels);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public float getCurrentZoom() {
        return zoomLevels.get(index);
    }

    public void setCurrentZoom(float zoom) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoom, this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            index = zoomLevels.indexOf(zoom);
            sendPacket(owner);
        }
    }

    public void setCurrentZoom(int index) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(this.index), zoomLevels.get(index), this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.index = index;
            sendPacket(owner);
        }
    }

    public Material getScope() {
        return scope;
    }

    public void setScope(Material scope) {
        this.scope = scope;
    }

    public void setZoomLevels(Float... zoomLevels) {
        this.zoomLevels = Arrays.asList(zoomLevels);
    }

    public void increaseZoom() {
        increaseZoom(1);
    }

    public void increaseZoom(int amount) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoomLevels.get(index + amount), this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            index += amount;
            sendPacket(owner);
        }
    }

    public void decreaseZoom() {
        decreaseZoom(1);
    }

    public void decreaseZoom(int amount) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoomLevels.get(index - amount), this);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            index -= amount;
            sendPacket(owner);
        }
    }

    public boolean isZoomed() {
        return index != 0;
    }

    protected int getIndex() {
        return index;
    }

    public List<Float> getZoomLevels() {
        return zoomLevels;
    }

    public void setZoomLevels(List<Float> zoomLevels) {
        this.zoomLevels = zoomLevels;
    }

    public short getScopeData() {
        return scopeData;
    }

    public void setScopeData(short scopeData) {
        this.scopeData = scopeData;
    }

    public double getZoom(float zoomLevel) {
        return -0.0055568182 * zoomLevel * zoomLevel + 0.1024068182 * zoomLevel + -0.4720166667;
    }

    public double getZoom() {
        return getZoom(zoomLevels.get(index));
    }

    public ZoomBar getZoomBar() {
        return zoomBar;
    }

    public void setZoomBar(ZoomBar zoomBar) {
        this.zoomBar = zoomBar;
    }

    public void sendPacket(Player player) {
        if (player == null) return;
        WrapperPlayServerAbilities packet = new WrapperPlayServerAbilities();

        if (getCurrentZoom() == 1) {
            packet.setWalkingSpeed(0.2F);
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            ZOOMED_PLAYERS.remove(player);
            ActionBar.getActionBar(player).remove(zoomBar);
        } else {

            if (player.getInventory().getItemInOffHand() == null
                    || player.getInventory().getItemInOffHand().getType() == Material.AIR) {
                player.getInventory().setItemInOffHand(new ItemStack(scope, 1, scopeData));
                ZOOMED_PLAYERS.put(player, this);
                ActionBar.getActionBar(player).add(zoomBar);
            }
            packet.setWalkingSpeed((float) getZoom());
        }
        switch (player.getGameMode()) {
            case CREATIVE:
                packet.setCanInstantlyBuild(true);
            case SPECTATOR:
                packet.setInvulnerable(true);
                packet.setCanFly(true);
                packet.setFlyingSpeed(0.085F);
        }
        packet.sendPacket(player);
    }
}
