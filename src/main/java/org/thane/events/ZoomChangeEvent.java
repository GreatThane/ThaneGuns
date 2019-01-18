package org.thane.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thane.guns.Scope;

import java.util.LinkedHashMap;
import java.util.Map;

public class ZoomChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private Scope scope;
    private float previousZoom;
    private float newZoom;

    public ZoomChangeEvent(float previousZoom, float newZoom, Scope scope) {
        this.previousZoom = previousZoom;
        this.newZoom = newZoom;
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public float getPreviousZoom() {
        return previousZoom;
    }

    public void setPreviousZoom(float previousZoom) {
        this.previousZoom = previousZoom;
    }

    public float getNewZoom() {
        return newZoom;
    }

    public void setNewZoom(float newZoom) {
        this.newZoom = newZoom;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
